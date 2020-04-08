package cn.offway.hades.service;

import cn.offway.hades.domain.PhProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 活动产品表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhProductInfoService {

    PhProductInfo save(PhProductInfo phProductInfo);

    PhProductInfo findOne(Long id);

    List<PhProductInfo> findByEndTime(Date endTime);

    PhProductInfo saveProduct(PhProductInfo phProductInfo);

    int updateSort(Long productId);

    List<PhProductInfo> findAll(List<Long> ids);

    List<PhProductInfo> save(List<PhProductInfo> phProductInfos);

    Page<PhProductInfo> findByPage(String name, String type, String status, Long channel, Pageable page);

    Page<PhProductInfo> findByType(String type, Long channel, Pageable page);

    boolean notice(Long productId, String video, String img, String codes, String type);

    Long getJoin(Long productId);
}
