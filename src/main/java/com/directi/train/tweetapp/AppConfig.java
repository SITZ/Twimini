package com.directi.train.tweetapp;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public SimpleJdbcTemplate simpleJdbcTemplate() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost/minitwitter");
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("postgres");

        return new SimpleJdbcTemplate(basicDataSource);
    }

    @Bean
    public ThreadLocal<Long> userID() {
        return new ThreadLocal<Long>();
    }
}
