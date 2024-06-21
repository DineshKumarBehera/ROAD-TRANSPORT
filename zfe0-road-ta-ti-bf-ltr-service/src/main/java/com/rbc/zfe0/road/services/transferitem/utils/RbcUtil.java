package com.rbc.zfe0.road.services.transferitem.utils;

import com.rbc.zfe0.road.services.transferitem.entity.NoteHistory;
import com.rbc.zfe0.road.services.transferitem.entity.Notes;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItemHistory;
import com.rbc.zfe0.road.services.transferitem.model.HistoryNotes;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RbcUtil {
    private static final SimpleDateFormat dateFormatterWithOutTime = new SimpleDateFormat("yyyy-MM-dd");

    public static String getMessage(String keyIn) {
        Object[] args = new Object[0];
        return getMessage(keyIn, args);
    }

    private static String getMessage(String keyIn, Object[] args) {
        return getMessage(keyIn, args);
    }

    public static String normalize(String valueIn) {
        return RbcUtil.normalize(valueIn, false);
    }

    public static String normalize(String valueIn, boolean returnNonNull) {
        String retVal = valueIn;

        if (valueIn != null) {
            retVal = StringUtils.replaceChars(valueIn, "- \t", null);
        }

        if (retVal == null && returnNonNull == true) {
            retVal = "";
        }
        return retVal;
    }

    public static String convertDate(Date dateIn) {
        String retVal = "";

        if (dateIn != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            retVal = dateFormatter.format(dateIn);
        }

        return retVal;
    }

    public static BigDecimal insurenceValue(BigDecimal data) {
        if (data != null) {
            BigDecimal rounded = data.setScale(2, RoundingMode.HALF_UP);
            return rounded;
        }
        return null;
    }

    public static String formatAccountNumber(String accountNumberIn, String accountType, String checkDigit) {
        String accountNumber = accountNumberIn;

        if (accountType != null) {
            accountNumber += accountType;
        } else {
            accountNumber += " ";
        }

        if (checkDigit != null) {
            accountNumber += checkDigit;
        } else {
            accountNumber += " ";
        }

        String formattedAccountNumber = accountNumber;

        if (accountNumber != null) {
            if (accountNumber.length() == 12) {
// dain format
                formattedAccountNumber = accountNumber.substring(0, 4) + "-" + accountNumber.substring(4, 8) + "-" + accountNumber.substring(8);
            } else if (accountNumber.length() == 10) {
// adp format
                formattedAccountNumber = accountNumber.substring(0, 3) + "-" + accountNumber.substring(3, 8) + "-" + accountNumber.substring(8, 9) + "-" + accountNumber.substring(9);
            }
        }

        return formattedAccountNumber;
    }

    public static String formatCUSIP(String cusip) {
        String formattedCusip = cusip;

        if (net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils.isNotBlank(cusip) && cusip.length() == Constants.LENGTH_CUSIP) {
            formattedCusip = cusip.substring(0, 6) + "-" + cusip.substring(6, 8) + "-" + cusip.substring(8);
        }

        if (net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils.isNotBlank(cusip) && cusip.length() == Constants.LENGTH_CUSIIP) {
            formattedCusip = cusip.substring(0, 6) + "-" + cusip.substring(6, 9) + "-" + cusip.substring(9);
        }

        if (net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils.isNotBlank(cusip) && cusip.length() <= Constants.LENGTH_CUSIP) {
            String[] splitTimeStamp = formattedCusip.split("");
            Arrays.setAll(splitTimeStamp, index -> splitTimeStamp[index].replaceAll("[\\s\\-()]", ""));
            formattedCusip = cusip.substring(0, 7);
        }

        return formattedCusip;
    }

    public static boolean dateIsToday(Date dateToCheck) {
        boolean retVal = false;

        if (dateToCheck != null) {
            Calendar todayCal = Calendar.getInstance();
            Calendar dateToCheckCal = Calendar.getInstance();
            dateToCheckCal.setTime(dateToCheck);

// must have same mm/dd/yyyy
            if (todayCal.get(Calendar.MONTH) == dateToCheckCal.get(Calendar.MONTH) &&
                    todayCal.get(Calendar.DAY_OF_MONTH) == dateToCheckCal.get(Calendar.DAY_OF_MONTH) &&
                    todayCal.get(Calendar.YEAR) == dateToCheckCal.get(Calendar.YEAR)) {
                retVal = true;
            }
        }
        return retVal;
    }

    public static Notes getNoteForEvent(TransferItem transferItemIn,
                                        String noteEventCodeIn) {
        Notes retVal = null;
        Notes noteTemp = null;

        for (Iterator iter = transferItemIn.getNotes().iterator(); iter.hasNext(); ) {
            noteTemp = (Notes) iter.next();
            if (noteEventCodeIn.equals(noteTemp.getNotesEventCode())) {
                retVal = noteTemp;
                break;
            }
        }

        return retVal;
    }


    public static NoteHistory getNoteForHistoryEvent(TransferItemHistory transferItemIn, String noteEventCodeIn) {
        NoteHistory retVal = null;
        NoteHistory noteTemp = null;
        List<NoteHistory> notesHistories = transferItemIn.getNotesHistories();

        if (notesHistories != null) {
            for (NoteHistory note : notesHistories) {
                if (noteEventCodeIn.equals(note.getEventCode())) {
                    retVal = note;
                    break;
                }
            }
        }

        return retVal;
    }



    public static String getNoteValueForEvent(TransferItem transferItemIn, String noteEventCode, String strip) {
        String retVal = null;

        Notes note = getNoteForEvent(transferItemIn, noteEventCode);

        if (note != null) {
            retVal = note.getNotesTxt();

            if (strip != null) {
                retVal = StringUtils.replace(retVal, strip, "");
            }
        }

        if (retVal != null) {
            retVal = retVal.trim();
        }

        return retVal;
    }

    public static String getNoteValueForHistoryEvent(TransferItemHistory transferItemIn, String noteEventCode, String strip) {
        String retVal = null;

        NoteHistory note = getNoteForHistoryEvent(transferItemIn, noteEventCode);

        if (note != null) {
            retVal = note.getNote();

            if (strip != null) {
                retVal = StringUtils.replace(retVal, strip, "");
            }
        }

        if (retVal != null) {
            retVal = retVal.trim();
        }

        return retVal;
    }
}
