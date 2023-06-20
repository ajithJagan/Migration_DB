package com.flexcub.resourceplanning.config;

import com.flexcub.resourceplanning.contracts.dto.PartnerContractDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
@Configuration
public class PartnerContractPagination {
       public PageImpl<PartnerContractDetails> getPartnerContractDetails(int page, int size, List<PartnerContractDetails> partnerContractDetailsList) {
            PageRequest pageRequest = PageRequest.of(page, size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, partnerContractDetailsList.size());
            List<PartnerContractDetails> pageDto = partnerContractDetailsList.subList(startIndex, endIndex);
            return new PageImpl<>(pageDto, pageRequest, partnerContractDetailsList.size());
        }
}
