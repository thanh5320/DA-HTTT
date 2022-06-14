package org.logstashplugins.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.logstashplugins.utils.time.TimeUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateDeSerializer extends JsonDeserializer<Date> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeUtils.DT_FORMAT);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String dateString = jsonParser.getText();
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }

        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault())
                .toInstant());
        return date;
    }
}
