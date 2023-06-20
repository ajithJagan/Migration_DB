package com.flexcub.resourceplanning.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FlexcubErrorCodes {

    INVALID_FIRST_NAME("FC1001", "Invalid first name"),
    INVALID_LAST_NAME("FC1002", "Invalid last name"),
    INVALID_EMAIL_ID("FC1003", "Invalid email address"),
    INVALID_DOB("FC1004", "Invalid date of birth"),
    INVALID_PHONE("FC1005", "Invalid phone number"),
    INVALID_P_EMAIL("FC1006", "Invalid primary email address"),
    INVALID_TIN("FC1007", "Invalid tax id"),
    INVALID_BUSINESS_NAME("FC1008", "Invalid business name"),
    INVALID_LICENSE("FC1009", "Invalid business license number"),
    INVALID_P_NAME("FC1010", "Invalid primary name"),
    DATA_NOT_SAVED("FC1011", "[DEV]Unable to save data"),
    EXISTING_BUSINESS("FC1012", "Business details already exists"),
    EXISTING_CANDIDATE("FC1013", "Candidate details already exists"),
    CONTROLLER_ISSUE("FC1014", "Error occurred in the controller"),
    DATA_NOT_FOUND("FC1015", "Data not found for given request"),

    INVALID_ID_REQUEST("FC1078", "Data not found for given request"),
    ID_NOT_FOUND("FC1079", "User not available"),
    PARAM_NOT_PROVIDED("FC1016", "[DEV]Request param null/invalid"),
    INVALID_REGISTRATION_LINK("FC1017", "Invalid registration link"),
    USER_ALREADY_VERIFIED("FC1018", "User already verified"),
    LINK_EXPIRED("FC1019", "Verification link expired"),
    DOMAIN_VALUES_EXIST("FC1020", "Domain value already exists"),
    TECHNOLOGY_EXIST("FC1021", "Technology value already exists"),

    INVALID_PASSWORD("FC1022", "Invalid password"),
    NO_EMAIL("FC1023", "No email id found"),
    INVALID_LASTUSED("FC1024", "Last used date can't be future date"),
    EXISTING_PRIMARYPHONE("FC1025", "Primary phone number already exists"),
    EXISTING_EMAIL("FC1026", "Account already exists with the given email address"),
    INVALID_PRIMARYPHONE("FC1027", "Phone number should be of 10 digits"),
    INVALID_OLD_PASSWORD("FC1028", "Old password invalid"),
    INVALID_LINK_FOR_FORGOT_PASSWORD("FC1029", "Forgot password link is expired"),
    INVALID_JOB_ID("FC1030", "Invalid job id, please provide valid job id"),
    WRONG_FILE_FORMAT("FC1031", "File format not supported"),
    FILE_NOT_FOUND("FC1032", "File not found"),
    FILE_NOT_SYNCED("FC1033", "Could not sync file"),
    WRONG_TEMPLATE("FC1034", "Wrong template"),
    INVALID_JOB_SKILL_OWNER_COMBO("FC1035", "[DEV]Invalid combination of job id and skill owner id"),
    INVALID_REQUIREMENT_PHASE("FC1036", "[DEV]Invalid requirement phase"),
    EXPECTATION_FAILED("FC1037", "Please retry"),

    ATTACHMENT_UPDATE_FAILED("FC1038", "Failed to upload attachment"),
    BODY_EMPTY("FC1039", "[DEV]Request body null/invalid"),

    JOB_ALREADY_EXIST("FC1039", "Job already created"),
    INVALID_REQUEST("FC1040", "Invalid request"),
    FLOW_LOCKED("FC1041", "Flow is locked, can't be edited"),
    SLOT_ALREADY_BOOKED("FC1042", "Slot already booked for your other interview"),
    ACCOUNT_EXPIRED("FC1043", "Proceed to pay for account renewal"),
    JOB_LIMIT_EXCEEDED("FC1044", "Job limit for trial version is exceeded"),
    USER_LIMIT_EXCEEDED("FC1045", "Candidate already shortlisted for 3 jobs"),
    USER_COMMON_SLOT("FC1046", "User has not still selected new slots, auto schedule won't work, please proceed with manual scheduling"),
    ROLE_NOT_DEFINED("FC1047", "Sub-roles not defined yet by admin"),
    PO_ALREADY_ADDED("FC1048", "Purchase order already defined"),
    SOW_ALREADY_ADDED("FC1049", "Statement of work already defined"),

    INVALID_OWNER_ID("FC1050", "Invalid candidate id"),
    RESUME_NOT_FOUND("FC1051", "Please upload resume"),
    IMAGE_NOT_FOUND("FC1052", "Please upload image"),
    INVALID_ID("FC1053", "Invalid id"),
    INVALID_SEEKER("FC1050", "Invalid Seeker id"),
    SLOTS_MISMATCH("FC1051", "Slots mis-match for particular id"),
    PARAMS_MISMATCH("FC1052", "[DEV]parameters are not valid"),
    INVALID_ADMIN_DATA("FC1053", "No data added by admin"),
    INVALID_TECH_DATA_ID("FC1054", "Technology data not found"),
    INVALID_PROJECT_ID("FC1055", "Project not found"),
    INVALID_SEEKER_DATA("FC1056", "Invalid user data"),

    STATUS_NOT_SAVED("FC1057", "Status value not saved"),
    VISA_NOT_FOUND("FC1058", "Visa value not found"),
    ROLES_NOT_FOUND("FC1059", "Roles not found"),
    LEVEL_NOT_FOUND("FC1060", "Level not found"),
    STATUS_VALUES_EXIST("FC1061", "Status value already exists"),
    STATES_NOT_FOUND("FC1062", "States not found"),
    CITIES_NOT_FOUND("FC1063", "Cities not found"),
    DOMAIN_NOT_FOUND("FC1064", "Domain is empty"),
    DOMAIN_DATA_NOT_SAVED("FC1065", "Domain data not saved,invalid request"),
    TECHNOLOGY_NOT_FOUND("FC1066", "Technology list is empty"),
    TOKEN_NOT_FOUND("FC1067", "Token not found"),
    SKILLSET_NOT_FOUND("FC1068", "Skillset not found"),
    MARITAL_STATUS_NOT_FOUND("FC1069", "Marital status not found"),
    INVALID_SEEKER_ID("FC1070", "Invalid seeker id,please provide valid seeker id"),
    INVALID_PURCHASEORDER_ID("FC1071", "Invalid purchase order id,please provide valid purchase order id"),
    NO_PURCHASE_ORDER("FC1072", "No purchase order to be display"),
    INVALID_MSA_ID("FC1073", "Invalid MSA id,please provide valid master service agreement id"),
    INVALID_TAX_ID("FC1074", "Invalid tax id of Skill seeker"),
    INVALID_ROLE_ID("FC1075", "Invalid role id"),
    DATA_NOT_FOUNDED("FC1076", "Data not found against id"),
    INVALID_SOW_ID("FC1077", "Invalid statement of work id"),
    INVALID_DATA("FC1078", "Given datas are invalid"),
    UPDATE_FAILED("FC1079", "Failed to update data"),
    HIRING_ID_NOT_FOUND("FC1080", "Hiring id not found"),
    PARTNER_ID_NOT_FOUND("FC1081", "User not found"),

    INVALID_JOB_ID_OR_INVALID_SKILL_OWNER_ID("FC1082", "Owner Id and Job Id doesn't match in selection phase"),
    INVALID_SKILL_SEEKER_ID("FC1083", "Invalid user"),
    JOB_NOT_FOUND("FC1085", "Job not found"),
    NO_CANDIDATES_SHORTLISTED("FC1086", "No candidates shortlisted for this job"),
    LOGIN_RESTRICTED("FC1094", "Access denied"),
    EMAIL_EXISTS("FC1087", "Account already exists with the given email address"),
    INVALID_INVOICE_ID("FC1095", "Invalid invoice id"),
    TAX_ID_NOT_FOUND("FC1088", "Invalid tax id business license"),
    MSA_ID_NOT_FOUND("FC1089", "No contracts found"),
    TIMESHEET_ALREADY_EXISTS("FC1090", "Invoice already generated "),
    MSA_ALREADY_EXISTS("FC1091", "MSA already exists for this owner"),
    SKILL_OWNER_JOB("FC1092", "Skill owner already in a hiring phase"),
    INVALID_TASK_ID("FC1093", "Task not found"),
    CONTRACT_EXPIRED("FC1094", "On-boarding must be within 14 days from the date of PO creation"),
    NO_INVOICE_FOUND("FC1095", "No invoice found by partner"),
    INVALID_OWNER_COUNT("FC1096", "Invalid ownerid or count "),
    INVALID_NAME("FC1097", "Name cannot be empty"),
    NO_TIMESHEET_DATA("FC1098", "Timesheet details not available for the given date"),
    INVALID_NEW_PASSWORD("FC1099", "New password is same as old password"),
    SSN_NOT_VALID("FC1100", "SSN already exists"),
    INVALID_OWNER_ID_OR_COUNT("FC1101", "Invalid ownerid or count"),
    INVALID_OWNER_ID_OR_MULTIPART_FILE("FC1102", "Invalid ownerid or multipart file"),
    INVALID_FILE("FC1103", "Invalid file please upload pdf or doc"),
    TECHNOLOGY_EXIST_IN_OWNER("FC1104", "Skillset already exists"),
    INVALID_OWNER_OR_PARTNER_ID("FC1105", "Invalid owner or partner id"),
    DATE_TIME_CHECK("FC1123","The current interview has not been completed."),
    UNABLE_SAVE_SEEKER_DATA("FC1133", "Unable to save the Seeker Data"),
    UNABLE_TO_RETRIEVE_SEEKER("FC1129", "Unable to get all seeker details"),

    INVALID_TIMESHEET_ID_OR_MULTIPART_FILE("FC1102", "Invalid timesheet id or multipart file"),

    INVALID_TIMESHEET_ID("FC1106", "Invalid timesheet id "),
    INVALID_RATECARD_VALUE("FC1107", "Invalid RateCard Value"),

    INVALID_TIMESHEETID("FC1108", "Can’t be retrieved"),

    TASK_ALREADY_DEFINED("FC1109", "The task has already been defined."),

    TITLE_EXISTS("FC1110", "Project already exists in the selected department"),
    NEW_SLOT_REQUESTED("FC1111", "Common slots are exhausted,Requested for new slots from SkillOwner"),
    RESCHEDULE_RESTRICTED("FC1112", "Applicable only on or after scheduled interview date  "),
    UNSUPPORTED_FILE_FORMAT("FC1117","File format not supported"),
    NOTIFICATION_FAILED_TO_SEND("FC1121","Notification is failed to send"),
    SEEKER_NOT_FOUND("FC1118","SkillSeeker does not exist"),
    UNABLE_SAVE_MULTIPART_FILE("FC1132", "Unable to save the file"),
    INVALID_CONTRACT_STATUS("FC1131", "None of the contract status found"),
    MSA_FAILED_TO_UPDATE("FC1116","The MSA file is still active"),
    PO_FILE_SUBMITTED_ALREADY("FC1126","PO file is Submitted or Approved Already"),
    PO_FILE_APPROVED_ALREADY("FC1125","PO file is Approved Already"),
    NO_SEEKER_MSA_FOUND("FC1136", "None of the seeker assigned MSA agreement"),
    CONTRACT_FILE_NOT_FOUND("FC1134", "No ContractFiles found with id"),
    UNABLE_UPDATE_MULTIPART_FILE("FC1130", "Unable to update the file"),

    UNABLE_CREATE_NEW_MSA_FILE("FC1135", "Unable to save contract file details"),

    MSA_FILE_NOT_FOUND("FC1119","MSA file is missing, please upload"),
    MSA_TEMPLATE_NOT_FOUND("FC1120","MSA Template is missing, please upload"),
    NO_ACTIVE_PARTNERS("FC1127","SkillPartners does not exist"),
    Project_Expired("FC1137","TimeSheet cannot be created for the expired project"),

    WEEKENDS_RESTRICTED("FC1113", "Please provide the week days"),
    CLIENT_REQUEST("FC1114", "Something went wrong in request. Please check the clientId or other details"),
    INVOICE_DATA("FC1115", "Can't find ownerId"),

    SEEKER_ID_NOT_FOUND("FC1116","Seeker not found"),
    UNABLE_SEND_MAIL("FC1117", "Unable to sent the mail"),
    MSA_NOT_INITIATED("FC1118","MSA is not initiated by SuperAdmin"),

    INVALID_PARTNER_OR_PROJECT_DATA("FC1138","Invalid Partner Or Project Data"),
    SOWDATA_NOT_SAVED("FC1139","Unable to save SOW Data"),
    PARTNER_ACTIVE_MSA_NOT_FOUND("FC1140", "No Active Msa found for the Partners"),
    CONTRACTS_NOT_FOUND("FC1141","Contracts does not exist"),
    SOW_EXISTS("FC1142","Sow already created for this Job with the Partner"),
    NO_VALID_PO_OR_SOW("FC1143","there is no valid PO or SOW");




    private final String errorCode;
    private final String errorDesc;

}