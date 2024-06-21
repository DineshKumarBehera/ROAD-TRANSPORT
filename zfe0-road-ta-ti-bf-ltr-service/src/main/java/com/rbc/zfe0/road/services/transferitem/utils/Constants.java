package com.rbc.zfe0.road.services.transferitem.utils;

public interface Constants {

    // Note event codes
    public static final String NOTE_EVENT_ENTRY_GENERATED = "ENGN"; // *When bookkeeping entry is created between
    // states.
    public static final String NOTE_EVENT_ITEM_BLOCKED = "ITBL"; // *When a transfer item is blocked
    public static final String NOTE_EVENT_ITEM_UNBLOCKED = "ITUB"; // *When a transfer item is unblocked
    public static final String NOTE_EVENT_ITEM_CLOSED = "ITCL"; // *When a transfer item is closed
    public static final String NOTE_EVENT_ITEM_CONFIRMED = "ITCN"; // *When user confirms receipt by transfer agent
    public static final String NOTE_EVENT_ITEM_CREATED = "ITCR"; // *When a transfer item is created manually
    public static final String NOTE_EVENT_ITEM_MERGED = "ITMG"; // *When transfer items are merged
    public static final String NOTE_EVENT_ITEM_EDITED = "ITED"; // *When a transfer item is edited in either pending or
    // OTT
    public static final String NOTE_EVENT_ITEM_RECEIVED = "ITRC"; // *When the securities are received back in from the
    // transfer agent
    public static final String NOTE_EVENT_ITEM_SPLIT = "ITSP"; // *When an item is split
    public static final String NOTE_EVENT_ITEM_REDIRECTED = "ITRE"; // *When a transfer item is redirected
    public static final String NOTE_EVENT_ITEM_SENT_TO_TRANSFER = "ITST"; // *When a transfer item moves out to transfer
    public static final String NOTE_EVENT_NOTE_ADDED = "NOAD"; // *When a note is created
    public static final String NOTE_EVENT_NOTIFICATION_SEND = "NOST"; // When notification is faxed
    public static final String NOTE_EVENT_DATA_CONVERSION = "CONV"; // data conversion notes
    public static final String NOTE_EVENT_DATA_CONVERSION_ERROR = "FAIL";
    public static final String NOTE_EVENT_VOLUNTARY_OFFER_ERROR = "VOLU";
    public static final String NOTE_EVENT_CLIENT_NAME_ADDRESS = "CLNA"; // *client name and address added during close
    public static final String NOTE_EVENT_CONFISCATED = "CONF"; // *item confiscated during close
    public static final String NOTE_EVENT_REJECT_TYPE = "REJT"; // *item rejection type
    public static final String NOTE_EVENT_REJECT_REASON = "REJR"; // *item reject reason
    public static final String NOTE_EVENT_ESCHEATED_STATE = "ESST"; // *escheated state
    public static final String LETTER_OF_TRANSMITTAL = "letterOfTransmittal";
    public static final String NOTE_EVENT_ESCHEATED_TREASURER = "ESTR"; // *escheated treasurer
    public static final String NOTE_EVENT_LONG_CHECK_NBR = "CKNB"; // *long check number entered into notes
    public static final String NOTE_EVENT_DISPOSITION_CHANGE = "DISP"; // disposition changed.

    public static final String ATTR_EDIT_MODE = "editMode";

    // Report types
    public static final String OTT_MAILING_SUMMARY_RPT = "ottMailSum";
    public static final String CLOSE_OUT_ACTIVITY_RPT = "closeOutActivity";
    public static final String TRANSFER_ITEM_SUMMARY_RPT = "transferItemSummary";

    public static final String TRANSFER_TYPE_BOX_LOCATION = "_BOX";
    public static final String TRANSFER_TYPE_ENTRY_CODE = "_ENT";

