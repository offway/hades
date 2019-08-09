package cn.offway.hades.service.impl;

import java.util.List;

import cn.offway.hades.domain.PhOrderGoods;
import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.domain.PhRefundGoods;
import cn.offway.hades.service.PhOrderGoodsService;
import cn.offway.hades.service.PhRefundGoodsService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhRefundOrderGoodsService;

import cn.offway.hades.domain.PhRefundOrderGoods;
import cn.offway.hades.repository.PhRefundOrderGoodsRepository;
import org.springframework.util.CollectionUtils;


/**
 * 退换货后的订单商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-06 15:46:21 Exp $
 */
@Service
public class PhRefundOrderGoodsServiceImpl implements PhRefundOrderGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhRefundOrderGoodsRepository phRefundOrderGoodsRepository;
    @Autowired
    private PhOrderGoodsService orderGoodsService;
    @Autowired
    private PhRefundGoodsService refundGoodsService;

    @Override
    public PhRefundOrderGoods save(PhRefundOrderGoods phRefundOrderGoods) {
        return phRefundOrderGoodsRepository.save(phRefundOrderGoods);
    }

    @Override
    public PhRefundOrderGoods findOne(Long id) {
        return phRefundOrderGoodsRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phRefundOrderGoodsRepository.delete(id);
    }

    @Override
    public List<PhRefundOrderGoods> save(List<PhRefundOrderGoods> entities) {
        return phRefundOrderGoodsRepository.save(entities);
    }

    @Override
    public void update(PhRefund refund, String orderNo, String type) {
        List<PhRefundOrderGoods> refundOrderGoods = phRefundOrderGoodsRepository.findByOrderNo(orderNo);
        if (CollectionUtils.isEmpty(refundOrderGoods)) {
            List<PhOrderGoods> orderGoods = orderGoodsService.findAllByPid(orderNo);
            if (type.equals("2")) {
                if (refund.getIsComplete().equals("0")) {
                    List<PhRefundGoods> phRefundGoods = refundGoodsService.listByPid(refund.getId());
                    for (PhOrderGoods orderGood : orderGoods) {
                        for (PhRefundGoods phRefundGood : phRefundGoods) {
                            if (phRefundGood.getOrderGoodsId().longValue() == orderGood.getId().longValue()) {
                                PhRefundOrderGoods refundOrderGoods1 = new PhRefundOrderGoods();
                                BeanUtils.copyProperties(orderGood, refundOrderGoods1);
                                Long goodsCount = refundOrderGoods1.getGoodsCount() - phRefundGood.getGoodsCount();
                                if (goodsCount > 0) {
                                    refundOrderGoods1.setGoodsCount(goodsCount);
                                    refundOrderGoods.add(refundOrderGoods1);
                                    PhRefundOrderGoods refundOrderGoods2 = refundOrderGoods1;
                                    refundOrderGoods2.setGoodsCount(phRefundGood.getGoodsCount());
                                    refundOrderGoods2.setGoodsStockId(phRefundGood.getToStockId());
                                    refundOrderGoods2.setGoodsImage(phRefundGood.getToStockImage());
                                    refundOrderGoods.add(refundOrderGoods2);
                                }
                            }
                        }
                    }
                }
            } else {
                if (refund.getIsComplete().equals("0")) {
                    List<PhRefundGoods> phRefundGoods = refundGoodsService.listByPid(refund.getId());
                    for (PhOrderGoods orderGood : orderGoods) {
                        for (PhRefundGoods phRefundGood : phRefundGoods) {
                            if (phRefundGood.getOrderGoodsId().longValue() == orderGood.getId().longValue()) {
                                PhRefundOrderGoods refundOrderGoods1 = new PhRefundOrderGoods();
                                BeanUtils.copyProperties(orderGood, refundOrderGoods1);
                                Long goodsCount = refundOrderGoods1.getGoodsCount().longValue() - phRefundGood.getGoodsCount().longValue();
                                if (goodsCount > 0) {
                                    refundOrderGoods1.setGoodsCount(goodsCount);
                                    refundOrderGoods.add(refundOrderGoods1);
                                }
                            }
                        }
                    }
                }
            }
        } else {

            if (type.equals("2")) {
                if (refund.getIsComplete().equals("0")) {
                    List<PhRefundGoods> phRefundGoods = refundGoodsService.listByPid(refund.getId());
                    for (PhRefundOrderGoods orderGood : refundOrderGoods) {
                        for (PhRefundGoods phRefundGood : phRefundGoods) {
                            if (phRefundGood.getOrderGoodsId().longValue() == orderGood.getId().longValue()) {
                                Long goodsCount = orderGood.getGoodsCount().longValue() - phRefundGood.getGoodsCount().longValue();
                                if (goodsCount > 0) {
                                    orderGood.setGoodsCount(goodsCount);
                                    PhRefundOrderGoods refundOrderGoods2 = orderGood;
                                    refundOrderGoods2.setGoodsCount(phRefundGood.getGoodsCount());
                                    refundOrderGoods2.setGoodsStockId(phRefundGood.getToStockId());
                                    refundOrderGoods2.setGoodsImage(phRefundGood.getToStockImage());
                                    refundOrderGoods.add(refundOrderGoods2);
                                }
                            }
                        }
                    }
                }
            } else {
                if (refund.getIsComplete().equals("0")) {
                    List<PhRefundGoods> phRefundGoods = refundGoodsService.listByPid(refund.getId());
                    for (PhRefundOrderGoods orderGood : refundOrderGoods) {
                        for (PhRefundGoods phRefundGood : phRefundGoods) {
                            if (phRefundGood.getOrderGoodsId().longValue() == orderGood.getId().longValue()) {
                                Long goodsCount = orderGood.getGoodsCount().longValue() - phRefundGood.getGoodsCount().longValue();
                                if (goodsCount > 0) {
                                    orderGood.setGoodsCount(goodsCount);
                                } else {
                                    refundOrderGoods.remove(orderGood);

                                }
                            }
                        }
                    }
                }
            }
        }
        save(refundOrderGoods);
    }

}
