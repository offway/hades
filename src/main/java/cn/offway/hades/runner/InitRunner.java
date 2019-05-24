package cn.offway.hades.runner;

import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.service.PhConfigService;
import cn.offway.hades.service.PhGoodsService;
import cn.offway.hades.service.PhGoodsStockService;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
                String[] args = key.split("_");
                String sTimeStr = args[0];
                String eTimeStr = args[1];
                String goodIds = args[2];
                Date sTime = DateTime.parse(sTimeStr, format).toDate();
                Date eTime = DateTime.parse(eTimeStr, format).toDate();
                Date now = new Date();
                if (sTime.compareTo(now) < 0 && eTime.compareTo(now) < 0) {
                    continue;
                }
                if ("".equals(goodIds.trim())) {
                    continue;
                }
                JSONArray taskList = jsonObject.getJSONArray(key);
                createJob(taskList, key, sTime, eTime, now, goodsService, stockService);
            }
        }
    }

    public static void createJob(JSONArray taskList, String key, Date sTime, Date eTime, Date now, PhGoodsService goodsService, PhGoodsStockService stockService) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(taskList.size(), factory);
        ScheduledExecutorService poolReverse = Executors.newScheduledThreadPool(taskList.size(), factory);
        for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
            //calc the delay in seconds
            long delaySeconds = sTime.getTime() - now.getTime();
            if (delaySeconds > 0) {
                pool.schedule(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        long gid = task.getLongValue("gid");
                        double discount = task.getDoubleValue("discount");
                        PhGoods goods = goodsService.findOne(gid);
                        if (goods != null) {
                            //update goods
                            goods.setOriginalPrice(goods.getPrice());
                            goods.setPrice(goods.getOriginalPrice() * discount);
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
                        checkAndPurgeTask(key);
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
                        long gid = task.getLongValue("gid");
                        double discount = task.getDoubleValue("discount");
                        PhGoods goods = goodsService.findOne(gid);
                        if (goods != null) {
                            //update goods
                            goods.setPrice(goods.getOriginalPrice() / discount);
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
                        checkAndPurgeTask(key + "_REVERSE");
                        return null;
                    }
                }, delaySecondsReverse, TimeUnit.MILLISECONDS);
            }
        }
        //link to global register
        JobHolder.getHolder().put(key, pool);
        JobHolder.getHolder().put(key + "_REVERSE", poolReverse);
    }

    private static String genPriceRange(List<Double> priceList) {
        Collections.sort(priceList);
        Double lowest = priceList.get(0);
        Double highest = priceList.get(priceList.size() - 1);
        if (Double.compare(lowest, highest) == 0) {
            return String.format("%.2f", lowest);
        } else {
            return String.format("%.2f-%.2f", lowest, highest);
        }
    }

    private static void checkAndPurgeTask(String key) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (JobHolder.getHolder().containsKey(key)) {
                        ExecutorService service = JobHolder.getHolder().get(key);
                        if (service.isShutdown() || service.isTerminated()) {
                            JobHolder.getHolder().remove(key);
                        } else {
                            service.shutdown();
                            if (service.awaitTermination(5, TimeUnit.SECONDS)) {
                                JobHolder.getHolder().remove(key);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
