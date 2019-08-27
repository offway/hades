package cn.offway.hades.runner;

import cn.offway.hades.domain.PhConfig;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.service.PhConfigService;
import cn.offway.hades.service.PhGoodsService;
import cn.offway.hades.service.PhGoodsStockService;
import cn.offway.hades.service.PhOrderInfoService;
import cn.offway.hades.singleton.JobHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
public class InitRunner implements ApplicationRunner {

    @Autowired
    private PhConfigService configService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsStockService stockService;
    @Autowired
    private PhOrderInfoService orderInfoService;
    private static Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//        JobDetail job = JobBuilder.newJob().build();
//        scheduler.addJob(job, true);
//        scheduler.start();

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String jsonStr = configService.findContentByName("CRONJOB");
        if (jsonStr == null || "".equals(jsonStr.trim())) {
            //no nothing
            logger.info("nothing to do with CRONJOB");
        } else {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            for (String key : jsonObject.keySet()) {
                System.out.println(key);
                String[] args = key.split("_");
                String sTimeStr = args[0];
                String eTimeStr = args[1];
                String goodIds = args[2];
                Date sTime = DateTime.parse(sTimeStr, format).toDate();
                Date eTime = DateTime.parse(eTimeStr, format).toDate();
                Date now = new Date();
                if (sTime.compareTo(now) < 0 && eTime.compareTo(now) < 0) {
                    removeTaskFormDB(key, configService);
                    continue;
                }
                if ("".equals(goodIds.trim())) {
                    removeTaskFormDB(key, configService);
                    continue;
                }
                JSONArray taskList = jsonObject.getJSONArray(key);
                createJob(taskList, key, sTime, eTime, now, goodsService, stockService, orderInfoService, configService);
            }
        }
    }

    public static void createJob(JSONArray taskList, String key, Date sTime, Date eTime, Date now, PhGoodsService goodsService, PhGoodsStockService stockService, PhOrderInfoService orderInfoService, PhConfigService configService) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(taskList.size(), factory);
        ScheduledExecutorService poolReverse = Executors.newScheduledThreadPool(taskList.size(), factory);
        for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
            //calc the delay in seconds
            long delaySeconds = sTime.getTime() - now.getTime();
            if (delaySeconds >= 0) {
                pool.schedule(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        long id = task.containsKey("gid") ? task.getLongValue("gid") : task.getLongValue("id");
                        String type = "discount";
                        if (task.containsKey("type")) {
                            type = task.getString("type");
                        }
                        PhGoods goods;
                        PhGoodsStock goodsStock;
                        switch (type) {
                            case "discount":
                                double discount = task.getDoubleValue("discount");
                                goods = goodsService.findOne(id);
                                if (goods != null) {
                                    //update goods
                                    if (discount >= 1) {
                                        goods.setOriginalPrice(null);
                                    } else {
                                        goods.setOriginalPrice(goods.getPrice());
                                    }
                                    goods.setPrice(goods.getPrice() * discount);
                                    //update stock
                                    List<Double> priceList = new ArrayList<>();
                                    for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                        stock.setPrice(stock.getPrice() * discount);
                                        PhGoodsStock stockSaved = stockService.save(stock);
                                        priceList.add(stockSaved.getPrice());
                                    }
                                    goods.setPriceRange(genPriceRange(priceList));
                                    goodsService.save(goods);
                                }
                                break;
                            case "goodsPrice":
                                double goodsPrice = task.getDoubleValue("goodsPrice");
                                double goodsPriceOriginal = task.getDoubleValue("goodsPriceOriginal");
                                goods = goodsService.findOne(id);
                                if (goods != null) {
                                    goods.setPrice(goodsPrice);
                                    goods.setOriginalPrice(goodsPriceOriginal);
                                    logger.info("setPrice to " + goodsPrice);
                                    logger.info("setOriginalPrice to " + goodsPriceOriginal);
                                    logger.info("now is " + new Date());
                                    goodsService.save(goods);
                                    //re-calc priceRange 3 seconds later
                                    calcPriceRange(id, stockService, goodsService);
                                }
                                break;
                            case "stockPrice":
                                double stockPrice = task.getDoubleValue("stockPrice");
                                goodsStock = stockService.findOne(id);
                                if (goodsStock != null) {
                                    goodsStock.setPrice(stockPrice);
                                    stockService.save(goodsStock);
                                }
                                break;
                            case "confirmPackage":
                                long oid = task.getLongValue("id");
                                PhOrderInfo orderInfo = orderInfoService.findOne(oid);
                                if (orderInfo != null) {
                                    orderInfo.setStatus("3");
                                    orderInfoService.save(orderInfo);
                                }
                                break;
                        }
                        checkAndPurgeTask(key, configService);
                        return null;
                    }
                }, delaySeconds, TimeUnit.MILLISECONDS);
            }
            //reverse job
            //calc the delay in seconds
            long delaySecondsReverse = eTime.getTime() - now.getTime();
            if (delaySecondsReverse > 0) {
                poolReverse.schedule(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        long id = task.containsKey("gid") ? task.getLongValue("gid") : task.getLongValue("id");
                        String type = "discount";
                        if (task.containsKey("type")) {
                            type = task.getString("type");
                        }
                        PhGoods goods;
                        PhGoodsStock goodsStock;
                        switch (type) {
                            case "discount":
                                double discount = task.getDoubleValue("discount");
                                goods = goodsService.findOne(id);
                                if (goods != null) {
                                    //update goods
                                    goods.setPrice(goods.getPrice() / discount);
                                    goods.setOriginalPrice(null);
                                    //update stock
                                    List<Double> priceList = new ArrayList<>();
                                    for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                        stock.setPrice(stock.getPrice() / discount);
                                        PhGoodsStock stockSaved = stockService.save(stock);
                                        priceList.add(stockSaved.getPrice());
                                    }
                                    goods.setPriceRange(genPriceRange(priceList));
                                    goodsService.save(goods);
                                }
                                break;
                            case "goodsPrice":
                                double goodsPrice = task.getDoubleValue("goodsPriceOriginal");
                                goods = goodsService.findOne(id);
                                if (goods != null) {
                                    goods.setPrice(goodsPrice);
                                    goods.setOriginalPrice(null);
                                    logger.info("setPrice to " + goodsPrice);
                                    logger.info("setOriginalPrice to NULL");
                                    logger.info("now is " + new Date());
                                    goodsService.save(goods);
                                    //re-calc priceRange 3 seconds later
                                    calcPriceRange(id, stockService, goodsService);
                                }
                                break;
                            case "stockPrice":
                                double stockPrice = task.getDoubleValue("stockPriceOriginal");
                                goodsStock = stockService.findOne(id);
                                if (goodsStock != null) {
                                    goodsStock.setPrice(stockPrice);
                                    stockService.save(goodsStock);
                                }
                                break;
                        }
                        checkAndPurgeTask(key + "_REVERSE", configService);
                        return null;
                    }
                }, delaySecondsReverse, TimeUnit.MILLISECONDS);
            }
        }
        //link to global register
        JobHolder.getHolder().put(key, pool);
        JobHolder.getHolder().put(key + "_REVERSE", poolReverse);
    }

    public static String genPriceRange(List<Double> priceList) {
        Collections.sort(priceList);
        Double lowest = priceList.get(0);
        Double highest = priceList.get(priceList.size() - 1);
        if (Double.compare(lowest, highest) == 0) {
            return String.format("%.2f", lowest);
        } else {
            return String.format("%.2f-%.2f", lowest, highest);
        }
    }

    private static void calcPriceRange(Long gid, PhGoodsStockService stockService, PhGoodsService goodsService) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);//wait all sub task done
                    //update PriceRange
                    List<Double> priceList = new ArrayList<>();
                    for (PhGoodsStock stock : stockService.findByPid(gid)) {
                        priceList.add(stock.getPrice());
                    }
                    PhGoods goods = goodsService.findOne(gid);
                    goods.setPriceRange(genPriceRange(priceList));
                    goodsService.save(goods);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void checkAndPurgeTask(String key, PhConfigService configService) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (JobHolder.getHolder().containsKey(key)) {
                        ExecutorService service = JobHolder.getHolder().get(key);
                        if (service.isShutdown() || service.isTerminated()) {
                            JobHolder.getHolder().remove(key);
                            removeTaskFormDB(key, configService);
                        } else {
                            service.shutdown();
                            if (service.awaitTermination(5, TimeUnit.SECONDS)) {
                                JobHolder.getHolder().remove(key);
                                removeTaskFormDB(key, configService);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void removeTaskFormDB(String key, PhConfigService configService) {
        //save to DB
        PhConfig config = configService.findOne("CRONJOB");
        if (config.getContent() != null && !"".equals(config.getContent())) {
            JSONObject jsonObject = JSONObject.parseObject(config.getContent());
            if (jsonObject.containsKey(key)) {
                logger.info("检测到已完成任务，从数据库删除");
                jsonObject.remove(key);
                config.setContent(jsonObject.toJSONString());
                configService.save(config);
            }
        }
    }
}
