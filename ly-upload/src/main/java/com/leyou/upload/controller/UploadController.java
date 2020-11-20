package com.leyou.upload.controller;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.upload.service.UpLoadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * date:2020-06-16
 * author:zhangxiaoshuai
 */
@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UpLoadService upLoadService;


    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> UploadImage(@RequestParam("file")MultipartFile file){

       String url= this.upLoadService.upload(file);
       if (StringUtils.isBlank(url)){
           throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
       }
       return ResponseEntity.ok(url);
    }

}
