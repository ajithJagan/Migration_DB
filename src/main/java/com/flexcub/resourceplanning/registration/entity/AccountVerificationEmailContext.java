package com.flexcub.resourceplanning.registration.entity;

import com.flexcub.resourceplanning.job.dto.RescheduleDto;
import com.flexcub.resourceplanning.job.dto.ScheduleInterviewDto;
import com.flexcub.resourceplanning.job.entity.RequirementPhase;
import com.flexcub.resourceplanning.job.entity.SelectionPhase;
import com.flexcub.resourceplanning.skillowner.entity.OwnerTimeSheetEntity;
import com.flexcub.resourceplanning.skillpartner.entity.SkillPartnerEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;

    private String forgotToken;



    @Value("${flexcub.baseURLImg}")
    private String baseURLImg;



    private String emailFrom = "no-reply@flexcub.com";

    public void init(RegistrationEntity registration, AbstractEmailContext emailContext) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", registration.getFirstName());
        setTemplateLocation("email-verification");
        setSubject("Complete your registration");
        setFrom(emailFrom);
        setTo(registration.getEmailId());
    }

    public void fail(SkillPartnerEntity skillPartnerEntity, AbstractEmailContext emailContext, List<String> failedList) {
        put("mail.smtp.starttls.enable", "true");
        put("failedReg", skillPartnerEntity.getBusinessName());
        put("failedList", failedList);
        setTemplateLocation("email-registration-failed");
        setSubject("Registration Failed For Skill Owners!!!");
        setFrom(emailFrom);
        setTo(skillPartnerEntity.getBusinessEmail());
    }

    public void forgotPass(RegistrationEntity registration, AbstractEmailContext emailContext) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", registration.getFirstName());
        setTemplateLocation("ForgetPassword");
        setSubject("Reset Your password");
        setFrom(emailFrom);
        setTo(registration.getEmailId());

    }

    public void hiringProcess(AbstractEmailContext emailContext, SelectionPhase selectionPhase) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase.getJob().getJobTitle());
        put("jobDescription", selectionPhase.getJob().getJobDescription());
        put("coreTechnology", selectionPhase.getJob().getCoreTechnology());
        put("SeekerName", selectionPhase.getJob().getSkillSeeker().getSkillSeekerName());
        setTemplateLocation("hiringProcess");
        setSubject("You have been Shortlisted");
        setFrom(emailFrom);
        setTo(selectionPhase.getSkillOwnerEntity().getPrimaryEmail());

    }

    public void scheduleMail(AbstractEmailContext emailContext, SelectionPhase selectionPhase, ScheduleInterviewDto scheduleInterviewDto) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase.getJob().getJobTitle());
        put("jobDescription", selectionPhase.getJob().getJobDescription());
        put("coreTechnology", selectionPhase.getJob().getCoreTechnology());
        put("SeekerName", selectionPhase.getJob().getSkillSeeker().getSkillSeekerName());
        put("modeOfInterview", scheduleInterviewDto.getModeOfInterview());
        put("dateOfInterview", scheduleInterviewDto.getDateOfInterview());
        put("timeOfInterview", scheduleInterviewDto.getTimeOfInterview());

        put("round", scheduleInterviewDto.getStage());
        setTemplateLocation("scheduleMail");
        setSubject("Interview Scheduled");
        setFrom(emailFrom);
        setTo(selectionPhase.getSkillOwnerEntity().getPrimaryEmail());

    }

    public void rescheduleMail(AbstractEmailContext emailContext, SelectionPhase selectionPhase, RescheduleDto rescheduleDto) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase.getJob().getJobTitle());
        put("jobDescription", selectionPhase.getJob().getJobDescription());
        put("coreTechnology", selectionPhase.getJob().getCoreTechnology());
        put("SeekerName", selectionPhase.getJob().getSkillSeeker().getSkillSeekerName());
        put("dateOfInterview", rescheduleDto.getDateOfInterview());
        put("timeOfInterview", rescheduleDto.getStartTime());
        put("round", rescheduleDto.getCurrentStage());
        setTemplateLocation("reScheduleMail");
        setSubject("Interview Rescheduled");
        setFrom(emailFrom);
        setTo(selectionPhase.getSkillOwnerEntity().getPrimaryEmail());

    }

    public void rejectionMail(AbstractEmailContext emailContext, SelectionPhase selectionPhase1) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase1.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase1.getJob().getJobTitle());
        put("skillSeekerName", selectionPhase1.getJob().getSkillSeeker().getSkillSeekerName());
        setTemplateLocation("rejectionMail");
        setSubject("Interview Update");
        setFrom(emailFrom);
        setTo(selectionPhase1.getSkillOwnerEntity().getPrimaryEmail());
    }

    //   public void dateOfInterview(SelectionPhase selectionPhase, RequirementPhase requirementPhase1, AbstractEmailContext emailContext) {
    public void selectedForRound(AbstractEmailContext emailContext, SelectionPhase selectionPhase, ScheduleInterviewDto scheduleInterviewDto, RequirementPhase requirementPhase) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase.getJob().getJobTitle());
        put("jobDescription", selectionPhase.getJob().getJobDescription());
        put("requirementPhaseName", requirementPhase.getRequirementPhaseName());
        put("SeekerName", selectionPhase.getJob().getSkillSeeker().getSkillSeekerName());
        put("modeOfInterview", scheduleInterviewDto.getModeOfInterview());
        put("dateOfInterview", scheduleInterviewDto.getDateOfInterview());
        put("timeOfInterview", scheduleInterviewDto.getTimeOfInterview());

        put("roundName", selectionPhase.getCurrentStage() - 1);
        setTemplateLocation("selectedForRound");
        setSubject("Congratulation");
        setFrom(emailFrom);
        setTo(selectionPhase.getSkillOwnerEntity().getPrimaryEmail());

    }

    public void newSlotRequest(AbstractEmailContext emailContext, SelectionPhase selectionPhase1) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", selectionPhase1.getSkillOwnerEntity().getFirstName());
        put("jobTitle", selectionPhase1.getJob().getJobTitle());
        put("companyName", selectionPhase1.getJob().getSkillSeeker().getSkillSeekerName());
        put("jobDescription", selectionPhase1.getJob().getJobDescription());

        setTemplateLocation("newSlotRequest");
        setSubject("New Date Slots Request");
        setFrom(emailFrom);
        setTo(selectionPhase1.getSkillOwnerEntity().getPrimaryEmail());
    }

    public void trialAccountExpiry(AbstractEmailContext emailContext, RegistrationEntity registrationEntity) {
        put("mail.smtp.starttls.enable", "true");
        put("firstName", registrationEntity.getFirstName());
        setTemplateLocation("TrialVersion_Expiry");
        setSubject("Flexcub Trial Account Expired");
        setFrom("info@flexcub.com");
        setTo(registrationEntity.getEmailId());
    }

    public void skillOwnerTimeSheetApproval(AbstractEmailContext emailContext, OwnerTimeSheetEntity ownerTimeSheet) {
        put("mail.smtp.starttls.enable", "true");
        put("seekerName", ownerTimeSheet.getSkillSeekerEntity().getSkillSeekerName());
        put("skillOwnerName", ownerTimeSheet.getSkillOwnerEntity().getFirstName());
        put("projectName", ownerTimeSheet.getSkillSeekerProjectEntity().getTitle());
        put("startDate", ownerTimeSheet.getStartDate());
        put("endDate", ownerTimeSheet.getEndDate());
        put("hours", ownerTimeSheet.getHours());
        setTemplateLocation("timesheet-approval");
        setSubject("Approval For SkillOwner TimeSheet");
        setFrom("flexcubjunior@outlook.com");
        setTo(ownerTimeSheet.getSkillSeekerEntity().getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void setForgotToken(String forgotToken) {
        this.forgotToken = forgotToken;
        put("forgotToken", forgotToken);
    }

    public void buildAccountExpiryURL(final String baseURLAccountExpiry) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLAccountExpiry).queryParam("expirationURL").toUriString();
        put("expirationURL", url);
    }

    public void buildVerificationUrl(final String baseURL, final String token) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURL+token) .toUriString();
        put("verificationURL", url );
    }


    public void buildForgetPassUrl(final String baseURLforgotPassword, final String forgotToken) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLforgotPassword+forgotToken).toUriString();
        put("forgotPassURL", url);
    }

    public void failedMailUrl(final String baseURL) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURL).queryParam("demo").toUriString();
        put("demo", url);
    }

    public void baseURLHiringProcess(final String baseURLHiringProcess) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLHiringProcess).queryParam("hiringProcess").toUriString();
        put("hiringProcess", url);
    }

    public void baseURLRejectionMail(final String baseURLRejectionMail) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLRejectionMail).queryParam("rejectionMail").toUriString();
        put("rejectionMail", url);
    }

    public void baseURLScheduleMail(final String baseURLScheduleMail) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLScheduleMail).queryParam("scheduleMail").toUriString();
        put("scheduleMail", url);
    }

    public void baseURLSelectedForRoundMail(final String baseURLSelectedForRoundMail) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLSelectedForRoundMail).queryParam("SelectedForRoundMail").toUriString();
        put("selectedForRoundMail", url);
    }

    public void baseURLNewSlotRequest(final String baseURLNewSlotRequest) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLNewSlotRequest).queryParam("newSlotRequest").toUriString();
        put("newSlotRequest", url);
    }

    public void baseURLSkillOwnerTimeSheet(final String baseURLSkillOwnerTimeSheet) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLSkillOwnerTimeSheet).queryParam("approvalLink").toUriString();
        put("approvalLink", url);
    }
    public void baseURLReScheduleMail(final String baseURLReScheduleMail) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseURLReScheduleMail).queryParam("reScheduleMail").toUriString();
        put("reScheduleMail", url);
    }
    public void baseurlLogin(final String baseurlLogin) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseurlLogin).queryParam("logoUrl").toUriString();
        put("logoUrl", url);
    }

}
