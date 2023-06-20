package com.flexcub.resourceplanning.invoice.service;

import com.flexcub.resourceplanning.invoice.dto.*;
import com.flexcub.resourceplanning.invoice.entity.InvoiceStatus;

import java.util.Date;
import java.util.List;


public interface InvoiceService {

//    InvoiceResponse updateInvoice(InvoiceResponse invoice);

    List<WorkEfforts> getOwnerByPartner(int partnerId, Date startDate, Date endDate);

    InvoiceResponse saveInvoiceByPartner(InvoiceRequest invoiceRequest);

    InvoiceDetailResponse getInvoice(String invoiceId,boolean partnerGenerated);

    List<InvoiceListingData> getInvoices(int partnerId);

    InvoiceResponse updatePartnerInvoiceStatus(String invoiceId, int id,String comments);

    InvoiceResponse updateAdminInvoiceStatus(String invoiceId, int id);

    List<InvoiceStatus> getInvoiceStatus();



    List<AdminInvoice> getAdminInvoiceData(int seekerId, int projectId);

    List<InvoiceDetails> getAllInvoiceDetails();

    List<SeekerInvoice> getAllInvoiceOfSeeker(int id);

    SeekerInvoiceStatus updateSeekerInvoiceStatus(String invoiceId, int statusId,String comments);

    List<InvoiceResponse> saveInvoiceDetailsByAdmin(List<AdminInvoiceRequest> invoiceRequest);

    List<ClientInvoiceDetails> invoiceClientDetails();

    List<InvoiceDetails> getAllInvoiceDetailAdmin();

    List<InvoiceDetails> getAdminInvoiceBySeeker(int seekerId);

    InvoiceUpdate updateInvoiceDetailsByPartner(InvoiceUpdate invoiceUpdateRequest);

    InvoiceUpdate updateInvoiceDetailsByAdmin(InvoiceUpdate invoiceUpdateRequest);
}
