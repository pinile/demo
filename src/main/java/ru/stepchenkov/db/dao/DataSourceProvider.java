package ru.stepchenkov.db.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.stepchenkov.env.config.DBConfig;

import javax.sql.DataSource;

@Slf4j
@UtilityClass
public class DataSourceProvider {
    public static DataSource getH2DataSource(DBConfig config) {
        log.info("Создание H2 DataSource");

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.h2JdbcUrl());
        hikariConfig.setUsername(config.user());
        hikariConfig.setPassword(config.password());
        hikariConfig.setAutoCommit(true);
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setPoolName("H2");

        log.info("H2 DataSource создана");

        return new HikariDataSource(hikariConfig);
    }
}
