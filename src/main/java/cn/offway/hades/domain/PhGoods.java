package cn.offway.hades.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 商品表
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
@Entity
@Table(name = "ph_goods")
public class PhGoods extends BaseRowModel implements Serializable {

    /** ID **/
    @ExcelProperty(value = "序号",index = 0)
    private Long id;

    /** 名称 **/
    @ExcelProperty(value = "名称",index = 1)
    private String name;

    /** 封面图片 **/
    @ExcelProperty(value = "封面图片",index = 2)
    private String image;

    /** 商户ID **/
    @ExcelProperty(value = "商户ID",index = 3)
    private Long merchantId;

    /** 商户LOGO **/
    @ExcelProperty(value = "商户LOGO",index = 4)
    private String merchantLogo;

    /** 商户名称 **/
    @ExcelProperty(value = "商户名称",index = 5)
    private String merchantName;

    /** 商户类型[0-品牌商,1-买手店] **/
    private String merchantType;

    /** 品牌ID **/
    @ExcelProperty(value = "品牌ID",index = 6)
    private Long brandId;

    /** 品牌名称 **/
    @ExcelProperty(value = "品牌名称",index = 7)
    private String brandName;

    /** 品牌LOGO **/
    @ExcelProperty(value = "品牌LOGO",index = 8)
    private String brandLogo;

    /** 类别 **/
    @ExcelProperty(value = "类别",index = 9)
    private String type;

    /** 类目 **/
    @ExcelProperty(value = "类目",index = 10)
    private String category;

    /** 发售价 **/
    @ExcelProperty(value = "发售价",index = 11)
    private Double price;

    /** 原价 **/
    @ExcelProperty(value = "原价",index = 12)
    private Double originalPrice;

    /** 价格范围[添加库存的时候更新] **/
    @ExcelProperty(value = "价格范围",index = 13)
    private String priceRange;

    /** 材质 **/
    @ExcelProperty(value = "材质",index = 14)
    private String material;

    /** 状态[0-未上架,1-已上架] **/
    @ExcelProperty(value = "状态",index = 15)
    private String status;

    /** 浏览数 **/
    @ExcelProperty(value = "浏览数",index = 16)
    private Long viewCount;

    /** 销量 **/
    @ExcelProperty(value = "销量",index = 17)
    private Long saleCount;

    /** 创建时间 **/
    @ExcelProperty(value = "创建时间",index = 18)
    private Date createTime;

    /** 备注 **/
    @ExcelProperty(value = "备注",index = 19)
    private String remark;

    /** 货号 **/
    @ExcelProperty(value = "货号",index = 20)
    private String code;

    /** 上架时间 **/
    private Date upTime;

    /** 标签,该字段为二进制位运算标识,0否1是,从右到左第一位表示品牌保障,第二位表示7天退换货,第三位表示限量商品,第四位表示特殊商品。 **/
    private Long tag;

    /** 风格 **/
    private String style;

    /**商品标示[0-普通商品,1-限量商品]**/
    private String label;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image", length = 100)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "merchant_id", length = 11)
    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    @Column(name = "merchant_logo", length = 100)
    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    @Column(name = "merchant_name", length = 100)
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    @Column(name = "brand_id", length = 11)
    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Column(name = "brand_name", length = 50)
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Column(name = "brand_logo", length = 200)
    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    @Column(name = "type", length = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "category", length = 20)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "price", precision = 15, scale = 2)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "original_price", precision = 15, scale = 2)
    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Column(name = "price_range", length = 50)
    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    @Column(name = "material", length = 20)
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Column(name = "status", length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "view_count", length = 11)
    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    @Column(name = "sale_count", length = 11)
    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "remark", length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "code", length = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "up_time")
    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    @Column(name = "merchant_type", length = 2)
    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    @Column(name = "tag", length = 1)
    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    @Column(name = "style", length = 20)
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Column(name = "label", length = 2)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
