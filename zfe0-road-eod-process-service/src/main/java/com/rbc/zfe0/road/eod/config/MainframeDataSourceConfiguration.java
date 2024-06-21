package com.rbc.zfe0.road.eod.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "mainframeDataSourceEntityManagerFactory",
        transactionManagerRef = "mainframeDataSourceTransactionManager")
public class MainframeDataSourceConfiguration {

    @Value("${rbc.road.mainframe-datasource.url}")
    private String mainframeDataSourceUrl;
    @Value("${rbc.road.mainframe-datasource.username}")
    private String mainframeDataSourceUsername;
    @Value("${rbc.road.mainframe-datasource.password}")
    private String mainframeDataSourcePassword;
    @Value("${rbc.road.mainframe-datasource.schema}")
    private String mainframeDataSourceSchema;
    @Value("${rbc.road.mainframe-datasource.driver-class-name}")
    private String mainframeDataSourceDriverClassName;
    @Value("${rbc.road.mainframe-datasource.dialect}")
    private String mainframeDataSourceDialect;

    @Bean
    public LocalContainerEntityManagerFactoryBean mainframeDataSourceEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mainframeDataSource());
        em.setPackagesToScan("com.rbc.zfe0.road.eod.persistence");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties(mainframeDataSourceDialect));
        return em;
    }

    @Bean(name = "mainframeDataSource")
    public DataSource mainframeDataSource() {
        DriverManagerDataSource mf = new DriverManagerDataSource();
        mf.setDriverClassName(mainframeDataSourceDriverClassName);
        mf.setUrl(mainframeDataSourceUrl);
        mf.setUsername(mainframeDataSourceUsername);
        mf.setPassword(mainframeDataSourcePassword);
        mf.setSchema(mainframeDataSourceSchema);
        return mf;
    }

    @Bean
    public PlatformTransactionManager mainframeDataSourceTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                mainframeDataSourceEntityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties additionalProperties(String dataSourceDialect) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dataSourceDialect);
        return properties;
    }
}

