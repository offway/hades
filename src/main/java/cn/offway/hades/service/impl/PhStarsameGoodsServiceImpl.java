package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhStarsameGoodsService;

import cn.offway.hades.domain.PhStarsameGoods;
import cn.offway.hades.repository.PhStarsameGoodsRepository;


/**
 * 明星同款商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhStarsameGoodsServiceImpl implements PhStarsameGoodsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhStarsameGoodsRepository phStarsameGoodsRepository;

    @Override
    public PhStarsameGoods save(PhStarsameGoods phStarsameGoods) {
        return phStarsameGoodsRepository.save(phStarsameGoods);
    }

    @Override
    public Boolean deleteByPid(Long pid) {
        return phStarsameGoodsRepository.deleteByPid(pid);
    }

    @Override
    public PhStarsameGoods findOne(Long id) {
        return phStarsameGoodsRepository.findOne(id);
    }
}
