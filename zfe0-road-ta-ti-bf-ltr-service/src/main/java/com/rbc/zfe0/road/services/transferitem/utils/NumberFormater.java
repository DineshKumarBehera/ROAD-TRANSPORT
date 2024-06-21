package com.rbc.zfe0.road.services.transferitem.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

/**
 * @author Chris Alme
 * @author Tim Tracy
 */
public class NumberFormater {

    private NumberFormater() {
        // no instances
    }

    /**
     * Format a date for sorting purposes
     *
     */
    public static String formatDateForSort(Date dateIn) {
        String retVal = "";

        if (dateIn != null) {
            DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHssSSS");

            retVal = dateFormatter.format(dateIn);
        }

        return retVal;
    }

    /**
     * Formats a string to represent a phone number:
     * <ul>
     * <li>###-###-####</li>
     * </ul>
     *
     * If <code>phoneNumber</code> is <code>null</code>, <code>null</code> is
     * quietly returned. If the length of <code>phoneNumber</code> is greater than
     * {@link Constants#LENGTH_PHONE_NUMBER}, only the first 10 characters will be
     * returned. If the length of <code>phoneNumber</code> is less than
     * {@link Constants#LENGTH_PHONE_NUMBER}, <code>phoneNumber</code> is quietly
     * returned.
     *
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {

        if ((phoneNumber == null) || phoneNumber.equalsIgnoreCase("null")) {
            return "";
        }

        if (phoneNumber.length() < Constants.LENGTH_PHONE_NUMBER) {
            return phoneNumber;
        }

        String formatted = phoneNumber;
        if (phoneNumber.length() > Constants.LENGTH_PHONE_NUMBER) {
            formatted = phoneNumber.substring(0, Constants.LENGTH_PHONE_NUMBER);
        }

        return formatted.substring(0, 3) + "-" + formatted.substring(3, 6) + "-" + formatted.substring(6);
    }

    /**
     * Formats a string to represent an Employer Identification Number:
     * <ul>
     * <li>##-#######</li>
     * </ul>
     *
     * If the length of <code>ein</code> is not equal to
     * {@link Constants#LENGTH_ACCOUNT_TAX_ID}, no formatting is attempted and
     * <code>ein</code> is quietly returned.
     *
     * @param ein the unformatted Employer Identification Number.
     *
     * @return the formatted Employer Identification Number.
     */
    public static String formatEIN(String ein) {
        String formattedEin = ein;

        if (ein != null && ein.length() == Constants.LENGTH_ACCOUNT_TAX_ID) {
            formattedEin = ein.substring(0, 2) + "-" + ein.substring(2);
        }

        return formattedEin;
    }

    /**
     * Formats a string to represent a Social Security Number:
     * <ul>
     * <li>###-##-#####</li>
     * </ul>
     *
     * If the length of <code>ssn</code> is not equal to
     * {@link Constants#LENGTH_ACCOUNT_TAX_ID}, no formatting is attempted and
     * <code>ssn</code> is quietly returned.
     *
     * @param ssn the unformatted Social Security Number.
     *
     * @return the formatted Social Security Number.
     */
    public static String formatSSN(String ssn) {
        String formattedSsn = ssn;

        if (ssn != null && ssn.length() == Constants.LENGTH_ACCOUNT_TAX_ID) {
            formattedSsn = ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5);
        }

