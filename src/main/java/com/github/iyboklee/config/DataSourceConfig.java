/**
 * @Author iyboklee (iyboklee@gmail.com)
 */
package com.github.iyboklee.config;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.iyboklee.config.mtds.MultitenantDataSource;
import com.github.iyboklee.config.mtds.TenantType;
import com.zaxxer.hikari.HikariDataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

@Configuration
@EnableConfigurationProperties(MultitenantDataSourceProperties.class)
@EnableJpaRepositories(basePackages = "com.github.iyboklee.core.repository")
@EntityScan(basePackages = "com.github.iyboklee.core.model", basePackageClasses = Jsr310JpaConverters.class)
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired private MultitenantDataSourceProperties multiTenantDataSourceProperties;

    protected DataSource master() {
        MultitenantDataSourceProperties.HikariConfigBuilder builder = multiTenantDataSourceProperties.builder("master");
        return new Log4jdbcProxyDataSource(new HikariDataSource(builder.build()));
    }

    protected DataSource slave() {
        MultitenantDataSourceProperties.HikariConfigBuilder builder = multiTenantDataSourceProperties.builder("slave");
        return new Log4jdbcProxyDataSource(new HikariDataSource(builder.build()));
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        MultitenantDataSource ds = new MultitenantDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(TenantType.MASTER, master());
        targetDataSources.put(TenantType.SLAVE, slave());
        ds.setTargetDataSources(targetDataSources);
        ds.setDefaultTargetDataSource(targetDataSources.get(TenantType.MASTER));
        return ds;
    }

}
