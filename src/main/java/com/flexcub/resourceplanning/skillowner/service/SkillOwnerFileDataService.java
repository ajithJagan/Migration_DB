package com.flexcub.resourceplanning.skillowner.service;

import com.flexcub.resourceplanning.skillowner.dto.FileResp;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SkillOwnerFileDataService {
    FileResp checkFileTypeAndUpload(List<MultipartFile> excelDataFileList, String id);

    String readExcelFile(int id, boolean devInsert);

}
