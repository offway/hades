package cn.offway.hades.controller;

import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhGoodsStock;
import cn.offway.hades.domain.PhOrderInfo;
import cn.offway.hades.runner.InitRunner;
import cn.offway.hades.service.*;
import cn.offway.hades.utils.MathUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Controller
@RequestMapping("/rpc")
public class RpcController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
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

    @RequestMapping("/processJob")
    @ResponseBody
    public String processJob(String key, String taskListStr) {
        JSONArray taskList = JSONArray.parseArray(taskListStr);
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        ExecutorService poolNow = Executors.newFixedThreadPool(taskList.size(), factory);
        for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
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
                                goods.setPrice(goods.getPrice() * discount);
                                //update stock
                                List<Double> priceList = new ArrayList<>();
                                for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                    stock.setPrice(stock.getPrice() * discount);
                                    PhGoodsStock stockSaved = stockService.save(stock);
                                    priceList.add(stockSaved.getPrice());
                                }
                                goods.setPriceRange(InitRunner.genPriceRange(priceList));
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
                                InitRunner.calcPriceRange(id, stockService, goodsService);
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
                                //增加积分
                                double amount = MathUtils.add(orderInfo.getAmount(), orderInfo.getWalletAmount());
                                Long points = (long) amount;
                                InitRunner.points(orderInfo.getUserId(), "4", points, "确认收货,订单号：" + orderInfo.getOrderNo(), accumulatePointsService, userInfoService);
                            }
                            break;
                    }
                    InitRunner.checkAndPurgeTask(key, configService);
                    return null;
                }
            };
            poolNow.submit(callable);
        }
        return "OK";
    }

    @RequestMapping("/processJobReverse")
    @ResponseBody
    public String processJobReverse(String key, String taskListStr) {
        JSONArray taskList = JSONArray.parseArray(taskListStr);
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        ExecutorService poolNow = Executors.newFixedThreadPool(taskList.size(), factory);
        for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
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
                                goods.setPrice(goods.getPrice() / discount);
                                goods.setOriginalPrice(null);
                                //update stock
                                List<Double> priceList = new ArrayList<>();
                                for (PhGoodsStock stock : stockService.findByPid(goods.getId())) {
                                    stock.setPrice(stock.getPrice() / discount);
                                    PhGoodsStock stockSaved = stockService.save(stock);
                                    priceList.add(stockSaved.getPrice());
                                }
                                goods.setPriceRange(InitRunner.genPriceRange(priceList));
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
                                InitRunner.calcPriceRange(id, stockService, goodsService);
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
                    InitRunner.checkAndPurgeTask(key + "_REVERSE", configService);
                    return null;
                }
            };
            poolNow.submit(callable);
        }
        return "OK";
    }
}
