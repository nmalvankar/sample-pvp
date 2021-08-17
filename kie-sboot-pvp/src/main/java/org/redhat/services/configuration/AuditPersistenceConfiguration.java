package org.redhat.services.configuration;

import javax.sql.DataSource;

import org.jbpm.springboot.autoconfigure.EntityManagerFactoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.mysql.cj.jdbc.MysqlDataSource;

/**
 *  NOTE: Currently not working..
 *  PVP Across Multiple Sources is currently broken : https://issues.redhat.com/browse/BAPL-1704
 */
@Configuration
public class AuditPersistenceConfiguration {

    @Value("${kie.spring.secondary.datasource.url}")
    String jdbcUri;

    @Value("${kie.spring.secondary.datasource.pu-name:org.jbpm.audit.persistence.jpa}")
    String puName;

    @Value("${kie.spring.secondary.datasource.username:sa}")
    String username;

    @Value("${kie.spring.secondary.datasource.password:sa}")
    String password;

    @Value("${kie.spring.secondary.datasource.properties.hibernate.dialect}")
    String dialect;

    @Value("${kie.spring.secondary.datasource.properties.hibernate.ddl-auto:update}")
    String ddl;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Use a customised persistece.xml from jbpm if you wish to import jbpm Entity classes i.e. auditing etc..
     */
    protected static final String PERSISTENCE_XML_LOCATION = "classpath:/META-INF/audit-persistence.xml";
    protected static final String AUDIT_PERSISTENCE_UNIT_NAME = "org.jbpm.audit.persistence.jpa";

    @Bean(name = "auditEntityManager")
    @ConditionalOnMissingBean(name = "auditEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("jpaAuditDataSource") DataSource dataSource, 
                                                                       JpaProperties jpaProperties) {
        return EntityManagerFactoryHelper.create(applicationContext,
                                                 dataSource,
                                                 jpaProperties,
                                                 AUDIT_PERSISTENCE_UNIT_NAME,
                                                 PERSISTENCE_XML_LOCATION);
    }



    @Bean(name = "jpaAuditDataSource")
    public DataSource h2DataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL(jdbcUri);
        ds.setUser(username);
        ds.setPassword(password);
        return ds;
    }


}
