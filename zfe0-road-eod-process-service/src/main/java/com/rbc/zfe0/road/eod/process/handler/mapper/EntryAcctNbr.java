package com.rbc.zfe0.road.eod.process.handler.mapper;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.utils.Utility;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents an account number in a BookkeepingEntry or BatchEntryMapper.
 * This class has helper methods that make it easier to deal with
 * the account number.
 *
 */
public class EntryAcctNbr {

    public static final EntryAcctNbr ENTRY_ACCT_CLIENT = new
            EntryAcctNbr( Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT );
    String entryAcctNbr = null;
    String entryAcctType = null;
    String entryAcctCheckDigit = null;

    String entryAcctNbrCompl = null;

    public EntryAcctNbr()
    {
    }

    /**
     * Accepts a complete and formatted account number.
     * @param completeNumberIn
     */
    public EntryAcctNbr( String completeNumberIn )
    {
        String normalizedAcctNbr = Utility.normalize( completeNumberIn );

// 097-00028-1-2
        if( completeNumberIn.length() == 13  )
        {
            String[] parts = completeNumberIn.split("-");
            if( parts.length == 4 )
            {
// 097-00028
                this.entryAcctNbr = parts[0] + parts[1];
// 1
                this.entryAcctType = parts[2];

                this.entryAcctCheckDigit = parts[3];
            }
        }
// Or will it be 0970002812
        else if( normalizedAcctNbr.length() == 10 )
        {
            this.entryAcctNbr = normalizedAcctNbr.substring( 0, 8 );
            this.entryAcctType = normalizedAcctNbr.substring( 8, 9 );
            this.entryAcctCheckDigit = normalizedAcctNbr.substring( 9 );
        }
        else
        {
// Might be "C", "CLIENT", or "CHOICE"
            this.entryAcctNbr = completeNumberIn;
        }
    }

    public EntryAcctNbr( String entryAcctNbrIn, String entryAcctTypeIn,
                         String entryAcctCheckDigitIn )
    {
        this.entryAcctNbr = entryAcctNbrIn;
        this.entryAcctType = entryAcctTypeIn;
        this.entryAcctCheckDigit = entryAcctCheckDigitIn;
    }

    /**
     * @return Returns the entryAcctCheckDigit.
     */
    public String getEntryAcctCheckDigit() {
        return entryAcctCheckDigit;
    }

    /**
     * @param entryAcctCheckDigitIn The entryAcctCheckDigit to set.
     */
    public void setEntryAcctCheckDigit(String entryAcctCheckDigitIn) {
        if( entryAcctCheckDigitIn != null  )
        {
            this.entryAcctCheckDigit = entryAcctCheckDigitIn.trim();
        }
        else
        {
            this.entryAcctCheckDigit = entryAcctCheckDigitIn;
        }
    }

    /**
     * @return Returns the entryAcctNbr.
     */
    public String getEntryAcctNbr() {
        return entryAcctNbr;
    }

    /**
     *
     * @param entryAcctNbrIn
     */

    public void setEntryAcctNbr( String entryAcctNbrIn ) {
        if( entryAcctNbrIn != null )
        {
            this.entryAcctNbr = entryAcctNbrIn.trim();
        }
        else
        {
            this.entryAcctNbr = entryAcctNbrIn;
        }
    }

    /**
     * @return Returns the entryAcctNbrCompl.
     */
    public String getEntryAcctNbrCompl() {

        if( this.entryAcctNbrCompl == null )
        {
            StringBuffer buff = new StringBuffer();
            if( this.entryAcctNbr != null )
            {
                buff.append( this.entryAcctNbr );
            }
            if( this.entryAcctType != null )
            {
                buff.append( this.entryAcctType );
            }
            if( this.entryAcctCheckDigit != null )
            {
                buff.append( this.entryAcctCheckDigit );
            }

            this.entryAcctNbrCompl = buff.toString();
        }

        return entryAcctNbrCompl;
    }

    /**
     * @return Returns the entryAcctType.
     */
    public String getEntryAcctType() {
        return entryAcctType;
    }

    /**
     *
     * @param entryAcctTypeIn
     */
    public void setEntryAcctType( String entryAcctTypeIn ) {
        if( entryAcctTypeIn != null )
        {
            this.entryAcctType = entryAcctTypeIn;
        }
        else
        {
            this.entryAcctType = entryAcctTypeIn;
        }
    }

    /**
     * Determines if this is a non-blank entry.
     * @return <code>true</code> if the entry should be considered
     * empty, <code>false</code> if not.
     */
    public boolean isEntryAvailable() {
        boolean retVal = true;

        if( StringUtils.isBlank( this.entryAcctNbr ) || this.entryAcctNbr.equals( Constants.BK_ENTRY_NA ))
        {
            retVal = false;
        }
        return retVal;
    }

    /**
     * Helper method to determine if the account number is
     * the value "ORI_SHRT", which is used to drive bookkeeping
     * entries.
     * @return <code>true</code> if the account number is
     * "ORI_SHRT", <code>false</code> if not.
     */
    public boolean isOrigIssueShort() {
        return Constants.BK_ENTRY_ORIG_ISSUE_SHORT.equals( this.entryAcctNbr );
    }

    /**
     * Helper method to determine if the account number is
     * the value "NEW_SHRT", which is used to drive bookkeeping
     * entries.
     * @return <code>true</code> if the account number is
     * "NEW_SHRT", <code>false</code> if not.
     */
    public boolean isNewIssueShort() {
        return Constants.BK_ENTRY_NEW_ISSUE_SHORT.equals( this.entryAcctNbr );
    }

    /**
     * Helper method to determine if the account number is
     * the value "C" or "CLIENT", which is used to drive bookkeeping
     * entries.
     * @return <code>true</code> if the account number is
     * "C" or "CLIENT", <code>false</code> if not.
     */
    public boolean isClient() {
        return Constants.BK_ENTRY_CLIENT_SHORT.equals( this.entryAcctNbr ) || Constants.BK_ENTRY_CLIENT.equals( this.entryAcctNbr );
    }

    /**
     * Helper method to determine if the account number is
     * the value "CHOICE", which is used to drive bookkeeping
     * entries.
     * @return <code>true</code> if the account number is
     * "CHOICE", <code>false</code> if not.
     */
    public boolean isUserChoice() {
        return Constants.BK_ENTRY_USER_CHOICE.equals( this.entryAcctNbr );
    }

    /**
     * Helper method to determine if the account number is
     * the value "ROLLBACK", which is used to drive bookkeeping
     * entries.
     * @return <code>true</code> if the account number is
     * "ROLLBACK", <code>false</code> if not.
     */
    public boolean isRollback() {
        return Constants.BK_ENTRY_ROLLBACK.equals( this.entryAcctNbr );
    }
}
