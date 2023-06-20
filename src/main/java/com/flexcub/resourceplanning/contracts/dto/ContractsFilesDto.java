package com.flexcub.resourceplanning.contracts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContractsFilesDto {

    private int id;
    private String fileName;
    private String mimeType;
    private long size;
    private int fileVersion;
    @JsonIgnoreProperties
    private byte[] data;
}
