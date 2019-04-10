package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhMerchantFileService;

import cn.offway.hades.domain.PhMerchantFile;
import cn.offway.hades.repository.PhMerchantFileRepository;


/**
 * 商户附件表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhMerchantFileServiceImpl implements PhMerchantFileService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhMerchantFileRepository phMerchantFileRepository;
	
	@Override
	public PhMerchantFile save(PhMerchantFile phMerchantFile){
		return phMerchantFileRepository.save(phMerchantFile);
	}
	
	@Override
	public PhMerchantFile findOne(Long id){
		return phMerchantFileRepository.findOne(id);
	}
}
