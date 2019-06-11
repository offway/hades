package cn.offway.hades.controller;

import cn.offway.hades.domain.PhAdmin;
import cn.offway.hades.domain.PhRefund;
import cn.offway.hades.properties.QiniuProperties;
import cn.offway.hades.service.PhRefundGoodsService;
import cn.offway.hades.service.PhRefundService;
import cn.offway.hades.service.PhRoleadminService;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class RefundController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhRoleadminService roleadminService;
    @Autowired
    private PhRefundService refundService;
    @Autowired
    private PhRefundGoodsService refundGoodsService;

    @RequestMapping("/refund.html")
    public String index(ModelMap map, @AuthenticationPrincipal PhAdmin admin) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "refund_index";
    }

    private Date strToDate(String s) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (!"".equals(s)) {
            return DateTime.parse(s, format).toDate();
        } else {
            return null;
        }
    }

    private Map<String, Object> buildResponse(Object sEcho, Object iTotalRecords, Object iTotalDisplayRecords, Object aData) {
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", sEcho);
        map.put("iTotalRecords", iTotalRecords);//数据总条数
        map.put("iTotalDisplayRecords", iTotalDisplayRecords);//显示的条数
        map.put("aData", aData);//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/refund_list")
    public Map<String, Object> getList(HttpServletRequest request, String orderNo, String sTime, String eTime, String userId, String sTimeCheck, String eTimeCheck, String type, String status) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort);
        Page<PhRefund> pages = refundService.list(orderNo, strToDate(sTime), strToDate(eTime), userId, strToDate(sTimeCheck), strToDate(eTimeCheck), type, status, pr);
        int initEcho = sEcho + 1;
        return buildResponse(initEcho, pages.getTotalElements(), pages.getTotalElements(), pages.getContent());
    }

    @RequestMapping("/refund_list_export.html")
    public void exportList(HttpServletResponse response, String orderNo, String sTime, String eTime, String userId, String sTimeCheck, String eTimeCheck, String type, String status) {
        List<PhRefund> all = refundService.all(orderNo, strToDate(sTime), strToDate(eTime), userId, strToDate(sTimeCheck), strToDate(eTimeCheck), type, status);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("utf-8");
            String fileName = new String(("RefundList_" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .getBytes(), StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter writer = new ExcelWriter(null, outputStream, ExcelTypeEnum.XLSX, true, new WriteHandler() {
                @Override
                public void sheet(int i, org.apache.poi.ss.usermodel.Sheet sheet) {
                    //nothing
                }

                @Override
                public void row(int i, Row row) {
                    //nothing
                }

                @Override
                public void cell(int i, Cell cell) {
                    if (cell.getRowIndex() == 0) {
                        return;
                    }
                    switch (i) {
                        case 5:
                            /* 类型[0-仅退款,1-退货退款,2-换货] **/
                            String[] arr = new String[]{"仅退款", "退货退款", "换货"};
                            cell.setCellValue(arr[Integer.valueOf(cell.getStringCellValue())]);
                            break;
                        case 6:
                            /* 状态[0-审核中,1-待退货,2-退货中,3-退款中,4-退款成功,5-退款取消,6-审核失败] **/
                            String[] arr2 = new String[]{"审核中", "待退货", "退货中", "退款中", "退款成功", "退款取消", "审核失败"};
                            cell.setCellValue(arr2[Integer.valueOf(cell.getStringCellValue())]);
                            break;
                        case 12:
                            if ("0".equals(cell.getStringCellValue())) {
                                cell.setCellValue("否");
                            } else if ("1".equals(cell.getStringCellValue())) {
                                cell.setCellValue("是");
                            } else {
                                cell.setCellValue("未知");
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            Sheet sheet = new Sheet(1, 0, PhRefund.class);
            sheet.setSheetName("Refund");
            sheet.setAutoWidth(true);
            writer.write(all, sheet);
            writer.finish();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("IO ERROR", e);
        }
    }
}
