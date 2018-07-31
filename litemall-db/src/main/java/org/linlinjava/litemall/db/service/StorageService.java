package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface StorageService {

    LitemallStorage store(MultipartFile file);

    LitemallStorage store(MultipartFile file,String imgBelongs);

    LitemallStorage store(InputStream inputStream, String fileName , String imgType);


    Path load(String filename);


    void deleteById(Integer fileId);
}