package com.rbc.zfe0.road.services.transferitem.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Utility {

    public static String normalize(String valueIn) {
        return normalize(valueIn, false);
    }

    /**
     * @param valueIn
     * @param returnNonNull
     * @return
     */
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

    public static Integer convertBooleanToInteger(Boolean flag) {
        Integer value = null;
        if (flag) {
            value = 1;
        } else {
            value = 0;
        }
        return value;
    }
}
