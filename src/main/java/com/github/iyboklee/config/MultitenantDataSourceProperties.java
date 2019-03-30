/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "multitenant")
public class MultitenantDataSourceProperties {

    private List<HikariConfigBuilder> hikariConfigBuilders;

    public HikariConfigBuilder builder(String name) {
        for (HikariConfigBuilder builder : hikariConfigBuilders) {
            if (builder.getName().equals(name))
                return builder;
        }
        return null;
    }

    @Data
    public static class HikariConfig {
        String driverClassName;
        String url;
        String username;
        String password;
        int minIdle;
        int maxPoolSize;
    }

    @Data
    public static class HikariConfigBuilder {
        String name;
        HikariConfig hikariConfig;

        public com.zaxxer.hikari.HikariConfig build() {
            Assert.notNull(name, "Builder name can not be null!");
            Assert.notNull(hikariConfig, "Hikari config can not be null!");
            com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
            config.setPoolName(name);
            config.setDataSourceClassName(hikariConfig.driverClassName);
            config.setMinimumIdle(hikariConfig.minIdle);
            config.setMaximumPoolSize(hikariConfig.maxPoolSize);
            config.addDataSourceProperty("url", hikariConfig.url);
            config.addDataSourceProperty("user", hikariConfig.username);
            config.addDataSourceProperty("password", hikariConfig.password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            return config;
        }
    }

}