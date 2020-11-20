package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * date:2020-06-16
 * author:zhangxiaoshuai
 */
@Service
@Slf4j
public class UpLoadService {
//    自定义允许的图片格式

    private static final List<String> ALLOW_TYPES= Arrays.asList("multipart/form-data","image/png", "image/jpeg","image/jpg");


    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    public String upload(MultipartFile file) {
        try {
        //校验图片
        //文件后缀名校验
        String type=file.getContentType();
        if (!ALLOW_TYPES.contains(type)){
            log.error("上传失败，文件类型不匹配", type);
            throw new LyException(ExceptionEnums.INVALID_FILE_TYPE);
        }
        //校验文件内容
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image==null){
            throw new LyException(ExceptionEnums.INVALID_FILE_TYPE);
        }
        //保存文件
//       将图片上传到fastdfs
//           2.1 获取文件的后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//            2.2 上传
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
//            2.3 返回完整路径
            return "http://image.leyou.com/"+storePath.getFullPath();

        } catch (IOException e) {
            log.error("文件上传失败");
           throw new LyException(ExceptionEnums.UPLOAD_FILE_ERROR);
        }

    }
}
