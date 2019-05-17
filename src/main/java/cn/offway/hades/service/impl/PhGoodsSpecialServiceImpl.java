package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhGoodsSpecial;
import cn.offway.hades.repository.PhGoodsSpecialRepository;
import cn.offway.hades.service.PhGoodsSpecialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * 特殊商品Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhGoodsSpecialServiceImpl implements PhGoodsSpecialService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhGoodsSpecialRepository phGoodsSpecialRepository;

    @Override
    public PhGoodsSpecial save(PhGoodsSpecial phGoodsSpecial) {
        return phGoodsSpecialRepository.save(phGoodsSpecial);
    }

    @Override
    public boolean isSpecial(Long gid) {
        Optional<String> res = phGoodsSpecialRepository.getCount(gid);
        if (res.isPresent()) {
            return Long.compare(Long.valueOf(String.valueOf(res.get())), 0) > 0;
        } else {
            return false;
        }
    }

    @Override
    public List<PhGoodsSpecial> findAll() {
        return phGoodsSpecialRepository.findAll();
    }

    @Override
    public void delete(Long gid) {
        phGoodsSpecialRepository.deleteByGid(gid);
    }

    @Override
    public PhGoodsSpecial findOne(Long id) {
        return phGoodsSpecialRepository.findOne(id);
    }
}
