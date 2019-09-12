package cn.offway.hades.service;

import cn.offway.hades.domain.PhStarsame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 明星同款Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhStarsameService {

    PhStarsame save(PhStarsame phStarsame);

    List<PhStarsame> getLimitListSortMini();

    PhStarsame findOne(Long id);

    void delete(Long id);

    Page<PhStarsame> findAll(String id, String name, String starName, Pageable pageable);

    void resort(Long sort);

    void resetSort();

    List<PhStarsame> getLimitList();

    void sameSort();
}
