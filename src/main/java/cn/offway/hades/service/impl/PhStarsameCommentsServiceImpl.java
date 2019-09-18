package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhStarsameCommentsService;

import cn.offway.hades.domain.PhStarsameComments;
import cn.offway.hades.repository.PhActivityCommentsRepository;


/**
 * 文章评论Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-18 16:00:10 Exp $
 */
@Service
public class PhStarsameCommentsServiceImpl implements PhStarsameCommentsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhActivityCommentsRepository phActivityCommentsRepository;
	
	@Override
	public PhStarsameComments save(PhStarsameComments phActivityComments){
		return phActivityCommentsRepository.save(phActivityComments);
	}
	
	@Override
	public PhStarsameComments findOne(Long id){
		return phActivityCommentsRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phActivityCommentsRepository.delete(id);
	}

	@Override
	public List<PhStarsameComments> save(List<PhStarsameComments> entities){
		return phActivityCommentsRepository.save(entities);
	}
}
