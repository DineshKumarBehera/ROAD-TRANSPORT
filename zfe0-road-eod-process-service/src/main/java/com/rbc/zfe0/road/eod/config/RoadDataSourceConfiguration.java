package com.rbc.zfe0.road.eod.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.rbc.zfe0.road.eod.persistence",
        entityManagerFactoryRef = "dataSourceEntityManagerFactory",
        transactionManagerRef = "dataSourceTransactionManager")
public class RoadDataSourceConfiguration {

    @Value("${rbc.road.datasource.url}")
    private String dataSourceUrl;
    @Value("${rbc.road.datasource.username}")
    private String dataSourceUsername;
    @Value("${rbc.road.datasource.password}")
    private String dataSourcePassword;
    @Value("${rbc.road.datasource.schema}")
    private String dataSourceSchema;
    @Value("${rbc.road.datasource.driver-class-name}")
    private String dataSourceDriverClassName;
    @Value("${rbc.road.datasource.dialect}")
    private String dataSourceDialect;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean dataSourceEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sqlDataSource());
        em.setPackagesToScan("com.rbc.zfe0.road.eod.persistence");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties(dataSourceDialect));
        return em;
    }

    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource sqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceDriverClassName);
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);
        dataSource.setSchema(dataSourceSchema);
        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager dataSourceTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                dataSourceEntityManagerFactory().getObject());
        return transactionManager;
    }

    //    @Bean
//    public JmsTemplate jmsTemplate() {
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        return jmsTemplate;
//    }
    private Properties additionalProperties(String dataSourceDialect) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dataSourceDialect);
        return properties;
    }
}
