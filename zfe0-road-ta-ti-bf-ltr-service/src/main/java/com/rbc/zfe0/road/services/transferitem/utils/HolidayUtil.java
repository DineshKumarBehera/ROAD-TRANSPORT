package com.rbc.zfe0.road.services.transferitem.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class HolidayUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isHoliday() {
        Date todaysDate = new Date();

        //It is Weekend
        if (checkWeekend(todaysDate)) {
            return true;
        }

        //It is holiday
        if (checkRBCHoliday(todaysDate) == 1) {
            return true;
        }

        return false;
    }

    //check Weekend
    private boolean checkWeekend(Date runDate) { // throws  java.lang.Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(runDate);

        int whatday = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("What day is " + whatday);

        return whatday == Calendar.SUNDAY || whatday == Calendar.SATURDAY;
    }


    //check holiday
    private int checkRBCHoliday(Date runDate) { // throws  java.lang.Exception {
        Object[] params = new Object[]{
                runDate
        };
        //and their corresponding data types
        int[] types = new int[]{
                Types.DATE,
        };

        String sql = "SELECT COUNT(*) FROM dzfe0.T_HDAY_CALDR WHERE HDAY_OBSRVD=?";
        return jdbcTemplate.queryForObject(sql, params, types, Integer.class);
    }

}