package br.com.diego.twodatabases.configurations;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	 entityManagerFactoryRef = "dbTwoEntityManagerFactory",
	 transactionManagerRef = "dbTwoTransactionManager",
	 basePackages = {
	  "br.com.diego.twodatabases.repositories.dbtwo"
	 }
)
public class DbTwoConfiguration {

	@Bean(name = "dbTwoDataSource")
	@ConfigurationProperties(prefix = "dbtwo.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "dbTwoEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean dbTwoEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dbTwoDataSource") DataSource dataSource) {
		return builder
		   .dataSource(dataSource)
		   .packages("br.com.diego.twodatabases.model.dbtwo")
		   .persistenceUnit("dbtwo")
		   .build();
	}

	@Bean(name = "dbTwoTransactionManager")
	public PlatformTransactionManager dbTwoTransactionManager(@Qualifier("dbTwoEntityManagerFactory") EntityManagerFactory dbTwoEntityManagerFactory) {
		return new JpaTransactionManager(dbTwoEntityManagerFactory);
	}
	
}

