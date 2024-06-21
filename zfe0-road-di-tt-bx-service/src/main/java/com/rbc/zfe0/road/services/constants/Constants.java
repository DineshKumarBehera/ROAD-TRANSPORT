package com.rbc.zfe0.road.services.constants;

public class Constants {
    public static final String Exist = "Exist";
    public static final String NotExist = "NotExist";

    public static final String NOTE_EVENT_ENTRY_GENERATED = "ENGN"; // *When bookkeeping entry is created between states.
    public static final String NOTE_EVENT_ITEM_BLOCKED = "ITBL"; // *When a transfer item is blocked
    public static final String NOTE_EVENT_ITEM_UNBLOCKED = "ITUB"; // *When a transfer item is unblocked
    public static final String NOTE_EVENT_ITEM_CLOSED = "ITCL"; // *When a transfer item is closed
    public static final String NOTE_EVENT_ITEM_CONFIRMED = "ITCN"; // *When user confirms receipt by transfer agent
    public static final String NOTE_EVENT_ITEM_CREATED = "ITCR"; // *When a transfer item is created manually
    public static final String NOTE_EVENT_ITEM_MERGED = "ITMG"; // *When transfer items are merged
    public static final String NOTE_EVENT_ITEM_EDITED = "ITED"; // *When a transfer item is edited in either pending or OTT
    public static final String NOTE_EVENT_ITEM_RECEIVED = "ITRC"; // *When the securities are received back in from the transfer agent
    public static final String NOTE_EVENT_ITEM_SPLIT = "ITSP"; // *When an item is split
    public static final String NOTE_EVENT_ITEM_REDIRECTED = "ITRE"; //    *When a transfer item is redirected
    public static final String NOTE_EVENT_ITEM_SENT_TO_TRANSFER = "ITST"; //    *When a transfer item moves out to transfer
    public static final String NOTE_EVENT_NOTE_ADDED = "NOAD"; //    *When a note is created
    public static final String NOTE_EVENT_NOTIFICATION_SEND = "NOST"; //    When notification is faxed
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

    // Status related
    public static final String STATUS_PENDING = "Pend";
    public static final String STATUS_OUT_TO_TRANSFER = "OTT";
    public static final String STATUS_CLOSED = "CLOS";
    public static final String STATUS_DAILY_ITEMS = "DI";

    // Close type related
    public static final String CLOSE_TYPE_CONFISCATED = "CLCF";
    public static final String CLOSE_TYPE_ESCHEATED = "CLES";
    public static final String CLOSE_TYPE_NORMAL = "CLNC";
    public static final String CLOSE_TYPE_REJECTED = "CLRJ";
    public static final String CLOSE_TYPE_WORTHLESS = "CLWL";
    public static final String CLOSE_TYPE_NOT_NORMAL = "CLNN";
    public static final String CLOSE_ESCHEATED_WORTHLESS_CONFISCATED = "CLES-CLWL-CLCF";
    public static final String REJECTION_TYPE_CODE_INCORRECT_AGENT = "INCORRECT_AGENT";
    public static final String REJECTION_TYPE_CODE_PAPERWORK_MISSING = "PAPERWORK_MISSING";
    public static final String REJECTION_TYPE_CODE_RESETUP_REQUIRED = "RESETUP_REQUIRED";
    public static final String REJECTION_TYPE_CODE_TRANSFER_FEES_MISSING = "TRANSFER_FEES_MISSING";
    public static final String REJECTION_TYPE_CODE_OTHER = "OTHER";


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

    public static final String OTT_ENTRY_1 = "OTT Entry 1";
    public static final String OTT_ENTRY_2 = "OTT Entry 2";
    public static final String OTT_ENTRY_3 = "OTT Entry 3";
    public static final String OTT_ENTRY_4 = "OTT Entry 4";
    public static final String OTT_ENTRY_5 = "OTT Entry 5";
    public static final String OTT_ENTRY_6 = "OTT Entry 6";
    public static final String OTT_ENTRY_7 = "OTT Entry 7";

