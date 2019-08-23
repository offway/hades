package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhLimitedSale;
import cn.offway.hades.repository.PhLimitedSaleRepository;
import cn.offway.hades.service.PhLimitedSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 限量发售Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhLimitedSaleServiceImpl implements PhLimitedSaleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhLimitedSaleRepository phLimitedSaleRepository;

    @Override
    public PhLimitedSale save(PhLimitedSale phLimitedSale) {
        return phLimitedSaleRepository.save(phLimitedSale);
    }

    @Override
    public Page<PhLimitedSale> list(Pageable pageable) {
        return phLimitedSaleRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phLimitedSaleRepository.delete(id);
    }

    @Override
    public PhLimitedSale findOne(Long id) {
        return phLimitedSaleRepository.findOne(id);
    }

    @Override
    public void issetshow(Long id){
        phLimitedSaleRepository.issetshow0();
        phLimitedSaleRepository.issetshow1(id);
    }

    @Override
    public void isshow(){
        phLimitedSaleRepository.issetshow0();
    }
}
