package com.rbc.zfe0.road.eod.constants;

public class Constants {


    /* Intraday constants */
    public static String APPNAME = "ROAD";
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

    /*EOD constants   */
// Status related
    public static String STATUS_PENDING = "Pend";
    public static String STATUS_OUT_TO_TRANSFER = "OTT";
    public static String STATUS_CLOSED = "CLOS";

    //close types
    public static String CLOSE_TYPE_CONFISCATED = "CLCF";
    public static String CLOSE_TYPE_ESCHEATED = "CLES";
    public static String CLOSE_TYPE_NORMAL = "CLNC";
    public static String CLOSE_TYPE_REJECTED = "CLRJ";
    public static String CLOSE_TYPE_WORTHLESS = "CLWL";

    public static String DISPOSITION_TX00 = "TX00";
    public static String DISPOSITION_TX25 = "TX25";
    public static String DISPOSITION_TX35 = "TX35";
    public static String DISPOSITION_TX40 = "TX40";
    public static String DISPOSITION_CF00 = "CF00";
    public static String DISPOSITION_CF75 = "CF75";
    public static String DISPOSITION_CF85 = "CF85";
    public static String DISPOSITION_CF90 = "CF90";
    public static String DISPOSITION_UU97 = "UU97";

    // RBC Dain reg info related
    public static String REGISTRATION_INFO_KEY = "DEF ";

    //Error  messages
    public static String CERT_MISSING = "Invalid transit message : Cert Record is missing fron the Transit";
    public static String CERT_CREDIT_DEBIT_ERROR = "Destination Code/Client location is Empty for Cert - ";
    public static String JUMBO_CREDIT_DEBIT_ERROR = "Destination Code/Client location is Empty for Jumbo - ";

    public static String CLIENT_NUM_1 = "0";
    public static String CLIENT_NUM_2 = "006";

    //INTACT file recs.
    public static String EOD_PROPERTY_FILE_NAME = "eod.properties";
    public static String FILE_NOT_FOUND_ERROR = " Properties Configuration File not found";
    public static String CAGE_FILE_NAME = "roadcagedly";
    public static String DDR_FILE_INPUT_NAME = "RBCWM_ALTS_POSNROAD";
    public static String DDR_FILE_OUTPUT_NAME = "ddrFileOutReport";
    public static String ICR_REPORT_FILE_NAME = "roadIcrReport";
    public static String DISPOSITION_REPORT_FILE_NAME = "roadDispositionReport";
    public static String INTACT_FILE_NAME = "roadintactdly";
    public static String INTACT_LOG_FILE_NAME = "roadintactlogdly";
    public static String CONTROL_FILE_NAME = "controlfile.txt";
    public static String FILE_EXTENSION = ".txt";
    public static String INTACT_LOG_FILE_EXTENSION = ".csv";
    public static String SECURITY_TYPE = "A";
    public static String CREDIT_CASH_POSTING_REC = "D1 006000000000XUSD   BBKJ  C      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String DEBIT_CASH_POSTING_REC = "D1 006000000000XUSD   BBKJ  D      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String CREDIT_MEMO_REC = "D1 006000000000XUSD   BMEM  C      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String DEBIT_MEMO_REC = "D1 006000000000XUSD   BMEM  D      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String CREDIT_STOCK_MONEY_REC = "D1 006000000000XUSD   BBKJ  C      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String DEBIT_STOCK_MONEY_REC = "D1 006000000000XUSD   BBKJ  D      00000000000000000DC       #@R1   #@H1 NET00000000000000000USD         #@K1     #@N1 01        #@S1      ";
    public static String INTACT_HEADER = "DATE=060607060607122537   TIF.C006.INTACT  ROAD    006   ";
    public static String INTACT_FOOTER = "REC-CNT=00000000000060607060607122537ROAD    006         ";
    //Intact record type
    public static String INTACT_REC_CREDIT_MEMO = "Credit-Memo";
    public static String INTACT_REC_DEBIT_MEMO = "Debit-Memo";
    public static String INTACT_REC_CREDIT_STOCK_MONEY = "Credit-Stock";
    public static String INTACT_REC_DEBIT_STOCK_MONEY = "Debit-Stock";
    public static String INTACT_REC_CREDIT_CASH_POSTING = "Credit-Cash";
    public static String INTACT_REC_DEBIT_CASH_POSTING = "Debit-Cash";


    public static String AMPERSAND_DECODED = "&amp;";
    public static String GREATER_THAN_DECODED = "&gt;";
    public static String LESS_THAN_DECODED = "&lt;";
    public static String APOSTROPHE_DECODED = "&apos;";
    public static String AMPERSAND = "&";
    public static String GREATER_THAN = ">";
    public static String LESS_THAN = "<";
    public static String APOSTROPHE = "�";

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

    public static final String RECORD_TYPE_MEMO = "Memo";
    public static final String RECORD_TYPE_STOCK_MONEY = "Stock/Money";
    public static final String RECORD_TYPE_CASH = "Cash";

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

    public static final String TRANSFER_TYPE_BOX_LOCATION = "_BOX";
    public static final String TRANSFER_TYPE_ENTRY_CODE = "_ENT";

    public static final String BK_ENTRY_NA = "NA";
    public static final String BK_ENTRY_ORIG_ISSUE_SHORT = "ORI_SHRT";
    public static final String BK_ENTRY_NEW_ISSUE_SHORT = "NEW_SHRT";
    public static final String BK_ENTRY_CLIENT = "CLIENT";
    public static final String BK_ENTRY_CLIENT_SHORT = "C";
    public static final String BK_ENTRY_USER_CHOICE = "CHOICE";
    public static final String BK_ENTRY_ROLLBACK = "ROLLBACK";

    public static final String CHOICE_DEBIT = "Debit";
    public static final String CHOICE_CREDIT = "Credit";
    // Note event codes
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


}