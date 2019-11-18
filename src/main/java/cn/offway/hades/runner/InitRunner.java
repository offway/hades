package cn.offway.hades.runner;

import cn.offway.hades.domain.*;
import cn.offway.hades.service.*;
import cn.offway.hades.singleton.JobHolder;
import cn.offway.hades.utils.MathUtils;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private PhUserInfoService userInfoService;
    @Autowired
    private PhAccumulatePointsService accumulatePointsService;
    private static Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);
    private static final String LOG_TEXT_GOODS = "定时任务：商品{0}价格从{1}改到{2}";
    private static final String LOG_TEXT_STOCK = "定时任务：库存{0}价格从{1}改到{2}";

    @Override
    public void run(ApplicationArguments applicationArguments) {
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
                if (!isValidateTime(sTimeStr) || !isValidateTime(eTimeStr)) {
                    removeTaskFormDB(key, configService);
                    continue;
                }
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
                createJob(taskList, key, sTime, eTime, now, goodsService, stockService, orderInfoService, configService, userInfoService, accumulatePointsService);
            }
        }
    }

    private boolean isValidateTime(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setLenient(false);
        try {
            formatter.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static void createJob(JSONArray taskList, String key, Date sTime, Date eTime, Date now) {
        long delaySeconds = sTime.getTime() - now.getTime();
        long delaySecondsReverse = eTime.getTime() - now.getTime();
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            clientSocket = new Socket("127.0.0.1", 9501);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "add");
            JSONObject dataObj = new JSONObject();
            dataObj.put("key", key);
            dataObj.put("value", taskList);
            jsonObject.put("data", dataObj);
            out.println(jsonObject.toJSONString());
            logger.info(MessageFormat.format("server echo is:{0}", in.readLine()));
        } catch (IOException e) {
            logger.error("socket open error", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                logger.error("socket close error", e);
            }
        }

    }

    public static void createJob(JSONArray taskList, String key, Date sTime, Date eTime, Date now, PhGoodsService goodsService, PhGoodsStockService stockService, PhOrderInfoService orderInfoService, PhConfigService configService, PhUserInfoService userInfoService, PhAccumulatePointsService accumulatePointsService) {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(taskList.size(), factory);
        ExecutorService poolNow = Executors.newFixedThreadPool(taskList.size(), factory);
        ScheduledExecutorService poolReverse = Executors.newScheduledThreadPool(taskList.size(), factory);
        for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
            //calc the delay in seconds
            long delaySeconds = sTime.getTime() - now.getTime();
            Callable<Object> callable = new Callable<Object>() {
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
                                double tmp = goods.getPrice();
                                goods.setPrice(tmp * discount);
                                //update stock
                                List<Double> priceList = new ArrayList<>();
                                for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                    double tmpInner = stock.getPrice();
                                    stock.setPrice(tmpInner * discount);
                                    PhGoodsStock stockSaved = stockService.save(stock);
                                    logger.info(MessageFormat.format(LOG_TEXT_STOCK, stock.getId(), tmpInner, stock.getPrice()));
                                    priceList.add(stockSaved.getPrice());
                                }
                                goods.setPriceRange(genPriceRange(priceList));
                                goodsService.save(goods);
                                logger.info(MessageFormat.format(LOG_TEXT_GOODS, goods.getId(), tmp, goods.getPrice()));
                            }
                            break;
                        case "goodsPrice":
                            double goodsPrice = task.getDoubleValue("goodsPrice");
                            double goodsPriceOriginal = task.getDoubleValue("goodsPriceOriginal");
                            goods = goodsService.findOne(id);
                            if (goods != null) {
                                double tmp = goods.getPrice();
                                goods.setPrice(goodsPrice);
                                goods.setOriginalPrice(goodsPriceOriginal);
                                logger.info("setPrice to " + goodsPrice);
                                logger.info("setOriginalPrice to " + goodsPriceOriginal);
                                logger.info("now is " + new Date());
                                goodsService.save(goods);
                                logger.info(MessageFormat.format(LOG_TEXT_GOODS, goods.getId(), tmp, goods.getPrice()));
                                //re-calc priceRange 3 seconds later
                                calcPriceRange(id, stockService, goodsService);
                            }
                            break;
                        case "stockPrice":
                            double stockPrice = task.getDoubleValue("stockPrice");
                            goodsStock = stockService.findOne(id);
                            if (goodsStock != null) {
                                double tmp = goodsStock.getPrice();
                                goodsStock.setPrice(stockPrice);
                                stockService.save(goodsStock);
                                logger.info(MessageFormat.format(LOG_TEXT_STOCK, goodsStock.getId(), tmp, goodsStock.getPrice()));
                            }
                            break;
                        case "confirmPackage":
                            long oid = task.getLongValue("id");
                            PhOrderInfo orderInfo = orderInfoService.findOne(oid);
                            if (orderInfo != null) {
                                orderInfo.setStatus("3");
                                orderInfoService.save(orderInfo);
                                //增加积分
                                double amount = MathUtils.add(orderInfo.getAmount(), orderInfo.getWalletAmount());
                                Long points = (long) amount;
                                points(orderInfo.getUserId(), "4", points, "确认收货,订单号：" + orderInfo.getOrderNo(), accumulatePointsService, userInfoService);
                            }
                            break;
                    }
                    checkAndPurgeTask(key, configService);
                    return null;
                }
            };
            if (delaySeconds >= 0) {
                pool.schedule(callable, delaySeconds, TimeUnit.MILLISECONDS);
                poolNow.shutdownNow();
            } else {
                poolNow.submit(callable);
                pool.shutdownNow();
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
                                    double tmp = goods.getPrice();
                                    goods.setPrice(tmp / discount);
                                    goods.setOriginalPrice(null);
                                    //update stock
                                    List<Double> priceList = new ArrayList<>();
                                    for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                        double tmpInner = stock.getPrice();
                                        stock.setPrice(tmpInner / discount);
                                        PhGoodsStock stockSaved = stockService.save(stock);
                                        logger.info(MessageFormat.format(LOG_TEXT_STOCK, stock.getId(), tmpInner, stock.getPrice()));
                                        priceList.add(stockSaved.getPrice());
                                    }
                                    goods.setPriceRange(genPriceRange(priceList));
                                    goodsService.save(goods);
                                    logger.info(MessageFormat.format(LOG_TEXT_GOODS, goods.getId(), tmp, goods.getPrice()));
                                }
                                break;
                            case "goodsPrice":
                                double goodsPrice = task.getDoubleValue("goodsPriceOriginal");
                                goods = goodsService.findOne(id);
                                if (goods != null) {
                                    double tmp = goods.getPrice();
                                    goods.setPrice(goodsPrice);
                                    goods.setOriginalPrice(null);
                                    logger.info("setPrice to " + goodsPrice);
                                    logger.info("setOriginalPrice to NULL");
                                    logger.info("now is " + new Date());
                                    goodsService.save(goods);
                                    logger.info(MessageFormat.format(LOG_TEXT_GOODS, goods.getId(), tmp, goods.getPrice()));
                                    //re-calc priceRange 3 seconds later
                                    calcPriceRange(id, stockService, goodsService);
                                }
                                break;
                            case "stockPrice":
                                double stockPrice = task.getDoubleValue("stockPriceOriginal");
                                goodsStock = stockService.findOne(id);
                                if (goodsStock != null) {
                                    double tmp = goodsStock.getPrice();
                                    goodsStock.setPrice(stockPrice);
                                    stockService.save(goodsStock);
                                    logger.info(MessageFormat.format(LOG_TEXT_STOCK, goodsStock.getId(), tmp, goodsStock.getPrice()));
                                }
                                break;
                        }
                        checkAndPurgeTask(key + "_REVERSE", configService);
                        return null;
                    }
                }, delaySecondsReverse, TimeUnit.MILLISECONDS);
            } else {
                poolReverse.shutdownNow();
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

    public static void points(Long userId, String type, Long points, String remark, PhAccumulatePointsService accumulatePointsService, PhUserInfoService userInfoService) {
        PhAccumulatePoints accumulatePoints = new PhAccumulatePoints();
        accumulatePoints.setUserId(userId);
        accumulatePoints.setCreateTime(new Date());
        accumulatePoints.setType(type);
        accumulatePoints.setPoints(points);
        accumulatePoints.setPointsBalace(points);
        accumulatePoints.setStatus("0");
        accumulatePoints.setVersion(0L);
        accumulatePoints.setRemark(remark);
        accumulatePointsService.save(accumulatePoints);
        userInfoService.addPoints(userId, points);
    }

    public static void calcPriceRange(Long gid, PhGoodsStockService stockService, PhGoodsService goodsService) {
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

    public static void checkAndPurgeTask(String key, PhConfigService configService) {
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
