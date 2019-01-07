package cn.offway.hades.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import cn.offway.hades.config.BitPredicate;
import cn.offway.hades.domain.PhProductInfo;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.repository.PhProductInfoRepository;
import cn.offway.hades.service.JPushService;
import cn.offway.hades.service.PhLotteryTicketService;
import cn.offway.hades.service.PhProductInfoService;
import cn.offway.hades.service.PhWinningRecordService;
import cn.offway.hades.service.QiniuService;
import cn.offway.hades.utils.BitUtil;


/**
 * 活动产品表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
@Service
public class PhProductInfoServiceImpl implements PhProductInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhProductInfoRepository phProductInfoRepository;
	
	@Autowired
	private QiniuService qiniuService;
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private JPushService jPushService;
	
	@Autowired
	private PhWinningRecordService phWinningRecordService;
	
	@Autowired
	private PhLotteryTicketService phLotteryTicketService;
	
	@Value("${ph.file.path}")
	private String FILE_PATH;

	@Override
	public PhProductInfo save(PhProductInfo phProductInfo){
		return phProductInfoRepository.save(phProductInfo);
	}
	
	@Override
	public List<PhProductInfo> save(List<PhProductInfo> phProductInfos){
		return phProductInfoRepository.save(phProductInfos);
	}
	
	
	@Override
	public PhProductInfo saveProduct(PhProductInfo phProductInfo){
		Long productId = phProductInfo.getId();
		boolean isAdd = true;
		phProductInfo.setCreateTime(new Date());
		phProductInfo.setStatus("0");
		if(null!=productId){
			isAdd = false;
			PhProductInfo productInfo = findOne(productId);
			String image = productInfo.getImage();
			if(null!= image &&!image.equals(phProductInfo.getImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(image.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String banner = productInfo.getBanner();
			if(null!= banner &&!banner.equals(phProductInfo.getBanner())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(banner.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String shareImage = productInfo.getShareImage();
			if(null!= shareImage &&!shareImage.equals(phProductInfo.getShareImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(shareImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String showImage = productInfo.getShowImage();
			if(null!= showImage &&!showImage.equals(phProductInfo.getShowImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(showImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			String saveImage = productInfo.getSaveImage();
			if(null!= saveImage &&!saveImage.equals(phProductInfo.getSaveImage())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(saveImage.replace(qiniuProperties.getUrl()+"/", ""));
			}
			
			String thumbnail = productInfo.getThumbnail();
			if(null!= thumbnail &&!thumbnail.equals(phProductInfo.getThumbnail())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(thumbnail.replace(qiniuProperties.getUrl()+"/", ""));
			}
			
			String background = productInfo.getBackground();
			if(null!= background &&!background.equals(phProductInfo.getBackground())){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(background.replace(qiniuProperties.getUrl()+"/", ""));
			}
			
//			String background = productInfo.getBackground();
//			if(null!= background &&!background.equals(phProductInfo.getBackground())){
//				//如果资源变动则删除本地资源
//				File file=new File(FILE_PATH+"/"+StringUtils.substringAfterLast(background, "/"));
//		        if(file.exists()&&file.isFile()){
//		        	file.delete();
//		        }
//			}
			phProductInfo.setCreateTime(productInfo.getCreateTime());
			phProductInfo.setRuleContent(productInfo.getRuleContent());
			phProductInfo.setStatus(productInfo.getStatus());
			phProductInfo.setAppRuleContent(productInfo.getAppRuleContent());
			phProductInfo.setSort(productInfo.getSort());
			if(BitUtil.has(phProductInfo.getChannel().intValue(), BitUtil.APP)){
				if(productInfo.getBeginTime().getTime() != phProductInfo.getBeginTime().getTime()){
					//修改开始时间后更新定时推送
					jPushService.updateScheduleTrigger(String.valueOf(productInfo.getId()), "0", phProductInfo.getBeginTime());
				}
			}
		}
		
		String video = phProductInfo.getVideo();
		if(StringUtils.isBlank(video)){
			phProductInfo.setVideo(null);
		}
		phProductInfo = save(phProductInfo);
		
		if(isAdd){
			//设置抽奖码自增1
			phProductInfoRepository.sequence(phProductInfo.getId());
		}
		
		return phProductInfo;
	}
	
	@Override
	public PhProductInfo findOne(Long id){
		return phProductInfoRepository.findOne(id);
	}
	
	@Override
	public List<PhProductInfo> findAll(List<Long> ids){
		return phProductInfoRepository.findAll(ids);
	}
	
	
	
	@Override
	public List<PhProductInfo> findByEndTime(Date endTime){
		return phProductInfoRepository.findByEndTime(endTime);
	}
	
	@Override
	public int updateSort(Long productId){
		return phProductInfoRepository.updateSort(productId);
	}
	
	@Override
	public Page<PhProductInfo> findByPage(final String name,final String type,final String status,final Long channel, Pageable page){
		return phProductInfoRepository.findAll(new Specification<PhProductInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhProductInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(name)){
					params.add(criteriaBuilder.like(root.get("name"), "%"+name+"%"));
				}
				
				if(StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.equal(root.get("status"), status));
				}
				
				Date now = new Date();
				if(StringUtils.isNotBlank(type)){
					if("0".equals(type)){
						//进行中
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"),now));
						params.add(criteriaBuilder.greaterThan(root.get("endTime"), now));
					}else if("1".equals(type)){
						//未开始
						params.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
					
					}else if("2".equals(type)){
						//已结束
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), now));
					}
				}
				
				
				if(null!=channel){
					params.add(bitand((CriteriaBuilderImpl)criteriaBuilder,root.get("channel"), channel)) ;
				}
				
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				
				return null;
			}
		}, page);
	}
	
	public <Y extends Comparable<? super Y>> Predicate bitand(CriteriaBuilderImpl criteriaBuilder,
			Expression<? extends Y> expression,Y object) {
		return new BitPredicate<Y>( criteriaBuilder, expression, object);
	}
	
	@Override
	public Page<PhProductInfo> findByType(final String type,final Long channel,Pageable page){
		return phProductInfoRepository.findAll(new Specification<PhProductInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhProductInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				//已上架
				params.add(criteriaBuilder.equal(root.get("status"), "1"));

				if(null!=channel){
					params.add(bitand((CriteriaBuilderImpl)criteriaBuilder,root.get("channel"), channel)) ;
				}
				
				Date now = new Date();
				if(StringUtils.isNotBlank(type)){
					if("0".equals(type)){
						//进行中
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"),now));
						params.add(criteriaBuilder.greaterThan(root.get("endTime"), now));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.desc(root.get("beginTime")));
					}else if("1".equals(type)){
						//未开始
						params.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.asc(root.get("beginTime")));
					
					}else if("2".equals(type)){
						//已结束
						params.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime"), now));
						Predicate[] predicates = new Predicate[params.size()];
		                criteriaQuery.where(params.toArray(predicates));
		                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sort")),criteriaBuilder.desc(root.get("endTime")));
					}
					
				}

                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				
				return null;
			}
		}, page);
	}
	
	@Override
	public boolean notice(@PathVariable Long productId,String video,String codes) throws Exception {
		int count = phWinningRecordService.saveWin(productId, Arrays.asList(codes.split("\n")));
		if(count==0){
			return false;
		}
		PhProductInfo phProductInfo = findOne(productId);
		if(StringUtils.isNotBlank(video)){
			String videoOld = phProductInfo.getVideo();
			if(null!= videoOld &&!videoOld.equals(video)){
				//如果资源变动则删除七牛资源
				qiniuService.qiniuDelete(videoOld.replace(qiniuProperties.getUrl()+"/", ""));
			}
			phProductInfo.setVideo(video);
			save(phProductInfo);
		}
		
		Long channel = phProductInfo.getChannel();
		if(BitUtil.has(channel.intValue(), BitUtil.APP)){
			//开奖推送
			Map<String, String> extras = new HashMap<>();
			extras.put("type", "2");//0-H5,1-精选文章,2-活动
			extras.put("id", null);
			extras.put("url", null);
			jPushService.sendPush("开奖通知", "【免费抽"+phProductInfo.getName()+"】活动已开奖，幸运儿是你吗？点击查看>>", extras);
		}
				
		if(BitUtil.has(channel.intValue(), BitUtil.MINI)){
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String token = phLotteryTicketService.getToken();
						phLotteryTicketService.notice(token,phProductInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		return true;
	}
}