    public static final String TRANSFER_TYPE_TRAN_CODE_CASH = "1";
    public static final String TRANSFER_TYPE_TRAN_CODE_SECURITY = "2";
    public static final String TRANSFER_TYPE_TRAN_CODE_CASH_AND_SECURITY = "3";
    public static final String TRANSFER_TYPE_CODE_SUFFIX_CASH = "CA";
    public static final String TRANSFER_TYPE_CODE_SUFFIX_SECURITY = "SE";
    public static final String TRANSFER_TYPE_CODE_SUFFIX_CASH_AND_SECURITY = "CS";
    public static final String ISSUE_TYPE_CASH = "C";
    public static final String ISSUE_TYPE_SECURITY = "S";

    // Misc
    public static final String UNKNOWN = "Unknown";

    // page/request/session Attributes
    String ATTR_STATUS = "filter.status";

    String PATH_RESOURCES = "/WEB-INF/resources/";

    String ROLE_ADMIN = "ROLE_ROAD_ADMIN";
    String ROLE_GUEST = "ROLE_ROAD_GUEST";
    String ROLE_ASSOCIATE = "ROLE_ROAD_ASSOC";
    String ROLE_TL = "ROLE_ROAD_TL";

    public static final String ROAD_ADMIN = "roadadmin";
    public static final String ROAD_ASSOC = "roadassoc";
    public static final String ROAD_GUEST = "roadguest";
    public static final String ROAD_TEAMLEAD = "roadtl";

    // request parameter related...
    String PARAM_ID = "id";
    String PARAM_CODE = "code";
    String PARAM_PRINT = "print";

    String PARAM_TAX_ID_TYPE = "taxIdType";
    String PARAM_SSN = "SSN";
    String PARAM_EIN = "EIN";
    String PARAM_LAST_UPDATE_DATE = "lastUpdDate";
    String PARAM_HISTORY = "history";
    String PARAM_PAGE = "page";
    String PARAM_SORT = "sort";
    String PARAM_SORT_DIRECTION = "sortDir";
    String PARAM_STATUS = "status";
    String PARAM_ADVANCED_SEARCH_IND = "advancedSearchInd";
    String PARAM_UNLOCK_4_ADMIN = "unlock4admin";
    String PARAM_UNLOCK_4_ALL = "unlock4all";

    String PAGE_ZERO = "0";
    public static final String ENVIRONMENT_STAGE = "SIT";
    public static final String ENVIRONMENT_QA = "QA";
    public static final String ENVIRONMENT_DEV = "DEV";
    public static final String ACTION_DELETE = "delete";

    String ATTR_MENUS = "menus";
    String RBC_ERRORS = "rbcErrors";
    String ATTR_UNLOCKED = "unlocked";

    // length related
    int LENGTH_ACCOUNT_NUMBER = 10;
    int LENGTH_ACCOUNT_TAX_ID = 9;
    int LENGTH_CUSIP = 9;
    int LENGTH_CUSIIP = 9;
    int LENGTH_SECURITY_NUMBER = 7;
    int LENGTH_FORMATTED_SSN = 11;
    int LENGTH_FORMATTED_EIN = 10;
    int LENGTH_PHONE_NUMBER = 10;
    int LENGTH_CONTROL_ID = 16;
    // logon related:
    String ERROR_AUTHENTICATIONSERVICE_EXCEPTION = "error.authenticationservice";
    public static final String ATTR_CAN_EDIT = "canEdit";

    // Status related
    String STATUS_PENDING = "Pend";
    String STATUS_OUT_TO_TRANSFER = "OTT";
    String STATUS_CLOSED = "CLOS";
    String STATUS_DAILY_ITEMS = "DI"; // Overloading the Status field. For daily entries, we need
    // to get items that may be Pending, OTT, or Closed.

    String LABEL_STATUS = "label.status";
    String LABEL_STATUS_PENDING = "label.status.pending";
    String LABEL_STATUS_OUT_TO_TRANSFER = "label.status.outToTransfer";
    String LABEL_STATUS_CLOSED = "label.status.closed";
    String LABEL_STATUS_DAILY_ITEMS = "label.status.dailyItems";

    // RBC Dain reg info related
    String REGISTRATION_INFO_KEY = "DEF ";

