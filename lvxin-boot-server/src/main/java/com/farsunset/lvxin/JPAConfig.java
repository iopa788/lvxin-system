package com.farsunset.lvxin;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JPAConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Value("${spring.jpa.database}") String database,
			@Value("${spring.jpa.hibernate.dialect}") String dialect,
			@Value("${spring.jpa.show-sql}") String showSql,
			@Value("${spring.jpa.hibernate.naming.strategy}") String namingStrategy,
			@Value("${spring.jpa.hibernate.naming.physical-strategy}") String physicalStrategy,
			@Value("${spring.jpa.hibernate.ddl-auto}") String ddlAuto)  {
		
	    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	    entityManagerFactoryBean.setDataSource(dataSource);
	    entityManagerFactoryBean.setMappingResources("META-INF/hibernate/cimsession.hbm.xml");
	    entityManagerFactoryBean.setPackagesToScan(getClass().getPackage().getName());
	    
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", dialect);
		jpaProperties.setProperty("hibernate.show_sql", showSql);
		jpaProperties.setProperty("hibernate.naming-strategy", namingStrategy);
		jpaProperties.setProperty("hibernate.naming.physical-strategy", physicalStrategy);
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
		entityManagerFactoryBean.setJpaProperties(jpaProperties);
		
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.valueOf(database));
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		
	    return entityManagerFactoryBean;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory);
	    return transactionManager;
	}
	
	/*@Bean
	public LocalSessionFactoryBean sessionFactory(@Value("${spring.jpa.properties.hibernate.dialect}") String dialect,
			@Value("${spring.jpa.show-sql}") String showSql,
			@Value("${spring.jpa.hibernate.naming-strategy}") String namingStrategy,
			@Value("${spring.jpa.hibernate.naming.physical-strategy}") String physicalStrategy,
			@Value("${spring.jpa.hibernate.ddl-auto}") String ddlAuto) throws IOException {

		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resLoader);
		sessionFactoryBean.setMappingLocations(resolver.getResources("classpath:/hibernate/*.hbm.xml"));
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("hibernate.show_sql", showSql);
		properties.setProperty("hibernate.naming-strategy", namingStrategy);
		properties.setProperty("hibernate.naming.physical-strategy", physicalStrategy);
		properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
		sessionFactoryBean.setHibernateProperties(properties);
		return sessionFactoryBean;
	}*/
}