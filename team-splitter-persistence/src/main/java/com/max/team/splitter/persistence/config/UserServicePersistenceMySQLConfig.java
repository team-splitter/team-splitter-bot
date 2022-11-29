package com.max.team.splitter.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
/**
 * @author Payment Gateway Archetype
 */
@Configuration
@ConditionalOnProperty(name = "db", havingValue = "mysql")

public class UserServicePersistenceMySQLConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserServicePersistenceMySQLConfig.class);

    private final Environment env;

    @Autowired
    public UserServicePersistenceMySQLConfig(Environment env) {
        this.env = env;
    }


    @Bean
    public DataSource userDS() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("user");
        logger.info("Configuring User Persistence with a MySQL database");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(env.getRequiredProperty("team-splitter.db.mysql.url"));
        dataSource.setUsername(env.getRequiredProperty("team-splitter.db.mysql.user"));
        dataSource.setPassword(env.getRequiredProperty("team-splitter.db.mysql.password"));
        if (env.containsProperty("team-splitter.db.mysql.pool.maximumPoolSize")) {
            dataSource.setMaximumPoolSize(env.getProperty("team-splitter.db.mysql.pool.maximumPoolSize", Integer.class));
        }
        if (env.containsProperty("team-splitter.db.mysql.pool.connectionTimeout")) {
            dataSource.setConnectionTimeout(env.getProperty("team-splitter.db.mysql.pool.connectionTimeout", Long.class));
        }
        if (env.containsProperty("team-splitter.db.mysql.pool.maxLifetime")) {
            dataSource.setMaxLifetime(env.getProperty("team-splitter.db.mysql.pool.maxLifetime", Long.class));
        }
        if (env.containsProperty("team-splitter.db.mysql.pool.idleTimeout")) {
            dataSource.setIdleTimeout(env.getProperty("team-splitter.db.mysql.pool.idleTimeout", Long.class));
        }
        return dataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter userVA() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        // TODO: make switchable
//        vendorAdapter.setShowSql(Switches.showSql.isEnabled());
        return vendorAdapter;
    }
}
