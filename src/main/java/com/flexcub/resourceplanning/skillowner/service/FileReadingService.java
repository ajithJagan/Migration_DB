package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.FileResp;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileReadingService {

    FileResp checkFileTypeAndUpload(List<MultipartFile> excelDataFileList);

    ResponseEntity<Resource> downloadTemplate() throws IOException;
}
