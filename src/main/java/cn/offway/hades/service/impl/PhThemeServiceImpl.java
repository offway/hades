package cn.offway.hades.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.hades.service.PhThemeService;

import cn.offway.hades.domain.PhTheme;
import cn.offway.hades.repository.PhThemeRepository;


/**
 * 主题列表Service接口实现
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-03 14:49:20 Exp $
 */
@Service
public class PhThemeServiceImpl implements PhThemeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhThemeRepository phThemeRepository;
	
	@Override
	public PhTheme save(PhTheme phTheme){
		return phThemeRepository.save(phTheme);
	}
	
	@Override
	public PhTheme findOne(Long id){
		return phThemeRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phThemeRepository.delete(id);
	}

	@Override
	public List<PhTheme> save(List<PhTheme> entities){
		return phThemeRepository.save(entities);
	}
}