    String DISPOSITION_TX00 = "TX00";
    String DISPOSITION_TX25 = "TX25";
    String DISPOSITION_TX35 = "TX35";
    String DISPOSITION_TX40 = "TX40";
    String DISPOSITION_CF00 = "CF00";
    String DISPOSITION_CF75 = "CF75";
    String DISPOSITION_CF85 = "CF85";
    String DISPOSITION_CF90 = "CF90";
    String DISPOSITION_UU97 = "UU97";

    String LABEL_DISPOSITION = "label.dispositionCode";

    // Close type related
    String CLOSE_TYPE_CONFISCATED = "CLCF";
    String CLOSE_TYPE_ESCHEATED = "CLES";
    String CLOSE_TYPE_NORMAL = "CLNC";
    String CLOSE_TYPE_REJECTED = "CLRJ";
    String CLOSE_TYPE_WORTHLESS = "CLWL";
    String CLOSE_TYPE_NOT_NORMAL = "CLNN";
    String CLOSE_ESCHEATED_WORTHLESS_CONFISCATED = "CLES-CLWL-CLCF";
    String REJECTION_TYPE_CODE_INCORRECT_AGENT = "INCORRECT_AGENT";
    String REJECTION_TYPE_CODE_PAPERWORK_MISSING = "PAPERWORK_MISSING";
    String REJECTION_TYPE_CODE_RESETUP_REQUIRED = "RESETUP_REQUIRED";
    String REJECTION_TYPE_CODE_TRANSFER_FEES_MISSING = "TRANSFER_FEES_MISSING";
    String REJECTION_TYPE_CODE_OTHER = "OTHER";

    String REJECTION_TYPE_LBL_INCORRECT_AGENT = "Incorrect transfer agent";
    String REJECTION_TYPE_LBL_PAPERWORK_MISSING = "Paperwork missing";
    String REJECTION_TYPE_LBL_RESETUP_REQUIRED = "Re-setup required";
    String REJECTION_TYPE_LBL_TRANSFER_FEES_MISSING = "Transfer fees missing";
    String REJECTION_TYPE_LBL_OTHER = "Other";

    String LABEL_CLOSE_TYPE = "label.closeType";
    String LABEL_CLOSE_DATE = "label.closeDate";
    String LABEL_CLOSE_TYPE_NORMAL = "label.closeType.Normal";
    String LABEL_CLOSE_TYPE_NOT_NORMAL = "label.closeType.NotNormal";
    String LABEL_CLOSE_TYPE_ESCHEATED = "label.closeType.Escheated";
    String LABEL_CLOSE_TYPE_CONFISCATED = "label.closeType.Confiscated";
    String LABEL_CLOSE_TYPE_WORTHLESS = "label.closeType.Worthless";
    String LABEL_CLOSE_TYPE_REJECTED = "label.closeType.Rejected";

    String LABEL_ACCOUNT_NUMBER = "label.accountNumber";
    String ACCOUNT_NUMBER = "accountNumber";

    String LABEL_ADP_SECURITY_NUMBER = "label.adpSecurityNumber";
    String LABEL_ORIG_ADP_SECURITY_NUMBER = "label.origAdpSecurityNumber";

    String ADP_SECURITY_NUMBER = "adpSecurityNumber";

    String LABEL_DAIN_SECURITY_NUMBER = "label.dainSecurityNumber";
    String DAIN_SECURITY_NUMBER = "dainSecurityNumber";

    String LABEL_RECEIVED_DATE = "label.receivedDate";

    String LABEL_TRANSFERRED_DATE = "label.transferredDate";

    String LABEL_TRANSFER_TYPE = "label.transferType";

    String LABEL_CONFIRM_RECD_DATE = "label.confirmationReceivedDate";

    String VALIDATOR_ERR_NOTE = "error.note.empty";

    String VALIDATOR_ERR_PSWDCHG_MISSING_NEW = "error.passwordchange.missingnew";
    String VALIDATOR_ERR_PSWDCHG_MISSING_VERIFY = "error.passwordchange.missingverify";
    String VALIDATOR_ERR_PSWDCHG_CURRENT = "error.passwordchange.currentwrong";
    String VALIDATOR_ERR_PSWDCHG_VERIFY = "error.passwordchange.nomatchnew";

