package com.planetvoor;

import java.sql.Connection;

import javax.inject.Provider;
import javax.sql.DataSource;

import com.querydsl.sql.MySQLTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import org.springframework.context.annotation.Profile;

/**
 * @author voor
 */
@Configuration
public class QueryDslConfiguration {

    private final DataSource dataSource;

    @Autowired
    public QueryDslConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @Profile("test")
    public com.querydsl.sql.Configuration querydslTestConfiguration() {
        SQLTemplates templates = HSQLDBTemplates.builder().build(); // change to your Templates
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }

    @Bean
    @Profile("!test")
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = MySQLTemplates.builder().build(); // change to your Templates
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        return new SQLQueryFactory(querydslConfiguration(), provider);
    }
}
