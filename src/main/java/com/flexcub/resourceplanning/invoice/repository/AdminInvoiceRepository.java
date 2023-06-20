package com.flexcub.resourceplanning.invoice.repository;

import com.flexcub.resourceplanning.invoice.dto.AdminInvoice;
import com.flexcub.resourceplanning.invoice.entity.InvoiceAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import java.util.Optional;

public interface AdminInvoiceRepository extends JpaRepository<InvoiceAdmin, Integer> {

    Optional<InvoiceAdmin> findById(String id);

   @Query(value = "SELECT * FROM admin_invoice WHERE invoice_id=?;", nativeQuery = true)
   Optional<InvoiceAdmin> findByInvoiceId(String invoiceId);

   @Query(value = "SELECT * FROM admin_invoice WHERE invoice_id=? and invoice_status_id=?;", nativeQuery = true)
   Optional<InvoiceAdmin> findByInvoiceStatusId(String invoiceId,Integer invoiceStatusId);

   @Query(value = "select INVOICE_DATE from admin_invoice WHERE invoice_id=? ;", nativeQuery = true)
   LocalDate findByInvoiceDate(String invoiceId);

   @Query(value="SELECT * FROM admin_invoice ORDER BY changed_at DESC", nativeQuery = true )
   List<InvoiceAdmin> findAll();
}

