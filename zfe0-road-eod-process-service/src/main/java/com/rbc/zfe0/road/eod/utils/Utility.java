package com.rbc.zfe0.road.eod.utils;

import com.rbc.zfe0.road.eod.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class Utility {

    private static SimpleDateFormat fm = new SimpleDateFormat("MM-dd-yyyy");


    public static User getUser() {
        return new User("ROADSVC", "Roadsvc", true, true, true, true,
                Arrays.asList(new GrantedAuthority[0]));
    }


    //checks for the length of the string and returns the substring.
    public static String getString(String aMessage, int start, int length) {
        String val = "";
        int end = 0;
        try {
            if (!((aMessage.length() - 1) > start)) {
                return "";
            }
            if (aMessage.length() < (start + length)) {
                end = aMessage.length();
            } else {
                end = start + length;
            }
            val = aMessage.substring(start, end);
        } catch (Exception exp) {
            log.error("Error in getString. String: " +
                    aMessage + " start: " + start + " length: " + length, exp );
        }
        return val;
    }

    //checks for empty arraylist
    public static boolean isEmpty(List list) {
        if ((list != null)) {
            if (list.size() != 0)
                return false;
        }
        return true;
    }

    //checks for empty string
    public static boolean isEmpty(String str) {
        if ((str != null) && (str.trim().length() != 0)) {
            return false;
        }
        return true;
    }

    //checks for empty string
    public static boolean isEmpty(Date aDate) {
        if (aDate != null) {
            return false;
        }
        return true;
    }

    //checks for empty string
    public static boolean isEmpty(BigDecimal aVal) {
        if (aVal != null) {
            return false;
        }
        return true;
    }

    //returns int value from string
    public static int getInteger(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    //utility method to get account number from the string.
    public static String getAccountNumber(String message, int offset) {
        String accNum = "";
        if (message.length() > offset + 19) {
            accNum =  getString(message, offset, 3)
                    + getString(message, offset+3, 5);
        }
        return accNum;
    }

    public static Date addToDate(Date aDate, int days){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(aDate);
        c1.add(Calendar.DATE, days);
        return c1.getTime();
    }

    public static Date addMontsToCurrentDate(int months){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        c1.add(Calendar.MONTH, months);
        return c1.getTime();
    }

    public static Date getCurrentDate(){
        Calendar c1 = Calendar.getInstance();     //new GregorianCalendar();
        return c1.getTime();
    }

    public static int getCurrentDay(){
        Calendar c1 = Calendar.getInstance();
        return c1.get(Calendar.DATE );
    }

    //check two dates are equals
    public static boolean checkDateEqual(Date aDate, Date withDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(aDate);
        c2.setTime(withDate);

//Logger.debug(fm.format(c1.getTime())+" is " + fm.format(c2.getTime()));
        if(fm.format(c1.getTime()).equalsIgnoreCase(fm.format(c2.getTime()))){
            return true;
        }
        return false;
    }
    //compare dates
    public static boolean checkDateGreater(Date aDate, Date withDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(aDate);
        c2.setTime(withDate);
        if(c1.after(c2)){
            return true;
        }
        return false;
    }

    //compare dates
    public static boolean checkDateLess(Date aDate, Date withDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(aDate);
        c2.setTime(withDate);
        if(c1.before(c2)){
            return true;
        }
        return false;
    }

    public static boolean checkDateGreaterOREqual(Date aDate, Date withDate){
        if(checkDateGreater(aDate, withDate) || (checkDateEqual(aDate, withDate))){
            return true;
        }

        return false;
    }

    public static boolean checkDateLessOREqual(Date aDate, Date withDate){
        if(checkDateLess(aDate, withDate) || (checkDateEqual(aDate, withDate))){
            return true;
        }
        return false;
    }

    public static String getStringFromInt(int i){
        return (new Integer(i)).toString();
    }

    public static long getDiffBetDates(Date dateFrom , Date dateTo){
        long diff = (dateFrom.getTime() - dateTo.getTime()/ (1000*60*60*24));
        log.debug("Diff in Days = " + diff );
        return diff;
    }

    public static String formatDate(Date aDate){
        return fm.format(aDate);
    }

    public static  String formatString(String val){
        String str = val;
        if (str == null){
            str="";
        } else if (!Utility.isEmpty(str)) {
            str = val.replaceAll("\n", " ");
            str = str.replaceAll("\r", "");
        }

        return str;
    }

    public static String encodeForAmpersand(String aValues) {
        String encodedStr = "";
        if(!isEmpty(aValues)) {
            if(aValues.indexOf(Constants.AMPERSAND)> 0) {
                encodedStr = Utility.replaceAll(aValues, Constants.AMPERSAND, Constants.AMPERSAND_DECODED);
            }else {
                encodedStr = aValues;
            }
        }
        return encodedStr;
    }


    public static String encodeForGreaterThan(String aValues) {
        String encodedStr = "";
        if(!isEmpty(aValues)) {
            if((aValues.indexOf(Constants.GREATER_THAN)) > 0) {
                encodedStr = Utility.replaceAll(aValues, Constants.GREATER_THAN, Constants.GREATER_THAN_DECODED);
            }else {
                encodedStr = aValues;
            }
        }
        return encodedStr;
    }


    public static String encodeForLessThan(String aValues) {
        String encodedStr = "";
        if(!isEmpty(aValues)) {
            if(aValues.indexOf(Constants.LESS_THAN) >0 ) {
                encodedStr = replaceAll(aValues, Constants.LESS_THAN, Constants.LESS_THAN_DECODED);
            }else {
                encodedStr = aValues;
            }
        }
        return encodedStr;
    }


    public static String encodeForApostrophe(String aValues) {
        String encodedStr = "";
        if(!isEmpty(aValues)) {
            if(aValues.indexOf(Constants.APOSTROPHE) >0 ) {
                encodedStr = replaceAll(aValues, Constants.APOSTROPHE, Constants.APOSTROPHE_DECODED);
            }else {
                encodedStr = aValues;
            }
        }
        return encodedStr;
    }

    public static String replaceAll(String stringForReplace, String replaceMentFor, String replacementTo) {
        String st = stringForReplace;
        try {
            st =st.replaceAll(replaceMentFor , replacementTo);
        }catch(Exception ex) {
//nothing to do;
        }
        return st;
    }




    public static String normalize(String valueIn, boolean returnNonNull ) {
        String retVal = valueIn;

        if( valueIn != null )
        {
            retVal = StringUtils.replaceChars( valueIn, "- \t", null  );
        }

        if( retVal == null && returnNonNull == true )
        {
            retVal = "";
        }
        return retVal;
    }

    public static String normalize(String valueIn ) {

        return normalize( valueIn, false );
    }
}
