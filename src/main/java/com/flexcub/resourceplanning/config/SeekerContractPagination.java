package com.flexcub.resourceplanning.config;

import com.flexcub.resourceplanning.contracts.dto.SeekerMSADetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Configuration
public class SeekerContractPagination {
    public PageImpl<SeekerMSADetails> getSeekerContractDetails(int page, int size, List<SeekerMSADetails> seekerMSADetails) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, seekerMSADetails.size());
        List<SeekerMSADetails> pageDTO = seekerMSADetails.subList(startIndex, endIndex);
        return new PageImpl<>(pageDTO, pageRequest, seekerMSADetails.size());
    }
}
