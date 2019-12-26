package cn.offway.hades.service.impl;

import cn.offway.hades.domain.PhNotice;
import cn.offway.hades.repository.PhNoticeRepository;
import cn.offway.hades.service.PhNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 消息通知Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhNoticeServiceImpl implements PhNoticeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhNoticeRepository phNoticeRepository;

    @Override
    public PhNotice save(PhNotice phNotice) {
        return phNoticeRepository.save(phNotice);
    }

    @Override
    public List<PhNotice> save(List<PhNotice> phNotices) {
        return phNoticeRepository.save(phNotices);
    }

    @Override
    public Page<PhNotice> findAll(Pageable pageable) {
        return phNoticeRepository.findAll(pageable);
    }

    @Override
    public void del(Long id) {
        phNoticeRepository.delete(id);
    }

    @Override
    public PhNotice findOne(Long id) {
        return phNoticeRepository.findOne(id);
    }
}
