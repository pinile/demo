package ru.stepchenkov.db.dao;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import ru.stepchenkov.env.config.DBConfig;

import javax.sql.DataSource;

@Slf4j
@UtilityClass
public class DataSourceProvider {
    public static DataSource getH2DataSource(DBConfig config) {
        log.info("Создание H2 DataSource");

        disableHikariLogging();

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

    private static void disableHikariLogging() {
        try {
            // Отключаем через Logback
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.getLogger("com.zaxxer.hikari").setLevel(Level.OFF);

            // Отключаем JUL
            java.util.logging.LogManager.getLogManager().reset();
            java.util.logging.Logger.getLogger("com.zaxxer.hikari").setLevel(java.util.logging.Level.OFF);
        } catch (Exception e) {}
    }
}
