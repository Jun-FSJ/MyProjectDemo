package com.joon.demo.controller;

import com.joon.demo.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Joon
 * @date : 2023/3/20 10:43
 * @modyified By :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("/upload")
    public String singleFileUpload() throws Exception {
        String host = "https://ocrprod.aktech88.com";
        String path = "/houseCert/getHouseCertInfo";
        String method = "POST";
        String appcode = "b266a9f3da044088a0f634bced3f1332";
        Map<String, String> headers = new HashMap<>(2, 1);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();

        Map<String, String> bodys = new HashMap<String, String>(2, 1);
        // 待上传的文件path
        String filePath = "E:/" + 4 + ".jpg";
        File file = new File(filePath);
        byte[] fileData = Files.readAllBytes(file.toPath());
        bodys.put("imageBase64Str", Base64.getEncoder().encodeToString(fileData));
        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        return EntityUtils.toString(response.getEntity());
    }
}
