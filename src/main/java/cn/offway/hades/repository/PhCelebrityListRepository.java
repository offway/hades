package cn.offway.hades.repository;

import cn.offway.hades.domain.PhCelebrityList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 明星信息表Repository接口
 *
 * @author tbw
 * @version $v: 1.0.0, $time:2020-03-02 14:23:35 Exp $
 */
public interface PhCelebrityListRepository extends JpaRepository<PhCelebrityList, Long>, JpaSpecificationExecutor<PhCelebrityList> {
    @Query(nativeQuery = true, value = "SELECT * FROM ph_celebrity_list order by convert(name using gbk) asc limit ?1,10")
    List<Object[]> list(int offset);

    @Query(nativeQuery = true, value = "SELECT * FROM ph_celebrity_list where name like ?1 order by convert(name using gbk) asc limit ?2,10")
    List<Object[]> list(String name, int offset);

    @Deprecated
    @Query(nativeQuery = true, value = "SELECT count(id) as ct FROM ph_celebrity_list")
    Optional<String> countAll();

    @Deprecated
    @Query(nativeQuery = true, value = "SELECT count(id) as ct FROM ph_celebrity_list where name like ?1")
    Optional<String> count(String name);
}
