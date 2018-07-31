package org.linlinjava.litemall.db.service;


import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.util.CharUtil;
import org.linlinjava.litemall.db.util.FileUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class FileSystemStorageService implements StorageService {

    @Autowired
    private LitemallStorageService litemallStorageService;

    private static final Path rootLocation = Paths.get("/home/steve/images");

    private String generateUrl(String newPath,String key){
        return FileUploadConfig.FILE_URL+newPath+"/"+key;
    }

    private final String generateKey(){
        String key = null;
        LitemallStorage storageInfo = null;

        do{
            key = CharUtil.getRandomString(20);
            storageInfo = litemallStorageService.findByKey(key);
        }
        while(storageInfo != null);

        return key;
    }

    /**
     *
     *
     * @param inputStream
     * @param imgType 文件类型 如：image
     * @return
     */
    @Override
    public LitemallStorage store(InputStream inputStream,String fileName ,String imgType){
        LitemallStorage storageInfo = new LitemallStorage();
        String originalFilename = fileName;

        String key = generateKey();
        String[] fileSuffix = originalFilename.split("\\.");
        String newFileName = key+"."+fileSuffix[fileSuffix.length-1];

        try {

            //创建目录
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String ymd = sdf.format(new Date());
            String newPath = imgType+"/"+ymd;
            Path path = Files.createDirectories(FileSystemStorageService.rootLocation.resolve(newPath));
            //创建文件
            Files.copy(inputStream, path.resolve(newFileName));


            //添加数据记录
            String url = generateUrl(newPath,newFileName);

            storageInfo.setOldName(originalFilename);
            storageInfo.setNewName(newFileName);
//            storageInfo.setSize(fi);
            storageInfo.setType(imgType);
            storageInfo.setFilePath("/"+newPath+"/"+newFileName);
            storageInfo.setCreateTime(LocalDateTime.now());
            storageInfo.setModifyTime(LocalDateTime.now());
            storageInfo.setKey(key);
            storageInfo.setUrl(url);
//            storageInfo.setImgBelongs(imgBelongs);
            litemallStorageService.insertSelective(storageInfo);

        }
        catch (IOException e) {
            throw new RuntimeException ("Failed to store file " + newFileName, e);
        }

        return storageInfo;
    }

    @Override
    public LitemallStorage store(MultipartFile file,String imgBelongs) {
        LitemallStorage storageInfo = new LitemallStorage();
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String contentType = file.getContentType();
        String key = generateKey();
        String[] fileSuffix = originalFilename.split("\\.");
        String newFileName = key+"."+fileSuffix[fileSuffix.length-1];

        try {

            //创建目录
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String ymd = sdf.format(new Date());
            String newPath = contentType.split("/")[0]+"/"+ymd;
            Path path = Files.createDirectories(FileSystemStorageService.rootLocation.resolve(newPath));
            //创建文件
            Files.copy(inputStream, path.resolve(newFileName));


            //添加数据记录
            String url = generateUrl(newPath,newFileName);

            storageInfo.setOldName(originalFilename);
            storageInfo.setNewName(newFileName);
            storageInfo.setSize((int)file.getSize());
            storageInfo.setType(contentType);
            storageInfo.setFilePath("/"+newPath+"/"+newFileName);
            storageInfo.setCreateTime(LocalDateTime.now());
            storageInfo.setModifyTime(LocalDateTime.now());
            storageInfo.setKey(key);
            storageInfo.setUrl(url);
            storageInfo.setImgBelongs(imgBelongs);
            litemallStorageService.insertSelective(storageInfo);

        }
        catch (IOException e) {
            throw new RuntimeException ("Failed to store file " + newFileName, e);
        }

        return storageInfo;
    }

    /**
     *
     * 创建文件以及添加表记录
     *
     * @param file
     */
    @Override
    public LitemallStorage store(MultipartFile file) {
        return store(file,null);
    }


    /**
     *
     * 获取文件完整的Path
     *
     * @param filePath 带有文件名的path
     * @return
     */
    @Override
    public Path load(String filePath) {
        return FileSystemStorageService.rootLocation.resolve(filePath);
    }

    /**
     * 删除文件以及表记录
     *
     * @param fileId
     */
    @Override
    public void deleteById(Integer fileId) {
        LitemallStorage litemallStorage = litemallStorageService.selectByPrimaryKey(fileId);
        String filePath = litemallStorage.getFilePath().substring(1,litemallStorage.getFilePath().length());
        Path file = load(filePath);
        try {
            Files.delete(file);
            litemallStorageService.deleteByPrimaryKey(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}