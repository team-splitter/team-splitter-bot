package com.max.team.splitter.persistence.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * @author Service Archetype
 */
@Configuration
@ConditionalOnProperty(name = "db", havingValue = "postgres")
public class UserServicePersistencePostgresConfig {

    private static final Logger logger = LoggerFactory.getLogger(UserServicePersistencePostgresConfig.class);

    private final Environment env;

    @Autowired
    public UserServicePersistencePostgresConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource userDS() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("user");
        logger.info("Configuring User Persistence with a PostgreSQL database");
        dataSource.setJdbcUrl(env.getRequiredProperty("team-splitter.db.postgres.url"));
        dataSource.setUsername(env.getRequiredProperty("team-splitter.db.postgres.user"));
        dataSource.setPassword(env.getRequiredProperty("team-splitter.db.postgres.password"));
        if (env.containsProperty("team-splitter.db.postgres.pool.maximumPoolSize")) {
            dataSource.setMaximumPoolSize(env.getProperty("team-splitter.db.postgres.pool.maximumPoolSize", Integer.class));
        }
        if (env.containsProperty("team-splitter.db.postgres.pool.connectionTimeout")) {
            dataSource.setConnectionTimeout(env.getProperty("team-splitter.db.postgres.pool.connectionTimeout", Long.class));
        }
        if (env.containsProperty("team-splitter.db.postgres.pool.maxLifetime")) {
            dataSource.setMaxLifetime(env.getProperty("team-splitter.db.postgres.pool.maxLifetime", Long.class));
        }
        if (env.containsProperty("team-splitter.db.postgres.pool.idleTimeout")) {
            dataSource.setIdleTimeout(env.getProperty("team-splitter.db.postgres.pool.idleTimeout", Long.class));
        }
        return dataSource;
    }

    @Bean
    public HibernateJpaVendorAdapter userVA() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
//         vendorAdapter.setShowSql(Switches.showSql.isEnabled());
        return vendorAdapter;
    }
}
