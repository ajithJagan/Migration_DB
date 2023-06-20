package com.flexcub.resourceplanning.invoice.service.impl;

import com.flexcub.resourceplanning.exceptions.ServiceException;
import com.flexcub.resourceplanning.invoice.dto.*;
import com.flexcub.resourceplanning.invoice.entity.*;
import com.flexcub.resourceplanning.invoice.repository.*;
import com.flexcub.resourceplanning.invoice.service.InvoiceService;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.job.repository.SelectionPhaseRepository;
import com.flexcub.resourceplanning.notifications.dto.Notification;
import com.flexcub.resourceplanning.notifications.service.NotificationService;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillowner.entity.SkillOwnerEntity;
import com.flexcub.resourceplanning.skillowner.repository.OwnerTimeSheetRepository;
import com.flexcub.resourceplanning.skillowner.repository.SkillOwnerRepository;
import com.flexcub.resourceplanning.skillpartner.repository.SkillPartnerRepository;
import com.flexcub.resourceplanning.skillseeker.entity.PoEntity;
import com.flexcub.resourceplanning.skillseeker.entity.SkillSeekerProjectEntity;
import com.flexcub.resourceplanning.skillseeker.repository.PoRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerProjectRepository;
import com.flexcub.resourceplanning.skillseeker.repository.SkillSeekerRepository;
import com.flexcub.resourceplanning.utils.NullPropertyName;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.flexcub.resourceplanning.utils.FlexcubErrorCodes.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    InvoiceStatusRepository invoiceStatusRepository;

    @Autowired
    SeekerInvoiceRepository seekerInvoiceRepository;

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceDataRepository invoiceDataRepository;
    @Autowired
    SkillSeekerRepository skillSeekerRepository;

    @Autowired
    OwnerTimeSheetRepository ownerTimeSheetRepository;
    @Autowired
    PoRepository poRepository;

    @Autowired
    SkillSeekerProjectRepository skillSeekerProjectRepository;

    @Autowired
    SkillOwnerRepository skillOwnerRepository;

    @Autowired
    SelectionPhaseRepository selectionPhaseRepository;

    @Autowired
    SkillPartnerRepository skillPartnerRepository;

    @Autowired
    AdminInvoiceRepository adminInvoiceRepository;

    @Autowired
    AdminInvoiceDataRepository adminInvoiceDataRepository;

    @Autowired
    NotificationService notificationService;
    LocalDate currentDate = LocalDate.now();
    Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Override
    @Transactional
    public InvoiceResponse saveInvoiceByPartner(InvoiceRequest invoiceRequest) {
        try {
            Invoice invoice = new Invoice();
            invoice.setSkillPartner(skillPartnerRepository.findById(invoiceRequest.getSkillPartnerId()).get());
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setWeekStartDate(invoiceRequest.getStartDate());
            invoice.setWeekEndDate(invoiceRequest.getEndDate());
            invoice.setInvoiceStatus(invoiceStatusRepository.findById(1).get());
            invoice.setDueDate(invoiceRequest.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            List<InvoiceData> invoiceData = new ArrayList<>();
            InvoiceResponse response = new InvoiceResponse();
            int totalAmount = 0;
            for (PartnerInvoiceResponse invoiceRequestData : invoiceRequest.getPartnerInvoiceResponseList()) {
                InvoiceData data = new InvoiceData();
                data.setOwnerId(skillOwnerRepository.findBySkillOwnerEntityId(invoiceRequestData.getSkillOwnerId()));
                data.setSkillSeeker(skillSeekerRepository.findById(invoiceRequestData.getSkillSeekerId()).get());
                if (invoiceRequestData.getSkillSeekerProjectId() == 0) {
                    data.setSkillSeekerProjectId(null);
                } else {
                    data.setSkillSeekerProjectId(skillSeekerProjectRepository.findById(invoiceRequestData.getSkillSeekerProjectId()).get());
                }
                data.setInvoiceStatus(invoiceStatusRepository.findById(1).get());
                data.setTotalHours(invoiceRequestData.getTotalHours());
                data.setAmount(invoiceRequestData.getAmount());
                double actualAmount = invoiceRequestData.getAmount() * invoice.getSkillPartner().getServiceFeePercentage();
                data.setActualAmount(invoiceRequestData.getAmount() - actualAmount);
                totalAmount = totalAmount + invoiceRequestData.getAmount();
                invoiceData.add(data);

                //updating time sheet
                Optional<List<OwnerTimeSheetEntity>> ownerTimeSheetEntity = ownerTimeSheetRepository.findByStartDateAndEndDateAndOwnerId(invoiceRequest.getStartDate(), invoiceRequest.getEndDate(), invoiceRequestData.getSkillOwnerId());
                if (ownerTimeSheetEntity.isPresent()) {
                    for (OwnerTimeSheetEntity timeSheet : ownerTimeSheetEntity.get()) {
                        timeSheet.setInvoiceGenerated(true);
                        ownerTimeSheetRepository.save(timeSheet);
                        invoice.setTimesheetId(ownerTimeSheetEntity.get().get(0).getTimeSheetId());
                    }
                }
            }
            invoice.setInvoiceData(invoiceData);
            Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);
            Notification notification = new Notification();
            notificationService.partnerInvoiceStatusNotification(invoice, notification);
            response.setInvoiceId(savedInvoice.getId());
            response.setTotalAmount(totalAmount);
            response.setInvoiceStatus(invoice.getInvoiceStatus());

            return response;
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public InvoiceDetailResponse getInvoice(String invoiceId, boolean partnerGenerated) {
        try {
            if (partnerGenerated) {
                Optional<Invoice> invoiceEntity = invoiceRepository.findById(invoiceId);
                if (invoiceEntity.isPresent()) {
                    logger.info("InvoiceServiceImpl || getData || Get Details for particular Invoice Id ");
                    InvoiceDetailResponse invoiceDetailResponse = new InvoiceDetailResponse();
                    invoiceDetailResponse.setSkillPartnerId(invoiceEntity.get().getSkillPartner().getSkillPartnerId());
                    invoiceDetailResponse.setInvoiceDate(Date.from(invoiceEntity.get().getInvoiceDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    invoiceDetailResponse.setStatus(invoiceEntity.get().getInvoiceStatus());
                    invoiceDetailResponse.setStartDate(invoiceEntity.get().getWeekStartDate());
                    invoiceDetailResponse.setComments(invoiceEntity.get().getComments());
                    if (isValidStatus(invoiceEntity.get().getInvoiceStatus().getStatus())) {
                        invoiceDetailResponse.setDueDate(invoiceEntity.get().getDueDate());
                    }
                    List<WorkEfforts> partnerInvoiceResponses = new ArrayList<>();
                    for (InvoiceData invoice : invoiceEntity.get().getInvoiceData()) {
                        WorkEfforts invoiceResponse = new WorkEfforts();
                        invoiceResponse.setSkillOwnerEntityId(invoice.getOwnerId().getSkillOwnerEntityId());
                        if (null != invoice.getOwnerId().getLastName()) {
                            invoiceResponse.setOwnerName(invoice.getOwnerId().getFirstName() + " " + invoice.getOwnerId().getLastName());
                        } else {
                            invoiceResponse.setOwnerName(invoice.getOwnerId().getFirstName());
                        }
                        invoiceResponse.setSkillSeekerEntityId(invoice.getSkillSeeker().getId());
                        invoiceResponse.setClientName(invoice.getSkillSeeker().getSkillSeekerName());
                        if (null != invoice.getSkillSeekerProjectId()) {
                            invoiceResponse.setSkillSeekerProjectEntityId(invoice.getSkillSeekerProjectId().getId());
                            invoiceResponse.setProjectName(invoice.getSkillSeekerProjectId().getTitle());
                        } else {
                            invoiceResponse.setSkillSeekerProjectEntityId(0);
                            invoiceResponse.setProjectName("Default");
                        }
                        Optional<List<SelectionPhase>> ownerId = selectionPhaseRepository.findBySkillOwnerId(invoice.getOwnerId().getSkillOwnerEntityId());
                        for (SelectionPhase selectionPhase : ownerId.get()) {
//                            Optional<PoEntity> poEntity = poRepository.findByOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                            if (selectionPhase.getJob().getSkillSeeker().getId() == invoice.getSkillSeeker().getId()) {
                                invoiceResponse.setRate(selectionPhase.getRate());
//                                invoiceResponse.setRate(poEntity.get().getRateCard());
                            }

                        }

                        invoiceResponse.setTotalHours(invoice.getTotalHours());
                        invoiceResponse.setAmount(invoice.getAmount());
                        invoiceResponse.setActualAmount(invoice.getActualAmount());
                        invoiceResponse.setServiceFeesAmount(invoice.getAmount() * invoice.getOwnerId().getSkillPartnerEntity().getServiceFeePercentage());
                        invoiceResponse.setTimesheetId(invoiceEntity.get().getTimesheetId());

                        invoiceResponse.setTimesheetStartDate(ownerTimeSheetRepository.findByTimeSheetId(invoiceEntity.get().getTimesheetId()).get().getStartDate().toLocalDate());
                        invoiceResponse.setTimesheetEndDate(ownerTimeSheetRepository.findByTimeSheetId(invoiceEntity.get().getTimesheetId()).get().getEndDate().toLocalDate());

                        partnerInvoiceResponses.add(invoiceResponse);
                    }
                    invoiceDetailResponse.setInvoiceData(partnerInvoiceResponses);
                    return invoiceDetailResponse;
                } else {
                    logger.info("InvoiceServiceImpl || getData || Data not found ");
                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
                }
            } else {
                Optional<InvoiceAdmin> invoiceEntity = adminInvoiceRepository.findByInvoiceId(invoiceId);
                if (invoiceEntity.isPresent()) {
                    logger.info("InvoiceServiceImpl || getData || Get Details for particular Invoice Id ");
                    InvoiceDetailResponse invoiceDetailResponse = new InvoiceDetailResponse();
                    invoiceDetailResponse.setSkillPartnerId(invoiceEntity.get().getSkillPartnerEntity().getSkillPartnerId());
                    invoiceDetailResponse.setInvoiceDate(Date.from(invoiceEntity.get().getInvoiceDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    invoiceDetailResponse.setStatus(invoiceEntity.get().getInvoiceStatus());
                    invoiceDetailResponse.setComments(invoiceEntity.get().getComment());
                    if (isValidStatus(invoiceEntity.get().getInvoiceStatus().getStatus())) {
                        invoiceDetailResponse.setDueDate(invoiceEntity.get().getDueDate());
                    }
                    List<WorkEfforts> partnerInvoiceResponses = new ArrayList<>();
                    for (AdminInvoiceData invoice : invoiceEntity.get().getInvoiceData()) {
                        WorkEfforts invoiceResponse = new WorkEfforts();
                        invoiceResponse.setSkillOwnerEntityId(invoice.getOwnerId().getSkillOwnerEntityId());
                        if (null != invoice.getOwnerId().getLastName()) {
                            invoiceResponse.setOwnerName(invoice.getOwnerId().getFirstName() + " " + invoice.getOwnerId().getLastName());
                        } else {
                            invoiceResponse.setOwnerName(invoice.getOwnerId().getFirstName());
                        }
                        invoiceResponse.setSkillSeekerEntityId(invoice.getSkillSeeker().getId());
                        invoiceResponse.setClientName(invoice.getSkillSeeker().getSkillSeekerName());
                        if (null != invoice.getSkillSeekerProjectId()) {
                            invoiceResponse.setSkillSeekerProjectEntityId(invoice.getSkillSeekerProjectId().getId());
                            invoiceResponse.setProjectName(invoice.getSkillSeekerProjectId().getTitle());
                        } else {
                            invoiceResponse.setSkillSeekerProjectEntityId(0);
                            invoiceResponse.setProjectName("Default");
                        }
                        Optional<List<SelectionPhase>> ownerId = selectionPhaseRepository.findBySkillOwnerId(invoice.getOwnerId().getSkillOwnerEntityId());
                        for (SelectionPhase selectionPhase : ownerId.get()) {
//                            Optional<PoEntity> poEntity = poRepository.findByOwnerId(selectionPhase.getSkillOwnerEntity().getSkillOwnerEntityId());
                            if (selectionPhase.getJob().getSkillSeeker().getId() == invoice.getSkillSeeker().getId()) {
                                invoiceResponse.setRate(selectionPhase.getRate());
//                                invoiceResponse.setRate(poEntity.get().getRateCard());
                            }

                        }
                        invoiceResponse.setTotalHours(invoice.getTotalHours());
                        invoiceResponse.setAmount(invoice.getAmount());
                        double actualAmount = invoice.getAmount() * invoice.getOwnerId().getSkillPartnerEntity().getServiceFeePercentage();
                        invoiceResponse.setActualAmount(invoice.getAmount() - actualAmount);
                        invoiceResponse.setServiceFeesAmount(actualAmount);
                        invoiceResponse.setTimesheetId(invoiceEntity.get().getTimesheetId());
                        invoiceResponse.setTimesheetStartDate(ownerTimeSheetRepository.findByTimeSheetId(invoiceEntity.get().getTimesheetId()).get().getStartDate().toLocalDate());
                        invoiceResponse.setTimesheetEndDate(ownerTimeSheetRepository.findByTimeSheetId(invoiceEntity.get().getTimesheetId()).get().getEndDate().toLocalDate());
                        partnerInvoiceResponses.add(invoiceResponse);
                    }
                    invoiceDetailResponse.setInvoiceData(partnerInvoiceResponses);
                    return invoiceDetailResponse;
                } else {
                    logger.info("InvoiceServiceImpl || getData || Data not found ");
                    throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
                }
            }

        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    /**
     * @param partnerId partner id
     * @return
     */
    @Override
    @Transactional
    public List<InvoiceListingData> getInvoices(int partnerId) {
        List<InvoiceListingData> listingData = new ArrayList<>();
        Optional<List<Invoice>> invoiceList = invoiceRepository.findBySkillPartnerId(partnerId);
        if (invoiceList.isPresent()) {
            for (Invoice invoice : invoiceList.get()) {
                InvoiceListingData invoiceListingData = new InvoiceListingData();
                invoiceListingData.setInvoiceId(invoice.getId());
                invoiceListingData.setInvoiceDate(invoice.getInvoiceDate());
                invoiceListingData.setStatus(invoice.getInvoiceStatus());
                invoiceListingData.setSoCount(invoice.getInvoiceData().size());
                invoiceListingData.setWeekStartDate(invoice.getWeekStartDate());
                invoiceListingData.setComments(invoice.getComments());
                if (invoice.getInvoiceStatus().getStatus().equals("Approved") && !invoice.isApprovedStatus()) {
                    LocalDate plusDays = currentDate.plusDays(41);
                    invoiceListingData.setPaymentDueDate(plusDays);
                    invoice.setApprovedStatus(true);
                    invoice.setDueDate(plusDays);
                    invoiceRepository.saveAndFlush(invoice);
                } else if (isValidStatus(invoice.getInvoiceStatus().getStatus())) {
                    invoiceListingData.setPaymentDueDate(invoice.getDueDate());
                }
                LocalDate expired = invoice.getDueDate().plusDays(1);
                if (invoice.getInvoiceStatus().getStatus().equals("Approved") && (expired.isBefore(currentDate) || expired.equals(currentDate))) {
                    invoiceListingData.setStatus(invoiceStatusRepository.findById(5).get());
                    invoice.setInvoiceStatus(invoiceStatusRepository.findById(5).get());
                    invoiceRepository.saveAndFlush(invoice);
                }
                listingData.add(invoiceListingData);
            }

        } else {
            throw new ServiceException(NO_INVOICE_FOUND.getErrorCode(), NO_INVOICE_FOUND.getErrorDesc());
        }

        return listingData;
    }


    @Override
    @Transactional
    public List<WorkEfforts> getOwnerByPartner(int partnerId, Date startDate, Date endDate) {
        List<OwnerTimeSheetEntity> timesheet = ownerTimeSheetRepository.findByPartnerIdAndIdStartDateEndDate(partnerId, startDate, endDate);
        timesheet.sort(Comparator.comparing(OwnerTimeSheetEntity::getTimeSheetId));
        List<WorkEfforts> effortList = new ArrayList<>();

        try {
            if (!timesheet.isEmpty()) {
                for (OwnerTimeSheetEntity ownerTimeSheetEntity : timesheet) {
                    Optional<PoEntity> poEntity = poRepository.findByOwnerId(ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                    if (poEntity.isPresent()) {
                        Optional<SelectionPhase> selectionPhase = selectionPhaseRepository.findByJobIdAndSkillOwnerId(poEntity.get().getJobId().getJobId(), ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                        String totalEffort = ownerTimeSheetEntity.getHours();
                        List<Integer> totalHours = Arrays.stream(totalEffort.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                        AtomicInteger total = new AtomicInteger();
                        totalHours.forEach(total::addAndGet);
                        Hibernate.initialize(ownerTimeSheetEntity.getSkillOwnerEntity().getPortfolioUrl());
                        WorkEfforts efforts = new WorkEfforts();
                        efforts.setTimesheetId(timesheet.get(0).getTimeSheetId());
                        efforts.setSkillOwnerEntityId(ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId());
                        efforts.setSkillSeekerEntityId(ownerTimeSheetEntity.getSkillSeekerEntity().getId());
                        if (null != ownerTimeSheetEntity.getSkillSeekerProjectEntity()) {
                            efforts.setSkillSeekerProjectEntityId(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getId());
                            efforts.setProjectName(ownerTimeSheetEntity.getSkillSeekerProjectEntity().getTitle());
                        } else {
                            efforts.setSkillSeekerProjectEntityId(0);
                            efforts.setProjectName("Default");
                        }
                        if (null != ownerTimeSheetEntity.getSkillOwnerEntity().getLastName()) {
                            efforts.setOwnerName(ownerTimeSheetEntity.getSkillOwnerEntity().getFirstName() + " " + ownerTimeSheetEntity.getSkillOwnerEntity().getLastName());
                        } else {
                            efforts.setOwnerName(ownerTimeSheetEntity.getSkillOwnerEntity().getFirstName());
                        }
                        efforts.setClientName(ownerTimeSheetEntity.getSkillSeekerEntity().getSkillSeekerName());
                        efforts.setRate(selectionPhase.get().getRate());
//                        efforts.setRate(poEntity.get().getRateCard());
                        poEntity.ifPresent(entity -> efforts.setDesignation(entity.getJobId().getJobTitle()));

                        double actualAmount = (selectionPhase.get().getRate() * total.get()) * ownerTimeSheetEntity.getSkillOwnerEntity().getSkillPartnerEntity().getServiceFeePercentage();
                        efforts.setActualAmount((selectionPhase.get().getRate() * total.get()) - actualAmount);
                        int i = selectionPhase.get().getRate() * total.get();

//                        double actualAmount = (poEntity.get().getRateCard() * total.get()) * ownerTimeSheetEntity.getSkillOwnerEntity().getSkillPartnerEntity().getServiceFeePercentage();
//                        efforts.setActualAmount((poEntity.get().getRateCard() * total.get()) - actualAmount);
//                        int i = poEntity.get().getRateCard() * total.get();
                        efforts.setServiceFeesAmount(i * ownerTimeSheetEntity.getSkillOwnerEntity().getSkillPartnerEntity().getServiceFeePercentage());

                        efforts.setTotalHours(total.get());
                        efforts.setTimesheetStartDate(ownerTimeSheetEntity.getStartDate().toLocalDate());
                        efforts.setTimesheetEndDate(ownerTimeSheetEntity.getEndDate().toLocalDate());
                        efforts.setAmount(selectionPhase.get().getRate() * total.get());
//                        efforts.setAmount(poEntity.get().getRateCard() * total.get());
//                        effortList.add(efforts);
                        if (effortList.stream().anyMatch(effort -> effort.getSkillOwnerEntityId() == ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId())) {
                            WorkEfforts workEfforts = effortList.stream().filter(effort -> effort.getSkillOwnerEntityId() == ownerTimeSheetEntity.getSkillOwnerEntity().getSkillOwnerEntityId()).findFirst().get();
                            workEfforts.setTotalHours(workEfforts.getTotalHours() + total.get());
                            workEfforts.setAmount(workEfforts.getAmount() + efforts.getAmount());
                        } else {
                            effortList.add(efforts);
                        }
                    }
                }
            } else {
                throw new ServiceException(NO_TIMESHEET_DATA.getErrorCode(), NO_TIMESHEET_DATA.getErrorDesc());
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
        logger.info("InvoiceServiceImpl || getWorkEfforts || Getting WorkEfforts of SkillOwners");
        return effortList;
    }

    @Override
    @Transactional
    public InvoiceResponse updatePartnerInvoiceStatus(String invoiceId, int id, String comments) {
        Optional<Invoice> invoiceEntity = invoiceRepository.findById(invoiceId);
        Optional<InvoiceStatus> invoiceStatus = invoiceStatusRepository.findById(id);
        InvoiceResponse response = new InvoiceResponse();
        if (invoiceEntity.isPresent() && invoiceStatus.isPresent()) {
            invoiceEntity.get().setInvoiceStatus(invoiceStatus.get());
            invoiceEntity.get().setComments(comments);
            invoiceRepository.save(invoiceEntity.get());
            response.setInvoiceId(invoiceId);
            response.setInvoiceStatus(invoiceStatus.get());
            Notification notification = new Notification();
            notificationService.partnerInvoiceStatusNotification(invoiceEntity.get(), notification);
        }
        return response;
    }

    @Override
    @Transactional
    public InvoiceResponse updateAdminInvoiceStatus(String invoiceId, int id) {
        Optional<InvoiceAdmin> invoiceEntity = adminInvoiceRepository.findById(invoiceId);
        Optional<InvoiceStatus> invoiceStatus = invoiceStatusRepository.findById(id);
        InvoiceResponse response = new InvoiceResponse();
        if (invoiceEntity.isPresent() && invoiceStatus.isPresent()) {
            invoiceEntity.get().setInvoiceStatus(invoiceStatus.get());
            adminInvoiceRepository.save(invoiceEntity.get());
            response.setInvoiceId(invoiceId);
            response.setInvoiceStatus(invoiceStatus.get());
        }
        return response;
    }

    /**
     * @return all invoice status
     */
    @Override
    public List<InvoiceStatus> getInvoiceStatus() {
        try {
            return invoiceStatusRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(DATA_NOT_FOUND.getErrorCode(), DATA_NOT_FOUND.getErrorDesc());
        }
    }

    @Override
    @Transactional
    public List<AdminInvoice> getAdminInvoiceData(int seekerId, int projectId) {
        List<AdminInvoice> invoiceDataList = new ArrayList<>();
        List<InvoiceData> invoiceDataValue;
        if (projectId == 0) {
            invoiceDataValue = invoiceDataRepository.findByAndSeekerIdAndDefaultProject(seekerId);
        } else {
            invoiceDataValue = invoiceDataRepository.findByProjectIdAndSeekerId(seekerId, projectId);
        }
        if (!invoiceDataValue.isEmpty()) {
            for (InvoiceData invoiceData : invoiceDataValue) {
                AdminInvoice adminInvoice = new AdminInvoice();
                List<Invoice> invoices = invoiceRepository.findAll();
                for (Invoice invoice : invoices) {

                    for (InvoiceData invoiceDataValues : invoice.getInvoiceData()) {
                        if (invoiceDataValues.getId() == invoiceData.getId()) {
                            Optional<PoEntity> poEntity = poRepository.findByOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId());
                            adminInvoice.setStartDate(invoice.getWeekStartDate());
                            adminInvoice.setEndDate(invoice.getWeekEndDate());

                            adminInvoice.setInvoiceDataId(invoiceData.getId());
                            adminInvoice.setOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId());
                            adminInvoice.setOwnerName(invoiceData.getOwnerId().getFirstName());
                            adminInvoice.setActualAmount(invoiceData.getActualAmount());
                            if (null != invoiceData.getOwnerId().getLastName()) {
                                adminInvoice.setOwnerName(invoiceData.getOwnerId().getFirstName() + " " + invoiceData.getOwnerId().getLastName());
                            }
                            if (poEntity.isPresent()) {
                                adminInvoice.setPosition(poEntity.get().getJobId().getJobTitle());
                            }
                            adminInvoice.setAmount(invoiceData.getAmount());
                            adminInvoice.setClient(invoiceData.getSkillSeeker().getSkillSeekerName());
                            adminInvoice.setTotalHours(invoiceData.getTotalHours());

                                getSelectionPhases(invoiceData).get().stream().filter(selectionPhase -> selectionPhase.
                                                getJob().getSkillSeeker().getId() == invoiceData.getSkillSeeker().getId()).
                                        mapToInt(SelectionPhase::getRate).forEach(adminInvoice::setRateCard);
//                            adminInvoice.setRateCard(poEntity.get().getRateCard());
                            adminInvoice.setInvoiceCreationDate(invoice.getInvoiceDate());
                            adminInvoice.setInvoiceDueDate(invoice.getDueDate());
                            adminInvoice.setTimesheetId(invoice.getTimesheetId());
                            invoiceDataList.add(adminInvoice);
                        }

                    }
                }
            }
            logger.info("InvoiceServiceImpl || getAdminInvoiceData || getting Invoice By Seeker And Project Id");
            return invoiceDataList;
        } else {
            throw new ServiceException(NO_INVOICE_FOUND.getErrorCode(), NO_INVOICE_FOUND.getErrorDesc());
        }

    }

    private Optional<List<SelectionPhase>> getSelectionPhases(InvoiceData invoiceData) {
        try {
            Optional<List<SelectionPhase>> ownerId = selectionPhaseRepository.findBySkillOwnerId(invoiceData.getOwnerId().getSkillOwnerEntityId());
            return ownerId;
        } catch (ServiceException e) {
            throw new ServiceException(INVOICE_DATA.getErrorCode(), INVOICE_DATA.getErrorCode());
        }
    }

    @Override
    public List<InvoiceDetails> getAllInvoiceDetails() {

        List<InvoiceDetails> invoiceDetails = new ArrayList<>();
        List<Invoice> invoiceDataList = invoiceRepository.findAll();
        invoiceDataList.forEach(invoiceData -> {
            InvoiceDetails invoiceDetail = new InvoiceDetails();
            invoiceDetail.setInvoiceId(invoiceData.getId());
            invoiceDetail.setDate(invoiceData.getInvoiceDate());
            invoiceDetail.setStatus(invoiceData.getInvoiceStatus());
            if (invoiceData.getInvoiceStatus().getStatus().equals("Approved") && !invoiceData.isApprovedStatus()) {
                ZoneId zone = ZoneId.of("America/New_York");
                ZonedDateTime currentDate = ZonedDateTime.now(zone);
                LocalDate plusDays = currentDate.toLocalDate().plusDays(41);
                invoiceDetail.setPaymentDueDate(plusDays);
                invoiceData.setApprovedStatus(true);
                invoiceData.setDueDate(plusDays);
                invoiceRepository.saveAndFlush(invoiceData);
            } else if (isValidStatus(invoiceData.getInvoiceStatus().getStatus())) {
                invoiceDetail.setPaymentDueDate(invoiceData.getDueDate());
            }
            LocalDate expired = invoiceData.getDueDate().plusDays(1);
            if (invoiceData.getInvoiceStatus().getStatus().equals("Approved") && (expired.isBefore(currentDate) || expired.equals(currentDate))) {
                invoiceDetail.setStatus(invoiceStatusRepository.findById(5).get());
                invoiceData.setInvoiceStatus(invoiceStatusRepository.findById(5).get());
                invoiceRepository.saveAndFlush(invoiceData);
            }
            invoiceDetail.setPartnerId(invoiceData.getSkillPartner().getSkillPartnerId());
            invoiceDetail.setPartnerName(invoiceData.getSkillPartner().getBusinessName());
            invoiceDetail.setWeekStartDate(invoiceData.getWeekStartDate());
            invoiceDetail.setComments(invoiceData.getComments());
            invoiceDetails.add(invoiceDetail);

        });
        logger.info("InvoiceServiceImpl || getAllInvoiceDetails || get AllInvoiceDetails");
        return invoiceDetails;
    }

    private boolean isValidStatus(String status) {
        Map<String, Boolean> validStatus = new HashMap<>();
        validStatus.put("Approved", true);
        validStatus.put("Paid", true);
        validStatus.put("Pending", true);
        return validStatus.containsKey(status);
    }

    @Override
    @Transactional

    public List<SeekerInvoice> getAllInvoiceOfSeeker(int id) {

        List<SeekerInvoice> seekerInvoices = new ArrayList<>();

        List<AdminInvoiceData> adminInvoiceDataList = adminInvoiceDataRepository.findInvoiceBySeekerId(id);

        adminInvoiceDataList.forEach(adminInvoiceData -> {
            SeekerInvoice seekerInvoice = new SeekerInvoice();
            Optional<List<Invoice>> invoices = invoiceRepository.findBySkillPartnerId(adminInvoiceData.getOwnerId().getSkillPartnerEntity().getSkillPartnerId());

            for (Invoice invoice : invoices.get()) {
                invoice.getInvoiceData().forEach(invoiceData -> {
                    if (adminInvoiceData.getId() == invoiceData.getId()) {
                        seekerInvoice.setDate(invoice.getDueDate());
                        seekerInvoice.setInvoiceId(invoice.getId());
                    }
                });
            }
            seekerInvoice.setSender("FlexCub");
            seekerInvoice.setStatus("Accept");
            if (null != adminInvoiceData.getSkillSeekerProjectId()) {
                seekerInvoice.setDetails(adminInvoiceData.getSkillSeekerProjectId().getTitle());
            } else {
                seekerInvoice.setDetails("Default");
            }
            seekerInvoices.add(seekerInvoice);


        });
        return seekerInvoices;
    }

    @Override
    @Transactional
    public SeekerInvoiceStatus updateSeekerInvoiceStatus(String invoiceId, int statusId, String comments) {

        Optional<InvoiceAdmin> invoiceEntity = adminInvoiceRepository.findById(invoiceId);
        Optional<InvoiceStatus> invoiceStatus = invoiceStatusRepository.findById(statusId);
        SeekerInvoiceStatus invoiceStatus1 = new SeekerInvoiceStatus();
        if (invoiceEntity.isPresent() && invoiceStatus.isPresent()) {

            invoiceEntity.get().setInvoiceStatus(invoiceStatus.get());
            invoiceEntity.get().setComment(comments);
            adminInvoiceRepository.saveAndFlush(invoiceEntity.get());
            Notification notification = new Notification();
            notificationService.seekerInvoiceStatusNotification(invoiceEntity.get(), notification);
            invoiceStatus1.setAdminInvoiceId(invoiceId);
            invoiceStatus1.setComment(comments);
            invoiceStatus1.setStatusId(invoiceStatus.get().getId());

        }
        return invoiceStatus1;
    }

    @Override
    @Transactional
    public List<InvoiceResponse> saveInvoiceDetailsByAdmin(List<AdminInvoiceRequest> invoiceRequest) {
        try {
            List<InvoiceResponse> invoiceResponses = new ArrayList<>();
            invoiceRequest.forEach(adminInvoiceRequest -> {
                InvoiceResponse response = new InvoiceResponse();
                Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(adminInvoiceRequest.getInvoiceId());
                SkillOwnerEntity skillOwnerId = skillOwnerRepository.findBySkillOwnerEntityId(invoiceData.get().getOwnerId().getSkillOwnerEntityId());
                InvoiceAdmin invoice = new InvoiceAdmin();
                invoice.setSkillPartnerEntity(skillOwnerId.getSkillPartnerEntity());
                invoice.setInvoiceDate(LocalDate.now());
                invoice.setInvoiceStatus(invoiceStatusRepository.findById(1).get());

                Optional<List<Invoice>> invoices = invoiceRepository.findBySkillPartnerId(skillOwnerId.getSkillPartnerEntity().getSkillPartnerId());

                for (Invoice invoice1 : invoices.get()) {
                    invoice1.getInvoiceData().forEach(invoiceData1 -> {
                        if (adminInvoiceRequest.getInvoiceId() == invoiceData1.getId()) {
                            invoice.setDueDate(invoice1.getDueDate());
                            invoice.setTimesheetId(invoice1.getTimesheetId());
                        }
                    });
                }

                List<AdminInvoiceData> invoiceDataList = new ArrayList<>();
                int totalAmount = 0;
                AdminInvoiceData data = new AdminInvoiceData();
                data.setOwnerId(skillOwnerRepository.findBySkillOwnerEntityId(invoiceData.get().getOwnerId().getSkillOwnerEntityId()));
                data.setSkillSeeker(skillSeekerRepository.findById(invoiceData.get().getSkillSeeker().getId()).get());
                if (null != invoiceData.get().getSkillSeekerProjectId()) {
                    data.setSkillSeekerProjectId(skillSeekerProjectRepository.findById(invoiceData.get().getSkillSeekerProjectId().getId()).get());
                } else {
                    data.setSkillSeekerProjectId(null);
                }
                data.setInvoiceStatus(invoiceStatusRepository.findById(1).get());
                data.setTotalHours(invoiceData.get().getTotalHours());
                data.setAmount(invoiceData.get().getAmount());
                totalAmount = totalAmount + invoiceData.get().getAmount();
                invoiceDataList.add(data);

                invoice.setInvoiceData(invoiceDataList);
                InvoiceAdmin savedInvoice = adminInvoiceRepository.saveAndFlush(invoice);
                Notification notification = new Notification();

                notificationService.seekerInvoiceStatusNotification(savedInvoice, notification);
                response.setInvoiceId(savedInvoice.getId());
                response.setTotalAmount(totalAmount);
                response.setInvoiceStatus(invoice.getInvoiceStatus());
                invoiceData.get().setInvoiceGeneratedByAdmin(true);
                invoiceDataRepository.save(invoiceData.get());
                invoiceResponses.add(response);
            });
            logger.info("InvoiceServiceImpl || saveInvoiceDetailsByAdmin || Invoice Generated By Admin To Seeker");
            return invoiceResponses;
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }


//
//        List<AdminInvoiceData> adminInvoiceDataList = adminInvoiceDataRepository.findInvoiceBySeekerId(id);
//        adminInvoiceData.getInvoiceStatus();
//        SkillSeekerInvoice skillSeekerInvoice = new SkillSeekerInvoice();
//        for (AdminInvoiceData adminInvoiceData1 : adminInvoiceDataList) {
//            String adminInvoiceStatus = "Accept";
//            String rejectStatus = "Reject";
//            String approveStatus = "Approved";
//            String PaidStatus = "Paid";
//            String PendingStatus = "Pending";
//
//            if (adminInvoiceData1.getInvoiceStatus().equals(adminInvoiceStatus)) {
//                skillSeekerInvoice.setStatus(approveStatus);
//                adminInvoiceDataRepository.save(adminInvoiceData1);
//            } else if (adminInvoiceData1.getInvoiceStatus().equals(adminInvoiceStatus)) {
//                skillSeekerInvoice.setStatus(rejectStatus);
//                adminInvoiceDataRepository.save(adminInvoiceData1);
//            } else if (adminInvoiceData1.getInvoiceStatus().equals(adminInvoiceStatus) && seekerInvoice.getDate().plusDays(14).isAfter(LocalDate.now())) {
//                skillSeekerInvoice.setStatus(PendingStatus);
//                adminInvoiceDataRepository.save(adminInvoiceData1);


//    @Override
//    @Transactional
//    public List<InvoiceResponse> saveInvoiceDetailsByAdmin(List<AdminInvoiceRequest> invoiceRequest) {
//        try {
//            List<InvoiceResponse> invoiceResponses = new ArrayList<>();
//            invoiceRequest.forEach(adminInvoiceRequest -> {
//                InvoiceResponse response = new InvoiceResponse();
//
//                Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(adminInvoiceRequest.getInvoiceId());
//                SkillOwnerEntity skillOwnerId = skillOwnerRepository.findBySkillOwnerEntityId(invoiceData.get().getOwnerId().getSkillOwnerEntityId());
//                InvoiceAdmin invoice = new InvoiceAdmin();
//                invoice.setSkillPartnerEntity(skillOwnerId.getSkillPartnerEntity());
//                invoice.setInvoiceDate(LocalDate.now());
//                invoice.setInvoiceStatus(invoiceStatusRepository.findById(1).get());
//
//                Optional<List<Invoice>> invoices = invoiceRepository.findBySkillPartnerId(skillOwnerId.getSkillPartnerEntity().getSkillPartnerId());
//
//                for (Invoice invoice1 : invoices.get()) {
//                    invoice1.getInvoiceData().forEach(invoiceData1 -> {
//                        if (adminInvoiceRequest.getInvoiceId() == invoiceData1.getId()) {
//                            invoice.setDueDate(invoice1.getDueDate());
//                        }
//                    });
//                }
//
//                List<AdminInvoiceData> invoiceDataList = new ArrayList<>();
//                int totalAmount = 0;
//                AdminInvoiceData data = new AdminInvoiceData();
//                data.setOwnerId(skillOwnerRepository.findBySkillOwnerEntityId(invoiceData.get().getOwnerId().getSkillOwnerEntityId()));
//                data.setSkillSeeker(skillSeekerRepository.findById(invoiceData.get().getSkillSeeker().getId()).get());
//                data.setSkillSeekerProjectId(skillSeekerProjectRepository.findById(invoiceData.get().getSkillSeekerProjectId().getId()).get());
//                data.setInvoiceStatus(invoiceStatusRepository.findById(1).get());
//                data.setTotalHours(invoiceData.get().getTotalHours());
//                data.setAmount(invoiceData.get().getAmount());
//                totalAmount = totalAmount + invoiceData.get().getAmount();
//                invoiceDataList.add(data);
//
//                invoice.setInvoiceData(invoiceDataList);
//                InvoiceAdmin savedInvoice = adminInvoiceRepository.save(invoice);
//                response.setInvoiceId(savedInvoice.getId());
//                response.setTotalAmount(totalAmount);
//                response.setInvoiceStatus(invoice.getInvoiceStatus());
//                invoiceData.get().setInvoiceGeneratedByAdmin(true);
//                invoiceDataRepository.save(invoiceData.get());
//                invoiceResponses.add(response);
//            });
//            logger.info("InvoiceServiceImpl || saveInvoiceDetailsByAdmin || Invoice Generated By Admin To Seeker");
//            return invoiceResponses;
//        } catch (Exception e) {
//            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
//        }
//    }

    @Override
    public List<ClientInvoiceDetails> invoiceClientDetails() {
        try {
            List<InvoiceData> invoiceDataList = invoiceDataRepository.findAll();
            List<ClientInvoiceDetails> clientInvoiceDetailsList = new ArrayList<>();
            Map<Integer, Integer> map = new HashMap<>();
            invoiceDataList.forEach(invoiceData -> {
                if (null == invoiceData.getSkillSeekerProjectId()) {
                    SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
                    skillSeekerProjectEntity.setId(0);
                    skillSeekerProjectEntity.setTitle("Default");
                    invoiceData.setSkillSeekerProjectId(skillSeekerProjectEntity);
                }
                ClientInvoiceDetails clientInvoiceDetails = new ClientInvoiceDetails();
                if (!map.containsKey(invoiceData.getSkillSeeker().getId())) {
                    map.put(invoiceData.getSkillSeeker().getId(), invoiceData.getSkillSeekerProjectId().getId());
                    clientInvoiceDetails.setSkillSeekerId(invoiceData.getSkillSeeker().getId());
                    clientInvoiceDetails.setSkillSeekerName(invoiceData.getSkillSeeker().getSkillSeekerName());
                    Map<Integer, Integer> map1 = new HashMap<>();
                    List<ClientProjects> clientProjectsList = new ArrayList<>();
                    List<InvoiceData> invoiceProjectList = invoiceDataList.stream().filter(f -> f.getSkillSeeker().getId() == invoiceData.getSkillSeeker().getId()).collect(Collectors.toList());
                    invoiceProjectList.forEach(invoiceData1 -> {
                        if (null == invoiceData1.getSkillSeekerProjectId()) {
                            SkillSeekerProjectEntity skillSeekerProjectEntity = new SkillSeekerProjectEntity();
                            skillSeekerProjectEntity.setId(0);
                            skillSeekerProjectEntity.setTitle("Default");
                            invoiceData1.setSkillSeekerProjectId(skillSeekerProjectEntity);
                        }
                        ClientProjects projects = new ClientProjects();
                        if (invoiceData.getSkillSeeker().getId() == invoiceData1.getSkillSeeker().getId()) {
                            if (!map1.containsKey(invoiceData1.getSkillSeekerProjectId().getId())) {
                                map1.put(invoiceData1.getSkillSeekerProjectId().getId(), invoiceData.getSkillSeeker().getId());
                                projects.setProjectId(invoiceData1.getSkillSeekerProjectId().getId());
                                projects.setProjectName(invoiceData1.getSkillSeekerProjectId().getTitle());
                                clientProjectsList.add(projects);
                            }
                        }
                    });
                    clientInvoiceDetails.setClientProjects(clientProjectsList);
                    clientInvoiceDetailsList.add(clientInvoiceDetails);
                }
            });
            logger.info("InvoiceServiceImpl || invoiceClientDetails || Getting Client Details");
            return clientInvoiceDetailsList;
        } catch (Exception e) {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

    @Override
    public List<InvoiceDetails> getAllInvoiceDetailAdmin() {
        List<InvoiceDetails> invoiceDetails = new ArrayList<>();
        List<InvoiceAdmin> invoiceDataList = adminInvoiceRepository.findAll();
        invoiceDataList.forEach(invoiceData -> {
            InvoiceDetails invoiceDetail = new InvoiceDetails();
            invoiceDetail.setInvoiceId(invoiceData.getId());
            invoiceDetail.setDate(invoiceData.getInvoiceDate());
            invoiceDetail.setStatus(invoiceData.getInvoiceStatus());
            if (invoiceData.getInvoiceStatus().getStatus().equals("Approved") && !invoiceData.isApprovedStatus()) {
                ZoneId zone = ZoneId.of("America/New_York");
                ZonedDateTime currentDate = ZonedDateTime.now(zone);
                LocalDate plusDays = currentDate.toLocalDate().plusDays(41);
                invoiceDetail.setPaymentDueDate(plusDays);
                invoiceData.setApprovedStatus(true);
                invoiceData.setDueDate(plusDays);
                adminInvoiceRepository.saveAndFlush(invoiceData);
            } else if (isValidStatus(invoiceData.getInvoiceStatus().getStatus())) {
                invoiceDetail.setPaymentDueDate(invoiceData.getDueDate());
            }
            LocalDate expired = invoiceData.getDueDate().plusDays(1);
            if (invoiceData.getInvoiceStatus().getStatus().equals("Approved") && (expired.isBefore(currentDate) || expired.equals(currentDate))) {
                invoiceDetail.setStatus(invoiceStatusRepository.findById(5).get());
                invoiceData.setInvoiceStatus(invoiceStatusRepository.findById(5).get());
                adminInvoiceRepository.saveAndFlush(invoiceData);
            }

            invoiceDetail.setComments(invoiceData.getComment());
            if (invoiceData.getInvoiceData().size() > 0) {
                if (null != invoiceData.getInvoiceData().get(0).getSkillSeekerProjectId()) {
                    invoiceDetail.setSeekerProjectName(invoiceData.getInvoiceData().get(0).getSkillSeekerProjectId().getTitle());
                } else {
                    invoiceDetail.setSeekerProjectName("Default");
                }
                invoiceDetail.setClientName(invoiceData.getInvoiceData().get(0).getSkillSeeker().getSkillSeekerName());
                invoiceDetail.setPartnerId(invoiceData.getSkillPartnerEntity().getSkillPartnerId());
                invoiceDetail.setPartnerName(invoiceData.getSkillPartnerEntity().getPrimaryContactFullName());
                invoiceDetails.add(invoiceDetail);
            } else {
                throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
            }
        });
        logger.info("InvoiceServiceImpl || getAllInvoiceDetailAdmin || Getting All Invoice From Admin");

        return invoiceDetails;
    }

    @Override
    @Transactional
    public List<InvoiceDetails> getAdminInvoiceBySeeker(int seekerId) {
        List<InvoiceDetails> adminInvoice = new ArrayList<>();
        List<InvoiceAdmin> invoiceAdmins = adminInvoiceRepository.findAll();
        invoiceAdmins.forEach(invoiceAdmin -> {
            if (invoiceAdmin.getInvoiceData().get(0).getSkillSeeker().getId() == seekerId) {
                InvoiceDetails invoice = new InvoiceDetails();
                invoice.setInvoiceId(invoiceAdmin.getId());
                if (null != invoiceAdmin.getInvoiceData().get(0).getSkillSeekerProjectId()) {
                    invoice.setSeekerProjectName(invoiceAdmin.getInvoiceData().get(0).getSkillSeekerProjectId().getTitle());
                } else {
                    invoice.setSeekerProjectName("Default");
                }
                invoice.setClientName(invoiceAdmin.getInvoiceData().get(0).getSkillSeeker().getSkillSeekerName());
                invoice.setDate(invoiceAdmin.getInvoiceDate());
                invoice.setStatus(invoiceAdmin.getInvoiceStatus());
                if (invoiceAdmin.getInvoiceStatus().getStatus().equals("Approved") && !invoiceAdmin.isApprovedStatus()) {
                    LocalDate date = LocalDate.now();
                    LocalDate plusDays = date.plusDays(41);
                    invoice.setPaymentDueDate(plusDays);
                    invoiceAdmin.setApprovedStatus(true);
                    invoiceAdmin.setDueDate(plusDays);
                    adminInvoiceRepository.saveAndFlush(invoiceAdmin);
                } else if (isValidStatus(invoiceAdmin.getInvoiceStatus().getStatus())) {
                    invoice.setPaymentDueDate(invoiceAdmin.getDueDate());
                }
                LocalDate expired = invoiceAdmin.getDueDate().plusDays(1);
                LocalDate currentDate = LocalDate.now();
                if (invoiceAdmin.getInvoiceStatus().getStatus().equals("Approved") && (expired.isBefore(currentDate) || expired.equals(currentDate))) {
                    invoice.setStatus(invoiceStatusRepository.findById(5).get());
                    invoiceAdmin.setInvoiceStatus(invoiceStatusRepository.findById(5).get());
                    adminInvoiceRepository.saveAndFlush(invoiceAdmin);
                }


                invoice.setComments(invoiceAdmin.getComment());
                adminInvoice.add(invoice);
            }
        });
        logger.info("InvoiceServiceImpl || getAdminInvoiceBySeeker || Getting All Invoice From Admin Using Seeker");
        return adminInvoice;
    }

    @Transactional
    @Override
    public InvoiceUpdate updateInvoiceDetailsByPartner(InvoiceUpdate invoiceUpdateRequest) {

        Optional<Invoice> invoice = invoiceRepository.findById(invoiceUpdateRequest.getInvoiceId());
        if (invoice.isPresent()) {
            BeanUtils.copyProperties(invoiceUpdateRequest, invoice.get(), NullPropertyName.getNullPropertyNames(invoiceUpdateRequest));
            invoice.get().setId(invoiceUpdateRequest.getInvoiceId());
            for (int j = 0; j < invoiceUpdateRequest.getInvoiceUpdateList().size(); j++) {
                invoice.get().getInvoiceData().get(j).setAmount(invoiceUpdateRequest.getInvoiceUpdateList().get(j).getAmount());
                invoice.get().getInvoiceData().get(j).setTotalHours(invoiceUpdateRequest.getInvoiceUpdateList().get(j).getTotalHours());
                invoiceRepository.save(invoice.get());

            }

            List<InvoiceUpdateData> collect = invoice.get().getInvoiceData().stream().map(invoiceData -> {
                return modelMapper.map(invoiceData, InvoiceUpdateData.class);
            }).collect(Collectors.toList());

            InvoiceUpdate map = modelMapper.map(invoice.get(), InvoiceUpdate.class);
            map.setInvoiceUpdateList(collect);
            return map;

        } else {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }


    }

    @Transactional
    @Override
    public InvoiceUpdate updateInvoiceDetailsByAdmin(InvoiceUpdate invoiceUpdateRequest) {
        Optional<InvoiceAdmin> invoice = adminInvoiceRepository.findById(invoiceUpdateRequest.getInvoiceId());
        if (invoice.isPresent()) {
            BeanUtils.copyProperties(invoiceUpdateRequest, invoice.get(), NullPropertyName.getNullPropertyNames(invoiceUpdateRequest));
            invoice.get().setId(invoiceUpdateRequest.getInvoiceId());
            invoice.get().setInvoiceDate(invoiceUpdateRequest.getInvoiceDate());
            for (int j = 0; j < invoiceUpdateRequest.getInvoiceUpdateList().size(); j++) {
                invoice.get().getInvoiceData().get(j).setAmount(invoiceUpdateRequest.getInvoiceUpdateList().get(j).getAmount());
                invoice.get().getInvoiceData().get(j).setTotalHours(invoiceUpdateRequest.getInvoiceUpdateList().get(j).getTotalHours());
                adminInvoiceRepository.save(invoice.get());

            }
            List<InvoiceUpdateData> collect = invoice.get().getInvoiceData().stream().map(invoiceData -> {
                return modelMapper.map(invoiceData, InvoiceUpdateData.class);
            }).collect(Collectors.toList());

            InvoiceUpdate map = modelMapper.map(invoice.get(), InvoiceUpdate.class);
            map.setInvoiceUpdateList(collect);
            return map;
        } else {
            throw new ServiceException(INVALID_REQUEST.getErrorCode(), INVALID_REQUEST.getErrorDesc());
        }
    }

}