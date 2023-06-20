package com.flexcub.resourceplanning.contracts.repository;

import com.flexcub.resourceplanning.contracts.dto.SeekerMSADetails;
import com.flexcub.resourceplanning.contracts.entity.ContractFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
public interface ContractFileRepository extends JpaRepository<ContractFiles, Integer> {


    Logger logger = LoggerFactory.getLogger(ContractFileRepository.class);

    @Query(value = "SELECT * FROM sow_po_seeker_10_view;", nativeQuery = true)
    List<Object[]> findByAllViewData();

    default List<SeekerMSADetails> findSeekerMSADetailsByAllViewData() {
        List<Object[]> results = findByAllViewData();
        List<SeekerMSADetails> seekerMSADetailsList = new ArrayList<>();

        for (Object[] result : results) {
            try {
                SeekerMSADetails seekerMSADetails = createSeekerMSADetails(result);
                seekerMSADetailsList.add(seekerMSADetails);
            } catch (NullPointerException e) {
                logger.info("Null pointer found on dates or statusId");
            }
        }

        return seekerMSADetailsList;
    }

    private SeekerMSADetails createSeekerMSADetails(Object[] result) {
        SeekerMSADetails seekerMSADetails = new SeekerMSADetails();
        seekerMSADetails.setSkillSeekerId((Integer) result[17]);
        seekerMSADetails.setBusinessName((String) result[19]);
        seekerMSADetails.setProjectId((Integer) result[15]);
        seekerMSADetails.setProjectName((String) result[24]);
        seekerMSADetails.setJobId((String) result[1]);
        seekerMSADetails.setDepartment((String) result[25]);
        seekerMSADetails.setNoOfResource((Integer) result[6]);
        seekerMSADetails.setPhoneNumber((String) result[23]);
        seekerMSADetails.setEmail((String) result[22]);

        if (Objects.nonNull(result[18]) && Objects.isNull(result[13])) {
            seekerMSADetails.setMsaContractStartDate(((java.sql.Date) result[3]).toLocalDate());
            seekerMSADetails.setMsaContractExpiryDate(((java.sql.Date) result[3]).toLocalDate());
            seekerMSADetails.setStatusId((Integer) result[18]);
        } else if (Objects.nonNull(result[13]) && Objects.isNull(result[5])) {
            seekerMSADetails.setMsaContractStartDate(((java.sql.Date) result[3]).toLocalDate());
            seekerMSADetails.setMsaContractExpiryDate(((java.sql.Date) result[3]).toLocalDate());
            seekerMSADetails.setStatusId((Integer) result[13]);
        } else {
            seekerMSADetails.setMsaContractStartDate(((java.sql.Date) result[3]).toLocalDate());
            seekerMSADetails.setMsaContractExpiryDate(((java.sql.Date) result[4]).toLocalDate());
            seekerMSADetails.setStatusId((Integer) result[5]);
        }

        return seekerMSADetails;
    }


    ContractFiles findById(ContractFiles msaId);
}