    // Transfer Agent related
    String VALIDATOR_ERR_TRANSFER_AGENT_NAME = "error.transferAgent.missingName";
    String VALIDATOR_ERR_TRANSFER_AGENT_ADDRESS = "error.transferAgent.missingAddress";
    String VALIDATOR_ERR_TRANSFER_AGENT_PHONE = "error.transferAgent.missingPhone";
    String VALIDATOR_ERR_TRANSFER_AGENT_FAX = "error.transferAgent.missingFax";
    String VALIDATOR_ERR_TRANSFER_AGENT_FEE = "error.transferAgent.missingFee";
    String VALIDATOR_ERR_TRANSFER_AGENT_ID_NOT_FOUND = "error.transferAgent.idNotFound";
    String VALIDATOR_ERR_TRANSFER_AGENT_NAME_NOT_FOUND = "error.transferAgent.nameNotFound";
    String VALIDATOR_ERR_TRANSFER_AGENT_MULTIPLE_FOUND = "error.transferAgent.multipleFound";

    // Delivery Instruction Related
    String VALIDATOR_ERR_DELIV_INSTR_TYPE = "error.deliveryInstruction.missingType";
    String VALIDATOR_ERR_DELIV_INSTR_INSTRUCTION = "error.deliveryInstruction.missingInstruction";

    // Transfer Type Related
    String VALIDATOR_ERR_TRANSFER_TYPE_TYPE = "error.transferType.missingType";
    String VALIDATOR_ERR_TRANSFER_TYPE_CODE = "error.transferType.missingCode";

    String VALIDATOR_ERR_REGISTRATION_DATA_MISSING = "error.registrationInformation.missingData";
    String VALIDATOR_ERR_REGISTRATION_DETAILS_MISSING = "error.registrationInformation.missingDetails";

    String MSG_TRANSFER_AGENT_ASSOC_DELETED = "msg.transferAgent.associationDeleted";
    String MSG_TRANSFER_AGENT_ASSOC_NOT_FOUND = "msg.transferAgent.notFoundSecNum";

    // Transfer Item Edit related
    String VALIDATOR_ERR_TRANSFER_TYPE_MISSING = "error.transferItem.transferType.missing";
    String VALIDATOR_ERR_TRANSFER_AGENT_MISSING = "error.transferItem.transferAgent.missing";
    String VALIDATOR_ERR_DELIVERY_INSTR_MISSING = "error.transferItem.deliveryInstructions.missing";
    String VALIDATOR_ERR_CERTIFICATE_INFO_MISSING = "error.transferItem.certificateInfo.missing";

    String VALIDATOR_ERR_NEW_ISSUES_MISSING = "error.transferItem.newIssues.missing";

    // Transfer Item Audit related
    String VALIDATOR_ERR_TRANSFERITEM_ID_NUMERIC = "error.transferItemId.numeric";

    // Transfer Item Add related
    String ERROR_TRANSFER_ITEM_ALT_BRANCH_MISSING = "error.transferItem.altBranch.missing";
    String ERROR_TRANSFER_ITEM_ACCOUNT_NUM_MISSING = "error.transferItem.accountNumber.missing";
    String ERROR_TRANSFER_ITEM_REP_ID_MISSING = "error.transferItem.repId.missing";
    String ERROR_TRANSFER_ITEM_UNKNOWN = "error.transferItem.unknown";
    String ERROR_TRANSFER_ITEM_ORIG_ISSUE_SEC_DESC_MISSING = "error.transferItem.origIssueSecDesc.missing";
    String ERROR_TRANSFER_ITEM_ORIG_ISSUE_QUANTITY_MISSING = "error.transferItem.origIssueQuantity.missing";
    String ERROR_TRANSFER_ITEM_ORIG_ISSUE_DEBIT_MISSING = "error.transferItem.origIssueDebit.missing";
    String ERROR_TRANSFER_ITEM_ORIG_ISSUE_CREDIT_MISSING = "error.transferItem.origIssueCredit.missing";

    String MSG_TRANSFER_ITEM_SPLIT_BAD_QUANTITY = "error.transferItemSplit.badQuantity";

