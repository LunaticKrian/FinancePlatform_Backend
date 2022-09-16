package com.krian.finance.service;

import java.io.InputStream;

public interface FileService {

    // 上传文件到阿里云：
    String upload(InputStream inputStream, String module, String fileName);

    void removeFile(String url);
}
