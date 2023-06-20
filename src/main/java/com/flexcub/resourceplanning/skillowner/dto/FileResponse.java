package com.flexcub.resourceplanning.skillowner.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public FileResponse(String fileName, String fileDownloadUri, String fileType, long size, HttpStatus ok) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}