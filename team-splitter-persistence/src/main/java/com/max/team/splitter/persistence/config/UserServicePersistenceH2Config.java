package com.max.team.splitter.persistence.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * @author Service Archetype
 */
@Configuration
@ConditionalOnProperty(name = "db", havingValue = "h2")
public class UserServicePersistenceH2Config {

    private static final Logger logger = LoggerFactory.getLogger(UserServicePersistenceH2Config.class);

    @Bean
    public DataSource userDS() {
        logger.info("Configuring User Service Persistence with an H2 database");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("user");
        dataSource.setDataSource(new EmbeddedDatabaseBuilder()
            .generateUniqueName(true)
            .setType(EmbeddedDatabaseType.H2)
            .addScript("db/h2-postgres-init.sql").build());
        return dataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter userVA() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
//         vendorAdapter.setShowSql(Switches.showSql.isEnabled());
        return vendorAdapter;
    }
}
