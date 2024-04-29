package com.example.paysonix.configuration;

import com.example.paysonix.domain.response.Result;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(Result.class, new SimpleResultWithTrailingCommaSerializer());
        return builder.build();
    }

    public static class SimpleResultWithTrailingCommaSerializer extends JsonSerializer<Result> {

        public static final String TRAILING_SYMBOL = ",";

        @Override
        public void serialize(Result value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject(value);

            for (BeanPropertyDefinition definition: getDefinitions(serializers, value)) {
                Object fieldValue = getFieldValue(definition, value);
                JsonSerializer<Object> serializer = serializers.findValueSerializer(fieldValue.getClass());

                gen.writeFieldName(definition.getName());

                serializer.serialize(fieldValue, gen, serializers);
            }

            gen.writeRaw(TRAILING_SYMBOL);
            gen.writeEndObject();
        }

        private Object getFieldValue(BeanPropertyDefinition definition, Result value) {
            Method method = (Method) definition.getAccessor().getMember();
            Object fieldValue;
            try {
                fieldValue = method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            return fieldValue;
        }

        private List<BeanPropertyDefinition> getDefinitions(SerializerProvider serializers, Result value) {
            SerializationConfig config = serializers.getConfig();
            BeanDescription beanDescription = config.introspect(config.constructType(value.getClass()));
            return beanDescription.findProperties();
        }
    }
}