package org.redhat.services.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JDBCTemplate {

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("jpaAuditDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}