    // Transfer Item - Advanced Search related
    String VALIDATOR_BAD_SEARCH_CRITERIA = "error.transferItem.search.bad";
    String VALIDATOR_SEARCH_CRITERIA_NA = "error.transferItem.search.na";

    // New Cash/Security Issue related
    String VALIDATOR_ERR_CASH_ISSUE_AMT_REQ_MISSING = "error.cashIssue.missingAmountRequested";
    String VALIDATOR_ERR_CASH_ISSUE_AMT_REC_MISSING = "error.cashIssue.missingAmountReceived";

    String VALIDATOR_ERR_SECURITY_ISSUE_INS_VAL_MISSING = "error.securityIssue.missingInsuranceValue";

    String VALIDATOR_ERR_CLOSED_TYPE_REQUIRED = "error.closedType.required";

    // Dollar amount related
    String VALIDATOR_ERR_DOLLAR_AMOUNT_MISSING = "error.dollarValue.missing";
    String VALIDATOR_ERR_DOLLAR_AMOUNT_SCALE = "error.dollarValue.maxScale";
    String VALIDATOR_ERR_DOLLAR_AMOUNT_TOO_BIG = "error.dollarValue.maxSize";

    // Quantity related
    String VALIDATOR_ERR_QUANTITY_MISSING = "error.quantity.missing";
    String VALIDATOR_ERR_QUANTITY_SCALE = "error.quantity.maxScale";
    String VALIDATOR_ERR_QUANTITY_TOO_BIG = "error.quantity.maxSize";

    // CUSIP related
    String VALIDATOR_ERR_CUSIP_LENGTH = "error.cusip.length";
    String VALIDATOR_ERR_CUSIP_ALPHANUMERIC = "error.cusip.alphanumeric";
    String VALIDATOR_ERR_CUSIP_MISSING = "error.cusip.missing";

    // Security Number related
    String VALIDATOR_ERR_SECURITY_NUMBER_LENGTH = "error.adpSecurityNumber.length";
    String VALIDATOR_ERR_SECURITY_NUMBER_ALPHANUMERIC = "error.securityNumber.alphanumeric";
    String VALIDATOR_ERR_SECURITY_NUMBER_MISSING = "error.securityNumber.missing";

    // controlId related
    String VALIDATOR_ERR_CONTROLID_LENGTH = "error.controlId.length";
    String VALIDATOR_ERR_CONTROLID_ALPHANUMERIC = "error.controlId.alphanumeric";

    // Account Number related
    String VALIDATOR_ERR_ACCOUNT_NUMBER_LENGTH = "error.accountNumber.length";
    String VALIDATOR_ERR_ACCOUNT_NUMBER_LENGTH_RBC = "error.accountNumber.length.RBC";
    String VALIDATOR_ERR_ACCOUNT_NUMBER_ALPHANUMERIC = "error.accountNumber.alphanumeric";

    // Tax ID related
    String VALIDATOR_ERR_TAX_ID_LENGTH = "error.taxId.length";
    String VALIDATOR_ERR_TAX_ID_NUMERIC = "error.taxId.numeric";
    String VALIDATOR_ERR_TAX_ID_TYPE_MISSING = "error.taxId.type.missing";
    String VALIDATOR_ERR_TAX_ID_VALUE_MISSING = "error.taxId.value.missing";

    // Phone/Fax Number related
    String VALIDATOR_ERR_PHONE_NUMBER_LENGTH = "error.phoneNumber.length";
    String VALIDATOR_ERR_PHONE_NUMBER_EXT_LENGTH = "error.phoneNumber.extension.length";
    String VALIDATOR_ERR_PHONE_NUMBER_NUMERIC = "error.phoneNumber.numeric";

    // bookkeeping entry related
    String VALIDATOR_ERR_ENTRY_ENTRY_TYPE_MISSING = "error.bookkeepingEntry.entryTypeMissing";

