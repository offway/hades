package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.domain.PhRefundGoods;
import cn.offway.hades.domain.PhRefundOrderGoods;
import cn.offway.hades.repository.PhRefundOrderGoodsRepository;
import cn.offway.hades.service.PhOrderGoodsService;
import cn.offway.hades.service.PhRefundGoodsService;
import cn.offway.hades.service.PhRefundOrderGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public void updateByRefund(PhRefund phRefund) {
        String orderNo = phRefund.getOrderNo();
        String isComplete = phRefund.getIsComplete();
        String type = phRefund.getType();
        List<PhRefundGoods> phRefundGoodsList = refundGoodsService.listByPid(phRefund.getId());
        if ("0".equals(isComplete)) {
            //部分退换货
            int count = phRefundOrderGoodsRepository.countByOrderNo(orderNo);
            //检查是否发生过退换货
            if (count == 0) {
                //同步订单商品
                phRefundOrderGoodsRepository.insertOrderGoods(orderNo,phRefund.getId());
            }
            for (PhRefundGoods phRefundGoods : phRefundGoodsList) {
                Long orderGoodsId = phRefundGoods.getOrderGoodsId();
                int goodsCount = phRefundGoods.getGoodsCount().intValue();
                //减商品数量
                phRefundOrderGoodsRepository.subGoodsCount(orderNo, phRefundGoods.getFromStockId(), goodsCount);
                if ("2".equals(type)) {
                    //换货
                    //增加换货商品
                    phRefundOrderGoodsRepository.insertGoodsStock(orderGoodsId,phRefund.getId());
                    //修改换货商品
                    phRefundOrderGoodsRepository.updateGoodsStock(orderNo, phRefundGoods.getFromStockId(), goodsCount, phRefundGoods.getToStockId(), phRefundGoods.getToStockImage(), phRefundGoods.getToStockDesc());
                }
            }
            //删掉退光的商品
            //phRefundOrderGoodsRepository.deleteByNoMore(orderNo);
        }
    }

}
