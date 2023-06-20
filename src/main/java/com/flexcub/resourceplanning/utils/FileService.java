package com.flexcub.resourceplanning.utils;

import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import com.flexcub.resourceplanning.exceptions.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;

import static com.flexcub.resourceplanning.utils.FlexcubConstants.APPLICATION_VND_PDF;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.DOC;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.DOCX;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.PDF;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.TEXT_DOC;
import static com.flexcub.resourceplanning.utils.FlexcubConstants.TEXT_DOCX;
import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.UNSUPPORTED_FILE_FORMAT;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {
    public static ContractFiles createContract(MultipartFile multipartFile) throws IOException {

        HashMap<String, String> fileTypeList = new HashMap<>();
        fileTypeList.put(PDF, APPLICATION_VND_PDF);
        fileTypeList.put(DOC, TEXT_DOC);
        fileTypeList.put(DOCX, TEXT_DOCX);

        if (!fileTypeList.containsValue(multipartFile.getContentType())) {
            throw new ServiceException(UNSUPPORTED_FILE_FORMAT.getErrorCode(), UNSUPPORTED_FILE_FORMAT.getErrorDesc());

        }
        ContractFiles contractFiles = new ContractFiles();
        contractFiles.setFileName(multipartFile.getOriginalFilename());
        contractFiles.setData(multipartFile.getBytes());
        contractFiles.setMimeType(multipartFile.getContentType());
        contractFiles.setSize(multipartFile.getSize());
        contractFiles.setFileVersion(1);
        return contractFiles;
    }
}
