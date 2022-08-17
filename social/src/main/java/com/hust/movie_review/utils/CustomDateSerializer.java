package com.hust.movie_review.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    public static final String SDF_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SDF_FORMAT);
        dateFormat.setLenient(false);
        String formattedDate = dateFormat.format(date);
        jsonGenerator.writeString(formattedDate);
    }
}