    // History related
    String ERROR_HISTORY_IMMUTABLE = "error.history.immutable";
    String LABEL_LAST_MODIFIED_DATE = "Last Modified";
    String LABEL_LAST_MODIFIED_BY = "Last Modified By";
    String LABEL_TRANSFER_AGENT = "Transfer Agent";
    String LABEL_TAX_ID = "Tax Id";
    String LABEL_OLD_DESCRIPTION = "Orig Desc";
    String LABEL_NEW_DESCRIPTION = "New Desc";
    String LABEL_OLD_CUSIP = "Orig CUSIP";
    String LABEL_NEW_CUSIP = "New CUSIP";
    String LABEL_OLD_CREDIT_SHORT = "Orig short";
    String LABEL_NEW_CREDIT_SHORT = "New short";
    String LABEL_OLD_CREDIT_LONG = "Orig Long";
    String LABEL_NEW_CREDIT_LONG = "New Long";
    String LABEL_OLD_QUANTITY = "Orig Qty";
    String LABEL_NEW_QUANTITY = "New Qty";
    String LABEL_OLD_VALUE = "Orig Value";
    String LABEL_NEW_VALUE = "New Value";
    String ENTRY_CODE_PH = "PH";

    /* Intraday constants */
    public static int JUMBO_RECORD_LENGTH = 77;
    public static int CERT_RECORD_LENGTH = 133;
    public static int CERT_RECORD_TOTAL_LENGTH = 153;
    public static int OFFSET_START = 30;
    public static int TSND_CERT_COUNT = 20;
    public static int TSND_JUMBO_COUNT = 10;
    public static int CERT_START_POS = 129;
    public static int TSND_JUMBO_START_POS = 3191;
    public static String DETAIL_MESSAGE_CODE = "01";
    public static String CERT_MESSAGE_CODE = "07";
    public static String JUMBO_MESSAGE_CODE = "08";

    /* EOD constants */
    // Status related
    // RBC Dain reg info related
    public static String TRANSIT_DUMMY_ACCOUNT_NUMBER = "30192064";
    public static String TRANSIT_DUMMY_BRANCH = "301";
    public static String TRANSIT_DAIN_CLIENT_ACCOUNT_NUMBER = "09700023";
    public static String TRANSIT_DAIN_CLIENT_ACCOUNT_TYPE = "1";
    public static String TRANSIT_DAIN_CLIENT_ACCOUNT_CHECK_DIGIT = "7";

    // Error messages
    public static String CERT_MISSING = "Invalid transit message : Cert Record is missing fron the Transit";
    public static String CERT_CREDIT_DEBIT_ERROR = "Destination Code/Client location is Empty for Cert - ";
    public static String JUMBO_CREDIT_DEBIT_ERROR = "Destination Code/Client location is Empty for Jumbo - ";

    // database JNDI NAMES
    public static String MF_DATA_SOURCE_JNDI = "jdbc/ROADQAMF";
    // queue configurations
    public static String TRANSIT_QUEUE_CONNECTION_FACTORY_NAME = "jms/QCF";
    public static String TRANSIT_DEAD_QUEUE_JNDI_NAME = "jms/transitInputDeadq";

    // CAGE File Constants
    public static String CAGE_CLIENT_ACCOUNT_NUMBER = "09700021";

    // INTACT file recs.
    public static String CAGE_FILE_LOCATION = "/usr/dainapps/road/eodfiles/cage";
    public static String INTACT_FILE_LOCATION = "/usr/dainapps/road/eodfiles/intact";
    public static String INTACT_LOG_FILE_LOCATION = "/usr/dainapps/road/eodfiles/intactLog";
    public static String INTACT_LOG_FILE_EXTENSION = ".csv";
    public static String SECURITY_TYPE = "A";
    public static String CREDIT_CASH_POSTING_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";

    public static String DEBIT_CASH_POSTING_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";
    public static String CREDIT_MEMO_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";
    public static String DEBIT_MEMO_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";
    public static String CREDIT_STOCK_MONEY_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";
    public static String DEBIT_STOCK_MONEY_REC = "D1 006000000000XUSD BBKJ C00000000000000000 DC #@R1#@H1 NET00000000000000000USD #@K101#@S1";

    // Intact record type
    public static String INTACT_REC_CREDIT_MEMO = "Credit-Memo";
    public static String INTACT_REC_DEBIT_MEMO = "Debit-Memo";
    public static String INTACT_REC_CREDIT_STOCK_MONEY = "Credit_Stock_Money";
    public static String INTACT_REC_DEBIT_STOCK_MONEY = "Debit_Stock_Money";
    public static String INTACT_OTT_UPDATE_BATCH_CODE = "2P";
    public static String INTACT_WASH_ACCOUNT_NUMBER = "01800400";

