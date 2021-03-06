package org.linlinjava.litemall.admin.web;

import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.annotation.LoginAdmin;
import org.linlinjava.litemall.db.domain.LitemallLabel;
import org.linlinjava.litemall.db.service.LabelManageService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.entity.AccessToken;
import org.linlinjava.litemall.db.util.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/label")
public class LabelManageController {
    private final Log logger = LogFactory.getLog(LabelManageController.class);
    @Autowired
    private LabelManageService labelManageService;
    @Autowired
    private WeixinUtil weixinUtil;

    @GetMapping("/list")
    public Object list(@LoginAdmin Integer adminId,
                       Integer userId,
                       String labelName,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort){
        if(adminId == null){
            return ResponseUtil.fail401();
        }
        if(userId == null){
            return ResponseUtil.fail();
        }
        LitemallLabel label = new LitemallLabel();
        label.setUserId(userId);
        if(StringUtils.isNotBlank(labelName))
            label.setLabelName(labelName);
        PageInfo<Map> pageInfo = labelManageService.querySelective(label, page, limit, sort);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", pageInfo.getTotal());
        data.put("items", pageInfo.getList());

        return ResponseUtil.ok(data);
    }

    @PostMapping("/create")
    public Object create(@LoginAdmin Integer adminId, @RequestBody LitemallLabel label){
        logger.debug(label);
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        if(label == null){
            return ResponseUtil.fail();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        label.setCreateBy(adminId);
        label.setCreateTime(sdf.format(new Date()));
        label.setIsDelete(0);
        int state = labelManageService.add(label);
        if(state > 0){
            return ResponseUtil.ok(label);
        } else {
            return ResponseUtil.fail("添加失败");
        }
    }

    @PostMapping("/update")
    public Object update(@LoginAdmin Integer adminId, @RequestBody LitemallLabel label){
        logger.debug(label);
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        if(label == null){
            return ResponseUtil.fail();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        label.setModifyBy(adminId);
        label.setModifyTime(sdf.format(new Date()));
        int state = labelManageService.update(label);
        if(state > 0){
            return ResponseUtil.ok(label);
        } else {
            return ResponseUtil.fail("修改失败");
        }
    }

    @PostMapping("/delete")
    public Object delete(@LoginAdmin Integer adminId, @RequestBody LitemallLabel label){
        logger.debug(label);
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        if(label == null){
            return ResponseUtil.fail();
        }
        label.setIsDelete(1);
        int state = labelManageService.update(label);
        if(state > 0){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail("修改失败");
        }
    }

    @PostMapping("/createQrcode")
    public Object createQrcode(@LoginAdmin Integer adminId, @RequestBody LitemallLabel label){
        logger.debug(label);
        if(adminId == null){
            return ResponseUtil.unlogin();
        }
        if(label == null){
            return ResponseUtil.fail();
        }
        if(label.getId() != null){
            LitemallLabel litemallLabel = labelManageService.selectById(label.getId());
            if(litemallLabel != null){
                AccessToken token = weixinUtil.getAccessToken();
                if(token != null){
                    String qrcodeUrl = weixinUtil.getQRcode(token.getToken(), "labelId:"+litemallLabel.getId());
                    if (StringUtils.isNotBlank(qrcodeUrl)) {
                        litemallLabel.setQrcodeUrl(qrcodeUrl);
                        labelManageService.update(litemallLabel);
                    }
                }
            }
        }
        return ResponseUtil.ok();
    }


}
