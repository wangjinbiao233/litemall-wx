package org.linlinjava.litemall.admin.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.admin.util.QRCodeUtil;
import org.linlinjava.litemall.db.domain.LitemallCustomDiscount;
import org.linlinjava.litemall.db.domain.LitemallDiscount;
import org.linlinjava.litemall.db.domain.LitemallStore;
import org.linlinjava.litemall.db.service.LitemallDiscountService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/admin/discount")
public class LitemallDiscountController {

    private final Log logger = LogFactory.getLog(LitemallDiscountController.class);

    @Autowired
    LitemallDiscountService litemallDiscountService;

    @PostMapping("/selectDiscountList")
    public Object selectDiscountList(@LoginAdmin Integer adminId,
                                     String discountName,Integer discountType,
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        Map<String, Object> map = null;
        try {
        	LitemallCustomDiscount discount = new LitemallCustomDiscount();
        	if(StringUtils.isNotBlank(discountName))discount.setDiscountName(discountName);
        	if(discountType!=null)discount.setDiscountType(discountType);        	
            map = litemallDiscountService.selectByDiscountNameGroypBy(discount, page, limit);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.fail();
        }
        return ResponseUtil.ok(map);
    }

    @PostMapping("/createDiscount")
    public Object createDiscount(@LoginAdmin Integer adminId,LitemallCustomDiscount litemallDiscount){
        if(adminId == null){
            return ResponseUtil.fail401();
        }

        LitemallDiscount discount = new LitemallDiscount();
        discount.setKey(UUID.randomUUID().toString());
        discount.setDiscountName(litemallDiscount.getDiscountName());
        discount.setDiscountType(litemallDiscount.getDiscountType());
        discount.setDiscountsPrice(litemallDiscount.getDiscountsPrice());
        discount.setLimitPrice(litemallDiscount.getLimitPrice());
        discount.setStartTime(LocalDate.parse(litemallDiscount.getStartTimeStr()));
        discount.setEndTime(LocalDate.parse(litemallDiscount.getEndTimeStr()));
        discount.setCreateUserId(adminId+"");
        discount.setCreateTime(LocalDateTime.now());

        int n = Integer.parseInt(litemallDiscount.getDiscountCount());
        for (int i = 0;i < n;i++ ){
            litemallDiscountService.insertSelective(discount);
        }

        return ResponseUtil.ok();
    }
    
    @PostMapping("/updateDiscount")
    public Object updateDiscount(@LoginAdmin Integer adminId,LitemallCustomDiscount litemallDiscount){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        LitemallDiscount discount = new LitemallDiscount();
        discount.setId(litemallDiscount.getId());
        discount.setKey(litemallDiscount.getKey());
        discount.setStartTime(LocalDate.parse(litemallDiscount.getStartTimeStr()));
        discount.setEndTime(LocalDate.parse(litemallDiscount.getEndTimeStr()));
        //discount.setIsUse(1);
        litemallDiscount.setStartTime(LocalDate.parse(litemallDiscount.getStartTimeStr()));
        litemallDiscount.setEndTime(LocalDate.parse(litemallDiscount.getEndTimeStr()));
        litemallDiscountService.updateSelective(litemallDiscount);

        return ResponseUtil.ok();
    }
    
    @RequestMapping({"/downloadCodeImg"})  
	public void downloadCodeImg(HttpServletResponse response,
			String id,String keys) throws WriterException, IOException{
    	System.out.println("------------------ id =="+id+"------------keys =="+keys);
    	QRCodeUtil.zxingCodeCreate(keys, 300, 300, "E://zxingcode.jpg", "jpg");
    	
    	response.setHeader("Content-Type","application/octet-stream");
    	response.setHeader("Content-Disposition","attachment;filename="+keys+".jpg");
    	OutputStream outputStream = response.getOutputStream();
    	QRCodeUtil.zxingCodeCreateStream(keys+"_"+id, 300, 300, outputStream, "jpg");
    	outputStream.flush();
    	outputStream.close();
    }

}
