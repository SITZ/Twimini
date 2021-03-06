package com.directi.train.tweetapp;

import net.spy.memcached.MemcachedClient;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class AppConfig {

    @Bean
    public SimpleJdbcTemplate simpleJdbcTemplate1() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost/minitwitter1");
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("qwerty");
        return new SimpleJdbcTemplate(basicDataSource);
    }

    @Bean
    public SimpleJdbcTemplate simpleJdbcTemplate2() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost/minitwitter2");
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("qwerty");
        return new SimpleJdbcTemplate(basicDataSource);
    }

    @Bean
    public SimpleJdbcTemplate shardTemplate() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:postgresql://localhost/minitwittershard");
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("qwerty");
        return new SimpleJdbcTemplate(basicDataSource);
    }

    @Bean
    public MemcachedClient memcachedClient() {
        try {
            return new MemcachedClient(new InetSocketAddress("localhost",11211));
        } catch (IOException e) {
            return null;
        }
    }
}
