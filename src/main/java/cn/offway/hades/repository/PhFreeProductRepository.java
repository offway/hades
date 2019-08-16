package cn.offway.hades.repository;

import cn.offway.hades.domain.PhFreeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 免费送产品表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeProductRepository extends JpaRepository<PhFreeProduct, Long>, JpaSpecificationExecutor<PhFreeProduct> {

    /**
     * 此处写一些自定义的方法
     **/
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_free_product` WHERE (`id` in (?1))")
    void deleteList(List<Long> id);
}
