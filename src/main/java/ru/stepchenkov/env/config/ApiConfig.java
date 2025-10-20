package ru.stepchenkov.env.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:env",
        "system:properties",
        "classpath:config/${env}/apiConfig.properties"
})
public interface ApiConfig extends Config {

    @Key("api.url")
    String url();

}
