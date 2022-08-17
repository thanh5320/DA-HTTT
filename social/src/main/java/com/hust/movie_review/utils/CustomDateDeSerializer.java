package com.hust.movie_review.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeSerializer extends JsonDeserializer<Date> {
    public static final String SDF_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final String dateString = jsonParser.getText();
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(SDF_FORMAT);
        dateFormat.setLenient(false);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            throw new IOException("cant parse dat, format: " + SDF_FORMAT + ", received: " + dateString);
        }
    }
}
