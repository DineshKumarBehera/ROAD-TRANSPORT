package com.rbc.zfe0.road.services.transferitem.filter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author oric014
 * used by the TransferItemFilter to represent filter range (FROM x value TO y value).
 */
public class RangeCriteriaDate implements Serializable {
    private Date from;
    private Date to;
    private boolean lookingForNull = false;


    public RangeCriteriaDate()
    {
    }


    public RangeCriteriaDate( Date from, Date to ) {
        this(from, to, true);
    }

    public RangeCriteriaDate( boolean lookForNull ) {
        lookingForNull = lookForNull;
    }

    public RangeCriteriaDate( Date from, Date to, boolean resetTimestamp ) {
        super();
        setFrom(from, resetTimestamp);
        setTo(to);
    }

    /**
     * @return Returns the from.
     */
    public Date getFrom() {
        return from;
    }
    /**
     * @param from The from to set.
     */
    private GregorianCalendar calendarHelper= new GregorianCalendar();

    public void setFrom( Date from )
    {
        this.setFrom( from, true );
    }

    public void setFrom(Date from, boolean resetTimestamp)
    {
        this.from= from;
        if(from != null){
            if ( resetTimestamp)
            {
// make sure it is the very beginning of the day
                calendarHelper.setTime(from);
                calendarHelper.clear(Calendar.MILLISECOND);
                calendarHelper.clear(Calendar.SECOND);
                calendarHelper.clear(Calendar.MINUTE);
                calendarHelper.clear(Calendar.HOUR);
                calendarHelper.clear(Calendar.HOUR_OF_DAY);
                this.from = calendarHelper.getTime();
            }else {
                calendarHelper.setTime(from);
                this.from = calendarHelper.getTime();
            }
        }
    }

    /**
     * @return Returns the to.
     */
    public Date getTo() {
        return to;
    }
    /**
     * @param to The to to set.
     */
    public void setTo(Date to) {
        this.to= to;
        if ( to != null ) {
            calendarHelper.setTime(to);
            calendarHelper.set(Calendar.MILLISECOND,999);
            calendarHelper.set(Calendar.SECOND,59);
            calendarHelper.set(Calendar.MINUTE,59);
            calendarHelper.set(Calendar.HOUR_OF_DAY,23);
            this.to = calendarHelper.getTime();
        }
    }
    public boolean hasCriteria() {
        return from != null || to != null;
    }


    public boolean equals(Object obj) {
        boolean match;
        if ( obj == null ||
                !(obj instanceof RangeCriteriaDate)) {
            return false;
        }
        RangeCriteriaDate thatCriteria= (RangeCriteriaDate) obj;
        match= ( this.from == null )
                ? thatCriteria.getFrom() == null
                : thatCriteria.getFrom() != null
                ? this.from.compareTo(thatCriteria.getFrom()) == 0
                : false;
        match&= ( this.to == null )
                ? thatCriteria.getTo() == null
                : thatCriteria.getTo() != null
                ? this.from.compareTo(thatCriteria.getTo()) == 0
                : false;

        return match;
    }

    private static final long MILLIS_IN_A_DAY= 1000 * 60 * 60 * 24;
    public String toString() {
        SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy");
        return (from != null && to != null)
                ? sdf.format(from) + " to " + sdf.format(to)
                : ( from != null )
                ? " after " + sdf.format( new Date(from.getTime()-MILLIS_IN_A_DAY))
                : ( to != null )
                ? " before " + sdf.format(new Date(to.getTime()+MILLIS_IN_A_DAY))
                : "";
    }

    public int hashCode() {
        return (from != null && to != null)
                ? (from.hashCode() + to.hashCode()) * 3
                : ( from != null )
                ? from.hashCode() * 5
                : ( to != null )
                ? to.hashCode() * 7
                : 0;
    }
    /**
     * @return Returns the lookingForNull.
     */
    public boolean isLookingForNull() {
        return lookingForNull;
    }
    /**
     * @param lookingForNull The lookingForNull to set.
     */
    public void setLookingForNull(boolean lookingForNull) {
        this.lookingForNull = lookingForNull;
    }
}