        return formattedSsn;
    }
    /**
     * Formats a string to represent a CUSIP:
     * <ul>
     * <li>######-##-#</li>
     * </ul>
     *
     * If the length of <code>cusip</code> is not equal to
     * {@link Constants#LENGTH_CUSIP}, no formatting is attempted and
     * <code>cusip</code> is quietly returned.
     *
     * @param cusip the unformatted CUSIP.
     *
     * @return the formatted CUSIP.
     */
    public static String formatCusip(String cusip) {
        String formattedCusip = cusip;

        if (StringUtils.isNotBlank(cusip) && cusip.length() == Constants.LENGTH_CUSIP) {
            formattedCusip = cusip.substring(0, 6) + "-" + cusip.substring(6, 8) + "-" + cusip.substring(8);
        }

        return formattedCusip;
    }

    /**
     * Formats a string to represent an Account Number:
     * <ul>
     * <li>ADP Format: ###-#####-#-#</li>
     * <li>Dain Format: ####-####-####</li>
     * </ul>
     *
     * If the length of <code>accountNumber</code> is not equal to
     * {@link Constants#LENGTH_ACCOUNT_NUMBER}, no formatting is attempted and
     * <code>accountNumber</code> is quietly returned.
     *
     * @param accountNumber the unformatted Account Number.
     *
     * @return the formatted Account Number.
     */
    public static String formatAccountNumber(String accountNumber) {
        String formattedAccountNumber = StringUtils.replace(accountNumber, "-", null);

        if (accountNumber != null) {
            if (accountNumber.length() == 12) {
                // dain format
                formattedAccountNumber = accountNumber.substring(0, 4) + "-" + accountNumber.substring(4, 8) + "-"
                        + accountNumber.substring(8);
            }
            if (accountNumber.length() == 9) {
                // dain format
                formattedAccountNumber = accountNumber.substring(0, 6) + "-" + accountNumber.substring(6, 8) + "-"
                        + accountNumber.substring(8);
            }if (accountNumber.length() == 10) {
                // adp format
                formattedAccountNumber = accountNumber.substring(0, 3) + "-" + accountNumber.substring(3, 8) + "-"
                        + accountNumber.substring(8, 9) + "-" + accountNumber.substring(9);
            }
        }

        formattedAccountNumber = formattedAccountNumber.trim();

        // If the account number is "CLIENT"...
        if (Constants.BK_ENTRY_CLIENT.equals(formattedAccountNumber)) {
            // Make it "C"
            formattedAccountNumber = Constants.BK_ENTRY_CLIENT_SHORT;
        }
        return formattedAccountNumber;
    }

    public static String formatCreditDebitNumber(String accountNumberIn, String accountType, String checkDigit) {
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
                formattedAccountNumber = accountNumber.substring(0, 4) + "-" + accountNumber.substring(4, 8) + "-"
                        + accountNumber.substring(8);
            } else if (accountNumber.length() == 10) {
                // adp format
                formattedAccountNumber = accountNumber.substring(0, 3) + "-" + accountNumber.substring(3, 8) + "-"
                        + accountNumber.substring(8, 9) + "-" + accountNumber.substring(9);
            }
        }

        return formattedAccountNumber;
    }
    /**
     * @param-name
     * @return
     */
    public static String formatRtfLineBreaks(String valueIn) {
        if (valueIn == null) {
            return null;
        }

        String retVal = null;
        String tmp = valueIn;

        while (tmp.endsWith("\n\n")) {
            int idx = tmp.lastIndexOf("\n");
            tmp = tmp.substring(0, idx);
        }
        retVal = tmp.replaceAll("\\r", "");
        retVal = retVal.replaceAll("\\n", "{\\\\line}");

        return retVal;
    }

    /**
     * Returns a formatted String version of a Date if the date passed in is not
     * null and the date format passed in is not null.
     *
     * @param-entryDate Date to format.
     * @param dateFormat The pattern. Uses the standard DateFormat patterns.
     */
    public static String formatDate(Date dateIn, String dateFormat) {
        String retVal = "";

        if (dateIn != null && dateFormat != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
            retVal = dateFormatter.format(dateIn);
        }

        return retVal;
    }

    /**
     * null safe BigDecimal toString(). Returns the result of the toString() method
     * on a BigDecimal passed in if the BigDecimal is not null, otherwise returns an
     * empty String ("").
     *
     * @param-objectIn
     * @return Returns the result of the toString() method on a BigDecimal passed in
     *         if the BigDecimal is not null, otherwise returns an empty String
     *         ("").
     */
    public static String formatBigDecimal(BigDecimal bigDecimalIn) {
        String retVal = "";

        if (bigDecimalIn != null) {
            retVal = bigDecimalIn.toString();
        }

        return retVal;
    }

    /**
     * null safe Object toString(). Returns the result of the toString() method on
     * an Object passed in if the Object is not null, otherwise returns an empty
     * String ("").
     *
     * @param objectIn
     * @return the result of the toString() method on an Object passed in if the
     *         Object is not null, otherwise returns an empty String ("").
     */
    public static String objectToString(Object objectIn) {
        String retVal = "";

        if (objectIn != null) {
            retVal = objectIn.toString();
        }
        return retVal;
    }
}