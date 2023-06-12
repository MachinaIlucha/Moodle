package com.illiapinchuk.moodle.configuration.security.basic;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration to create a json mapper bean. */
@Configuration
public class JsonMapperConfiguration {

  @Bean
  public JsonMapper jsonMapper() {
    return (JsonMapper) new JsonMapper().registerModule(new JavaTimeModule());
  }
}
