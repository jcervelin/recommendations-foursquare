package io.jcervelin.ideas.geofinder.recommendations.configs.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;


/**
 * Class responsible for customise the date format of the JSON responses
 * the pattern to LocalDate is dd/MM/yyyy
 */
@Configuration
public class JacksonParser {

    @Bean
    public JavaTimeModule javaTimeModule() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(("dd/MM/yyyy"))));
        return javaTimeModule;
    }

    @Bean
    @Primary
    public ObjectMapper jsonObjectMapper(final JavaTimeModule javaTimeModule) {
        return Jackson2ObjectMapperBuilder
                .json()
                .serializationInclusion(NON_NULL)
                .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
                .modules(javaTimeModule)
                .simpleDateFormat("dd/MM/yyyy'T'HH:mm:ss'Z'")
                .build();
    }

}