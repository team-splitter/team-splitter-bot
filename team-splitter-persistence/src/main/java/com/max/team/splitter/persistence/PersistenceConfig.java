package com.max.team.splitter.persistence;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Archetect
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "com.max.team.splitter.persistence.repositories",
        },
        entityManagerFactoryRef = "userEMF",
        transactionManagerRef = "userTM")
@ComponentScan
public class PersistenceConfig {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceConfig.class );
    private final Environment env;

    @Autowired
    public PersistenceConfig(final Environment env) {
        this.env = env;
    }

    @Bean(name = "userTM")
    @Qualifier("user")
    public JpaTransactionManager userTM(
    @Qualifier("userDS") final DataSource dataSource,
    @Qualifier("userEMF") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean userEMF(
    @Qualifier("userDS") final DataSource dataSource,
    @Qualifier("userVA") final JpaVendorAdapter vendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPersistenceUnitName("user");
        factory.setPackagesToScan(
            "com.max.team.splitter.persistence.entities"
        );
        return factory;
    }

    @Bean
    @Qualifier("user")
    public JdbcTemplate userJdbcTemplate(@Qualifier("userDS") final DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.liquibase.enabled", havingValue = "true", matchIfMissing = true)
    public SpringLiquibase userLiquibase(@Qualifier("userDS") final DataSource dataSource) {
        logger.info("Applying Liquibase");
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        if (env.containsProperty("initdb") || env.containsProperty("gateway.initdb")) {
            liquibase.setDropFirst(true);
        }
        liquibase.setChangeLog("classpath:db/crdb/changelog-master.xml");
        return liquibase;
    }
}