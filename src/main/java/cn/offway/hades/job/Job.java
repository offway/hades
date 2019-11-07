package cn.offway.hades.job;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.repository.PhMerchantBrandRepository;
import cn.offway.hades.service.PhBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Job {
    @Autowired
    private PhMerchantBrandRepository merchantBrandRepository;
    @Autowired
    private PhBrandService brandService;

    @Scheduled(cron = "0 0 * * * *")
    public void disableEmptyBrand() {
        List<Object[]> list = merchantBrandRepository.checkAllEmptyBrand();
        for (Object[] item : list) {
            long count = Long.valueOf(String.valueOf(item[0]));
            if (count == 0) {
                long pk = Long.valueOf(String.valueOf(item[1]));
                PhBrand brand = brandService.findOne(pk);
                if (brand != null) {
                    brand.setStatus("0");
                    brandService.save(brand);
                }
            }
        }
    }
}