    public static final String INTO_97_ENTRY_1 = "Into 97 Entry 1";
    public static final String INTO_97_ENTRY_2 = "Into 97 Entry 2";
    public static final String INTO_97_ENTRY_3 = "Into 97 Entry 3";
    public static final String INTO_97_ENTRY_4 = "Into 97 Entry 4";
    public static final String INTO_97_ENTRY_5 = "Into 97 Entry 5";
    public static final String INTO_97_ENTRY_6 = "Into 97 Entry 6";
    public static final String INTO_97_ENTRY_7 = "Into 97 Entry 7";

    public static final String NORMAL_CLOSE_1 = "Normal Close 1";
    public static final String NORMAL_CLOSE_2 = "Normal Close 2";
    public static final String NORMAL_CLOSE_3 = "Normal Close 3";
    public static final String NORMAL_CLOSE_4 = "Normal Close 4";
    public static final String NORMAL_CLOSE_5 = "Normal Close 5";
    public static final String NORMAL_CLOSE_6 = "Normal Close 6";
    public static final String NORMAL_CLOSE_7 = "Normal Close 7";

    public static final String ESCHEATED_CLOSE_1 = "Escheated Close 1";
    public static final String ESCHEATED_CLOSE_2 = "Escheated Close 2";
    public static final String ESCHEATED_CLOSE_3 = "Escheated Close 3";
    public static final String ESCHEATED_CLOSE_4 = "Escheated Close 4";
    public static final String ESCHEATED_CLOSE_5 = "Escheated Close 5";
    public static final String ESCHEATED_CLOSE_6 = "Escheated Close 6";
    public static final String ESCHEATED_CLOSE_7 = "Escheated Close 7";

    public static final String WORTHLESS_CLOSE_1 = "Worthless Close 1";
    public static final String WORTHLESS_CLOSE_2 = "Worthless Close 2";
    public static final String WORTHLESS_CLOSE_3 = "Worthless Close 3";
    public static final String WORTHLESS_CLOSE_4 = "Worthless Close 4";
    public static final String WORTHLESS_CLOSE_5 = "Worthless Close 5";
    public static final String WORTHLESS_CLOSE_6 = "Worthless Close 6";
    public static final String WORTHLESS_CLOSE_7 = "Worthless Close 7";

    public static final String REJECTED_CLOSE_1 = "Rejected Close 1";
    public static final String REJECTED_CLOSE_2 = "Rejected Close 2";
    public static final String REJECTED_CLOSE_3 = "Rejected Close 3";
    public static final String REJECTED_CLOSE_4 = "Rejected Close 4";
    public static final String REJECTED_CLOSE_5 = "Rejected Close 5";
    public static final String REJECTED_CLOSE_6 = "Rejected Close 6";
    public static final String REJECTED_CLOSE_7 = "Rejected Close 7";
    public static final String CONFISCATED_CLOSE_1 = "Confiscated Close 1";
    public static final String CONFISCATED_CLOSE_2 = "Confiscated Close 2";
    public static final String CONFISCATED_CLOSE_3 = "Confiscated Close 3";
    public static final String CONFISCATED_CLOSE_4 = "Confiscated Close 4";
    public static final String CONFISCATED_CLOSE_5 = "Confiscated Close 5";
    public static final String CONFISCATED_CLOSE_6 = "Confiscated Close 6";
    public static final String CONFISCATED_CLOSE_7 = "Confiscated Close 7";

    public static final String RECORD_TYPE = "Record_Type";
    public static final String DEBIT_CREDIT = "Debit_Credit";
    public static final String SH = "SH";
    public static final String CA = "CA";
    public static final String SEC = "SEC";
    public static final String HYPHEN = "-";
    public static final String ENTRY_TYPE_C = "C";

    public static final String C = "C";
    public static final String NA = "NA";
    public static final String ORI_SHRT = "ORI_SHRT";
    public static final String NEW_SHRT = "NEW_SHRT";
    public static final String ROLLBACK = "ROLLBACK";
    public static final String CHOICE = "CHOICE";
    public static final String TRANSFER_TYPE_CASH = "TRANSFER_TYPE_CASH";
    public static final String  TRANSFER_TYPE_SECURITY = "TRANSFER_TYPE_SECURITY";

}