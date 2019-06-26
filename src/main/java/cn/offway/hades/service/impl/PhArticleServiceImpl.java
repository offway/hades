package cn.offway.hades.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhArticleService;

import cn.offway.hades.domain.PhArticle;
import cn.offway.hades.repository.PhArticleRepository;


/**
 * 文章Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Service
public class PhArticleServiceImpl implements PhArticleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhArticleRepository phArticleRepository;
	
	@Override
	public PhArticle save(PhArticle phArticle){
		return phArticleRepository.save(phArticle);
	}
	
	@Override
	public PhArticle findOne(Long id){
		return phArticleRepository.findOne(id);
	}
}
