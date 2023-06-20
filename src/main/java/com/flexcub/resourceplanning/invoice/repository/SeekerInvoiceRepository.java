package com.flexcub.resourceplanning.invoice.repository;

import com.flexcub.resourceplanning.invoice.dto.SeekerInvoice;
import com.flexcub.resourceplanning.invoice.entity.SkillSeekerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeekerInvoiceRepository extends JpaRepository<SkillSeekerInvoice, String> {

//    SkillSeekerInvoice findByStatus(int id);

}
