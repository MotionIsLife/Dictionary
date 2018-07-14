package testPackage.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * JpaConfig
 */
@Configuration
@PropertySource("classpath:application-dev_postgres.properties")
public class JpaConfig {

  @Autowired
  private DataSource dataSource;

  @Resource
  private Environment env;

  private String[] entityPackagesToScan = {"testPackage"};


  private static final String PROP_HIBERNATE_DIALECT = "db.hibernate.dialect";
  private static final String PROP_HIBERNATE_SHOW_SQL = "db.hibernate.show_sql";
  //private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "db.entitymanager.packages.to.scan";
  private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "db.hibernate.hbm2ddl.auto";
  private static final String PROP_HIBERNATE_FORMAT_SQL = "db.hibernate.format_sql";


  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
    lef.setDataSource(dataSource);
    lef.setJpaVendorAdapter(jpaVendorAdapter());
    lef.setPackagesToScan(entityPackagesToScan);
    lef.setJpaProperties(getHibernateProperties());
    return lef;
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(false);
    hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
    return hibernateJpaVendorAdapter;
  }

  private Properties getHibernateProperties() {
    Properties properties = new Properties();
    properties.put("hibernate.dialect", env.getRequiredProperty(PROP_HIBERNATE_DIALECT));
    properties.put("hibernate.show_sql", env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
    properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
    /*properties.put("hibernate.format_sql", env.getRequiredProperty(PROP_HIBERNATE_FORMAT_SQL));*/
    return properties;
  }

  @Bean
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }
}
