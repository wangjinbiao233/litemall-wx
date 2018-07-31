package org.linlinjava.litemall.os.web;

import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/storage/storage")
public class StorageController {

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
    public Object create(@RequestParam("file") MultipartFile file) {

        LitemallStorage storageInfo = storageService.store(file);

        return ResponseUtil.ok(storageInfo);
    }

    /**
     *
     *
     * @param id
     * @param oldName
     * @return
     */
    @PostMapping("/update")
    public Object update(Integer id,String oldName) {


        LitemallStorage litemallStorage = new LitemallStorage();
        litemallStorage.setId(id);
        litemallStorage.setOldName(oldName);

        int r = litemallStorageService.updateByPrimaryKeySelective(litemallStorage);


        if (r<=0){
            return  ResponseUtil.fail();
        }


        return ResponseUtil.ok();
    }



    /**
     * 删除文件
     *
     * @param id 文件id
     * @return
     */
    @PostMapping("/delete")
    public Object delete(Integer id) {

        storageService.deleteById(id);

        return ResponseUtil.ok();
    }


}
