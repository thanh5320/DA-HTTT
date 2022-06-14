package org.logstashplugins.teamplate;

import co.elastic.logstash.api.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.logstashplugins.config.ObjectMapperConfig;
import org.logstashplugins.teamplate.mapper.AbsMapper;

import java.util.Collection;
import java.util.Collections;

public abstract class AlterFilter<OKafka, OEs, OLog, M extends AbsMapper<OKafka, OEs>> implements Filter {
    private Logger log;
    public static final PluginConfigSpec<String> SOURCE_CONFIG =
            PluginConfigSpec.stringSetting("message", "message");

    private String id;
    private String sourceField;
    private ObjectMapper objectMapper;
    private M mapper;

    Class<OKafka> oKafkaClass;
    public AlterFilter(String id, Configuration config, M mapper, Class<OKafka> oKafkaClass, Class<OLog> oLogClass) {
        this.id = id;
        this.sourceField = config.get(SOURCE_CONFIG);
        this.objectMapper = ObjectMapperConfig.getObjectMapper();
        this.mapper = mapper;
        this.log = LogManager.getLogger(oLogClass);
        this.oKafkaClass=oKafkaClass;

    }

    @Override
    public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener) {
        for (Event e : events) {
            Object str = e.getField(sourceField);
            if (str instanceof String) {
                String result = process((String) str);
                if(result==null){
                    log.error("Error messages will be ignored: {}", str.toString());
                }
                e.setField(sourceField, result);
                matchListener.filterMatched(e);
            }
        }
        return events;
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        return Collections.singletonList(SOURCE_CONFIG);
    }

    @Override
    public String getId() {
        return this.id;
    }



    public OKafka convertMessToOKafka(String message){
        OKafka oKafka= null;

        try {
            oKafka = ObjectMapperConfig.getObjectMapper().readValue(message, oKafkaClass);
        } catch (JsonProcessingException ex) {
            log.error("error while converting message from kafka to object", ex.getMessage(), ex);
        }

        if (!isValid(oKafka)) {
            return null;
        }
        return oKafka;
    }

    public String process(String input){
        OKafka oKafka = convertMessToOKafka(input);
        if(oKafka==null) return null;
        OEs oEs = mapper.toOEs(oKafka);
        if(oEs==null) return null;
        oEs = ownProcess(oEs);
        return convertOEsToMess(oEs);
    }

    public abstract OEs ownProcess(OEs oEs);

    public abstract boolean isValid(OKafka oKafka);

    public String convertOEsToMess(OEs oEs) {
        String message = null;
        try {
            message = objectMapper.writeValueAsString(oEs);
        } catch (JsonProcessingException ex) {
            log.error("error when converting from ES object to message!");
        }
        return message;
    }
}