    public static String USER_SYSTEM = "SYSTEM";

    public static final String CHECK_NUMBER_IN_NOTES = "See notes.";
    public static final int MAX_CHECK_NBR_LENGTH = 10;
    public static final String MODE_VIEW = "view";
    public static final String MODE_ADVANCED_SEARCH = "advSrch";

    public static final String SORT_RECEIVED_DATE = "receivedDate";
    public static final String SORT_DIRECTION_DESC = "DESC";
    public static final String SORT_DIRECTION_ASC = "ASC";

    public static final int ERROR_CODE_UNKNOWN = 20000;

    public static final String COL_TRANSFER_ITEM_ID = "transferItemId";
    public static final String COL_RECEIVED_DATE = "receivedDate";
    public static final String COL_TRANSFERRED_DATE = "transferredDate";
    public static final String COL_CLOSED_DATE = "closeDate";
    public static final String COL_ADP_ACCOUNT_NBR = "adpAccountNumber";
    public static final String COL_ADP_SECURITY_NBR = "adpSecurityNumber";
    public static final String COL_CUSIP = "cusip";
    public static final String COL_QTY = "quantity";
    public static final String COL_SECURITY_DESC = "securityDescription";
    public static final String COL_VALUE = "value";
    public static final String COL_LAST_MODIFIED_DATE = "lastUpdateDate";
    public static final String COL_LAST_MODIFIED_BY = "lastUpdateName";

    public static final String TOTAL_OTT_COUNT = "totalOttCount";

    public static final String RECORD_TYPE_MEMO = "Memo";
    public static final String RECORD_TYPE_STOCK_MONEY = "Stock/Money";
    public static final String RECORD_TYPE_CASH = "Cash";

    public static final String CHOICE_DEBIT = "Debit";
    public static final String CHOICE_CREDIT = "Credit";

    public static final String BATCH_CODE_2D = "2D";
    public static final String BATCH_CODE_2G = "2G";
    public static final String BATCH_CODE_2H = "2H";
    public static final String BATCH_CODE_2J = "2J";
    public static final String BATCH_CODE_2Q = "2Q";
    public static final String BATCH_CODE_2P = "2P";
    public static final String BATCH_CODE_2R = "2R";
    public static final String BATCH_CODE_2U = "2U";
    public static final String BATCH_CODE_2W = "2W";
    public static final String BATCH_CODE_2Y = "2Y";
    public static final String BATCH_CODE_3D = "3D";
    public static final String BATCH_CODE_3P = "3P";
    public static final String BATCH_CODE_3Q = "3Q";
    public static final String BATCH_CODE_3R = "3R";
    public static final String BATCH_CODE_3U = "3U";
    public static final String BATCH_CODE_3W = "3W";
    public static final String BATCH_CODE_3Y = "3Y";
    public static final String BATCH_CODE_CK = "CK";
    public static final String BATCH_CODE_PH = "PH";
    public static final String BATCH_CODE_TG = "TG";
    // Adding the two more batchCodes 2M and 3M start
    public static final String BATCH_CODE_2M = "2M";
    public static final String BATCH_CODE_3M = "3M";
    // Adding the two more batchCodes 2M and 3M end

    // Adding the two more batchCodes 2I and 3I start
    public static final String BATCH_CODE_2I = "2I";
    public static final String BATCH_CODE_3I = "3I";
    // Adding the two more batchCodes 2I and 3I end

