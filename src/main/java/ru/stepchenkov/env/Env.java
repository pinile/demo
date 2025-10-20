package ru.stepchenkov.env;

import org.aeonbits.owner.ConfigFactory;
import ru.stepchenkov.env.config.ApiConfig;
import ru.stepchenkov.env.config.DBConfig;

public final class Env {

    public static class DB {
        public static final DBConfig DB_CONFIG = ConfigFactory.create(DBConfig.class);
    }

    public static class Api {
        public static final ApiConfig API_CONFIG = ConfigFactory.create(ApiConfig.class);
    }
}
