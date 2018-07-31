package org.linlinjava.litemall.wx.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.service.StorageService;
import org.linlinjava.litemall.db.util.ResponseUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/wx/storage")
public class WxStorageController {
    @Autowired
    private StorageService storageService;
    @Autowired
    private LitemallStorageService litemallStorageService;

    /**
     *
     * 获取文件list
     *
     * @param key
     * @param oldName
     * @param startTime
     * @param endTime
     * @param page
     * @param limit
     *
     * @return
     */
    @GetMapping("/list")
    public Object list(String key,String oldName,String startTime,String endTime,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                       String sort, String order){

        Map<String,Object> storageList = litemallStorageService.selectStorage(key,oldName,startTime,endTime,page,limit);

        return ResponseUtil.ok(storageList);
    }

    /**
     * 添加文件
     *
     * @param file
     * @return
     */
    @PostMapping("/create")
    public Object create(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
    	
    	String imgBelongs = request.getParameter("imgBelongs");
        LitemallStorage storageInfo = storageService.store(file , imgBelongs);

        return ResponseUtil.ok(storageInfo);
    }

    @PostMapping("/uploadPic")
    public Object uploadPic(@RequestParam("file") MultipartFile file) {
        LitemallStorage storageInfo = storageService.store(file);

        return ResponseUtil.ok(storageInfo);
    }


    /**
     * 删除文件
     *
     * @param id 文件id
     * @return
     */
    @PostMapping("/delete")
    public Object delete(String id) {
        storageService.deleteById(Integer.parseInt(id));
        return ResponseUtil.ok();
    }


}
