package com.rbc.zfe0.road.services.transferitem.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String date_format = "MM/dd/yyyy, hh:mm:ss a";
        String dateValue = jsonParser.getValueAsString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat((date_format));
            return sdf.parse(dateValue);
        }catch(Exception e){
            throw new IOException("Failed to parse date: "+ dateValue, e);
        }
    }
}
