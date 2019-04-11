package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoodsType;
import cn.offway.hades.repository.PhGoodsTypeRepository;
import cn.offway.hades.service.PhGoodsTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * 商品类别Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsTypeServiceImpl implements PhGoodsTypeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsTypeRepository phGoodsTypeRepository;

    @Override
    public PhGoodsType save(PhGoodsType phGoodsType) {
        return phGoodsTypeRepository.save(phGoodsType);
    }

    @Override
    public Page<PhGoodsType> findAll(Pageable pageable) {
        return phGoodsTypeRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phGoodsTypeRepository.delete(id);
    }

    @Override
    public PhGoodsType findOne(Long id) {
        return phGoodsTypeRepository.findOne(id);
    }
}
