package com.gluck.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@ComponentScan({ "com.gluck.dao" })
@EnableTransactionManagement
public class DataConfig {

    @Bean
    public SessionFactory sessionFactory() {

        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());

        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        builder.scanPackages("com.gluck.model").addProperties(properties);
        return builder.buildSessionFactory();
    }

    @Bean
    public DataSource dataSource() {
        try {
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUser("root");
            ds.setPassword("sa");
            ds.setURL("jdbc:mysql://localhost:3306/gluck");
            return ds;
            // return (DataSource) (new
            // InitialContext().lookup("java:comp/env/jdbc/gluck"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

}
