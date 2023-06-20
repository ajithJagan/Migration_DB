package com.flexcub.resourceplanning.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlexcubConstants {

    public static final String API = "api/v1";
    public static final String REG_URL = "/registerBusiness";
    public static final String ADD_CANDIDATE = "add-candidate";
    public static final String LOGIN_URL = "/login";
    public static final String GET_BUSINESS = "/businessInfo";
    public static final String VERIFY_USER = "/verify";
    public static final String VERIFY_OWNER = "/verifyOwner";
    public static final String NAME_REGEX = "^[a-zA-Z \\\\s]*$";
    public static final String PHONE_REGEX = "^\\d{10}$";
    public static final String PHONE_SPL_CHAR_REPLACE = "[^0-9]";
    public static final String TIN_REGEX = "^\\d{9}$";
    public static final String ALPHANUM_REGEX = "^[a-zA-Z0-9]+$";
    public static final String SAVING_USER = "Saving User Info in Repository";
    public static final String USER_SAVED = "User Successfully Registered";
    public static final String MAIL_SEND = "Mail send successfully";
    public static final String VERIFICATION_SUCCESS = "Verification Successful";
    public static final String INVALID_EMAIL = "EmailID is not valid";
    public static final String MAILID_ALREADY_PRESENT = "User is already Registered";
    public static final String MAIL_NOT_SENT = "Mail not sent";
    public static final String CSV = ".csv";

    public static final String DOC = ".doc";

    public static final String DOCX = ".docx";

    public static final String PDF = ".pdf";

    public static final String PNG = ".png";

    public static final String JPG = ".jpg";

    public static final String JPEG = ".jpeg";
    public static final String TEXT_CSV = "text/csv";

    public static final String TEXT_DOC = "application/msword";

    public static final String IMG_PNG = "image/png";

    public static final String IMG_JPG = "image/jpg";

    public static final String IMG_JPEG = "image/jpeg";

    public static final String TEXT_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final String XLS = ".xls";
    public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";

    public static final String APPLICATION_VND_PDF = "application/pdf";
    public static final String XLSX = ".xlsx";
    public static final String XLSX_SPREADSHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String LEVEL = "Level";
    public static final String TECHNOLOGY = "Technology";
    public static final String ADDRESS = "Address";
    public static final String CITY = "City";
    public static final String STATE = "State";
    public static final String VISA_STATUS = "Visa Status";
    public static final String LINKEDIN = "LinkedIn";
    public static final String ROLES = "Roles";
    public static final String EXP_YEARS = "Exp Years";
    public static final String RATE_CARD = "Hourly Rate";
    public static final String EXP_MONTHS = "Exp Months";
    public static final String DOMAIN = "Domain";
    public static final String STATUS = "Status";
    public static final String LAST_USED = "Last Used (mmyyyy)";
    public static final String ALTERNATE_PHONE = "Alternate Phone";
    public static final String PHONE_NUMBER = "PhoneNumber";
    public static final String ALTERNATE_EMAIL = "Alternate Email";
    public static final String PRIMARY_EMAIL = "Primary Email";
    public static final String DOB = "DOB";
    public static final String LAST_NAME = "Last Name";
    public static final String FIRST_NAME = "First Name";
    public static final String DRAFT = "Draft";
    public static final int BASE_RATE = 45;
    public static final int MAX_RATE = 500;

    public static final String HOURLY_RATE = "Hourly Rate" ;
}


