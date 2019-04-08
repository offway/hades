package cn.offway.hades.service;

import cn.offway.hades.domain.PhStarsameImage;

/**
 * 明星同款banner图片Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhStarsameImageService {

    PhStarsameImage save(PhStarsameImage phStarsameImage);

    PhStarsameImage findOne(Long id);

    void deleteByPid(Long pid);
}
