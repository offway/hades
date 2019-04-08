package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhStarsameService;

import cn.offway.hades.domain.PhStarsame;
import cn.offway.hades.repository.PhStarsameRepository;


/**
 * 明星同款Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhStarsameServiceImpl implements PhStarsameService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhStarsameRepository phStarsameRepository;

    @Override
    public PhStarsame save(PhStarsame phStarsame) {
        return phStarsameRepository.save(phStarsame);
    }

    @Override
    public void delete(Long id) {
        phStarsameRepository.delete(id);
    }

    @Override
    public Page<PhStarsame> findAll(Pageable pageable) {
        return phStarsameRepository.findAll(pageable);
    }

    @Override
    public PhStarsame findOne(Long id) {
        return phStarsameRepository.findOne(id);
    }
}
