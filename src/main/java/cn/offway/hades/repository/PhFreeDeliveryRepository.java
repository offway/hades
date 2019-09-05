package cn.offway.hades.repository;

        import cn.offway.hades.domain.PhFreeDelivery;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

/**
 * 免费送Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-08-14 10:53:56 Exp $
 */
public interface PhFreeDeliveryRepository extends JpaRepository<PhFreeDelivery, Long>, JpaSpecificationExecutor<PhFreeDelivery> {

    /**
     * 此处写一些自定义的方法
     **/
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_free_delivery` WHERE (`product_id` in (?1))")
    void deleteByproductId(List<Long> id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_free_delivery` WHERE `product_id` = ?1 ")
    void deleteBypId(Long id);
}
