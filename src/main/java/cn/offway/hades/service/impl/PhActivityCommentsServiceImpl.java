package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhActivityCommentsService;

import cn.offway.hades.domain.PhActivityComments;
import cn.offway.hades.repository.PhActivityCommentsRepository;


/**
 * 文章评论Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-18 16:00:10 Exp $
 */
@Service
public class PhActivityCommentsServiceImpl implements PhActivityCommentsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityCommentsRepository phActivityCommentsRepository;
	
	@Override
	public PhActivityComments save(PhActivityComments phActivityComments){
		return phActivityCommentsRepository.save(phActivityComments);
	}
	
	@Override
	public PhActivityComments findOne(Long id){
		return phActivityCommentsRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phActivityCommentsRepository.delete(id);
	}

	@Override
	public List<PhActivityComments> save(List<PhActivityComments> entities){
		return phActivityCommentsRepository.save(entities);
	}
}
