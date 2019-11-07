package cn.offway.hades.job;

import cn.offway.hades.domain.PhBrand;
import cn.offway.hades.domain.PhGoods;
import cn.offway.hades.domain.PhMerchantBrand;
import cn.offway.hades.repository.PhMerchantBrandRepository;
import cn.offway.hades.service.PhBrandService;
import cn.offway.hades.service.PhGoodsService;
import cn.offway.hades.service.PhMerchantBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Job {
    @Autowired
    private PhMerchantBrandService merchantBrandService;
    @Autowired
    private PhMerchantBrandRepository merchantBrandRepository;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhBrandService brandService;

    @Scheduled(cron = "0 * * * * *")
    public void disableEmptyBrand() {
        //SELECT count(b.id) as ct,a.brand_id,a.brand_name,a.merchant_id,a.merchant_name,a.id as pk,b.id as fk FROM phweb.ph_merchant_brand a left join phweb.ph_goods b on a.brand_id = b.brand_id group by a.brand_id ,a.merchant_id
        List<Object[]> list = merchantBrandRepository.checkEmptyBrand();
        for (Object[] item : list) {
            long count = Long.valueOf(String.valueOf(item[0]));
            if (count == 0) {
                long pk = Long.valueOf(String.valueOf(item[5]));
                PhMerchantBrand merchantBrand = merchantBrandService.findOne(pk);
                if (merchantBrand != null) {
                    PhBrand brand = brandService.findOne(merchantBrand.getBrandId());
                    brand.setStatus("0");
                    brandService.save(brand);
                    //
                }
            }
        }
    }
}
