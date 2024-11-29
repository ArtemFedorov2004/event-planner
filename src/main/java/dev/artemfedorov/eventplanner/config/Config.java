package dev.artemfedorov.eventplanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public IgnoreNullBeanUtilsBean ignoreNullBeanUtilsBean() {
        return new IgnoreNullBeanUtilsBean();
    }
}
