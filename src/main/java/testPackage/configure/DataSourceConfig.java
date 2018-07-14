package testPackage.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * DataSourceConfig
 */
@Configuration
@PropertySource("classpath:application-dev_postgres.properties")
public class DataSourceConfig {

  @Resource
  private Environment env;

  private static final String PROP_DATABASE_DRIVER = "db.driver";
  private static final String PROP_DATABASE_PASSWORD = "db.password";
  private static final String PROP_DATABASE_URL = "db.url";
  private static final String PROP_DATABASE_USERNAME = "db.username";

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
    dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
    dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
    dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));
    return dataSource;
  }
}
