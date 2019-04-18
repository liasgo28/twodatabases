package br.com.diego.twodatabases.configurations;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	 entityManagerFactoryRef = "dbOneEntityManagerFactory",
	 transactionManagerRef = "dbOneTransactionManager",
	 basePackages = {
	  "br.com.diego.twodatabases.repositories.dbone"
	 }
)
public class DbOneConfiguration {
	@Primary
	@Bean(name = "dbOneDataSource")
	@ConfigurationProperties(prefix = "dbone.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name = "dbOneEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean dbOneEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dbOneDataSource") DataSource dataSource) {
		return builder
		   .dataSource(dataSource)
		   .packages("br.com.diego.twodatabases.model.dbone")
		   .persistenceUnit("dbone")
		   .build();
	}

	@Primary
	@Bean(name = "dbOneTransactionManager")
	public PlatformTransactionManager dbOneTransactionManager(@Qualifier("dbOneEntityManagerFactory") EntityManagerFactory dbOneEntityManagerFactory) {
		return new JpaTransactionManager(dbOneEntityManagerFactory);
	}
	
}