    public static final String ENTRY_CODE_CGC = "CGC";
    public static final String ENTRY_CODE_CGM = "CGM";
    public static final String ENTRY_CODE_CSH = "CSH";
    public static final String ENTRY_CODE_DEL = "DEL";
    public static final String ENTRY_CODE_DPD = "DPD";
    public static final String ENTRY_CODE_DTC = "DTC";
    public static final String ENTRY_CODE_MKR = "MKR";
    public static final String ENTRY_CODE_RDP = "RDP";
    public static final String ENTRY_CODE_SLG = "SLG";
    public static final String ENTRY_CODE_SPW = "SPW";
    public static final String ENTRY_CODE_SRQ = "SRQ";
    public static final String ENTRY_CODE_SSB = "SSB";
    public static final String ENTRY_CODE_SSD = "SSD";
    public static final String ENTRY_CODE_SSG = "SSG";
    public static final String ENTRY_CODE_SSH = "SSH";
    public static final String ENTRY_CODE_SSJ = "SSJ";
    public static final String ENTRY_CODE_SSK = "SSK";
    public static final String ENTRY_CODE_SSN = "SSN";
    public static final String ENTRY_CODE_SSO = "SSO";
    public static final String ENTRY_CODE_SSQ = "SSQ";
    public static final String ENTRY_CODE_SSV = "SSV";
    public static final String ENTRY_CODE_SSW = "SSW";
    public static final String ENTRY_CODE_STD = "STD";
    public static final String ENTRY_CODE_SUR = "SUR";
    public static final String ENTRY_CODE_SUU = "SUU";
    public static final String ENTRY_CODE_USER_CHOICE_LABLE = "User Choice";

    public static final String QTY_CHOICE_OI = "OI";
    public static final String QTY_CHOICE_NI = "NI";

    public static final String BK_ENTRY_NA = "NA";
    public static final String BK_ENTRY_ORIG_ISSUE_SHORT = "ORI_SHRT";
    public static final String BK_ENTRY_NEW_ISSUE_SHORT = "NEW_SHRT";
    public static final String BK_ENTRY_CLIENT = "CLIENT";
    public static final String BK_ENTRY_CLIENT_SHORT = "C";
    public static final String BK_ENTRY_USER_CHOICE = "CHOICE";
    public static final String BK_ENTRY_ROLLBACK = "ROLLBACK";

    public static final String BATCH_ENTRY_BOX_LOCATION = "BOX-LOCATION";
    public static final String BATCH_ENTRY_TYPE_CLIENT = "CLIENT";
    public static final String BATCH_ENTRY_TYPE_CLIENT_SHORT = "C";
    public static final String BATCH_ENTRY_DUMMY = "DUMMY";
    public static final String BATCH_ENTRY_USER_CHOICE_NOT_CLIENT_VALUE = "_UCN";
    public static final String BATCH_ENTRY_USER_CHOICE_VALUE = "_USR";
    public static final String BATCH_ENTRY_LOOKUP = "_LKP";
    public static final String BATCH_ENTRY_MEMO_TYPE_MEMO = "Memo";
    public static final String BATCH_ENTRY_MEMO_TYPE_CASH = "Cash";
    public static final String BATCH_ENTRY_MEMO_TYPE_STOCK_MONEY = "Stock";

    public static final String ACCT_NUMBER_BOX_97 = "09000097";
    public static final String ACCT_TYPE_BOX_97 = "1";
    public static final String ACCT_CHECK_DIGIT_BOX_97 = "3";

    public static final String CASH_NI_NO_SHARES = "CASH-NI-NO-SHARES";
    public static final String SHARES_NI = "SHARES-NI";
    public static final String SHARES_OI = "SHARES-OI";
    public static final String SHARES_OI_CASH_NI = "SHARES-OI-CASH-NI";
    public static final String SHARES_OI_CASH_OI = "SHARES-OI-CASH-OI";
    public static final String SHARES_NI_CASH_NI = "SHARES-NI-CASH-NI";
    public static final String SHARES_CASH_NI_NO_SECURITY = "CASH-NI-NO-SECURITY";
    public static final String SHARES_CASH_OI_NO_SECURITY = "SHARES-CASH-OI-NO-SECURITY";

    public static final String SHARES_CHOICE_NA = "NA";
    public static final String SHARES_CHOICE_NEW_ISSUE = "NI";
    public static final String SHARES_CHOICE_ORIG_ISSUE = "OI";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ERROR_TRANSFER_ITEM_CONTROLID_MISSING = "error.transferItem.controlId.missing";

    public static final String NOTE_EVENT_ADDED = "NOTE";

}
