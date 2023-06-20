package com.flexcub.resourceplanning.invoice.repository;

import com.flexcub.resourceplanning.invoice.entity.AdminInvoiceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminInvoiceDataRepository extends JpaRepository<AdminInvoiceData, Integer> {

    @Query(value = "select * from invoice_data where skill_seeker_id=? ORDER BY changed_at DESC", nativeQuery = true)
    List<AdminInvoiceData> findInvoiceBySeekerId(int id);

    @Query(value = "select * from admin_invoice_data where admin_invoice_data=?", nativeQuery = true)
    AdminInvoiceData findByInvoiceId(String invoiceId);




}
