package bio.terra.javatemplate;

import bio.terra.common.iam.BearerToken;
import bio.terra.common.iam.BearerTokenFactory;
import bio.terra.common.iam.SamUser;
import bio.terra.common.iam.SamUserFactory;
import bio.terra.common.logging.LoggingInitializer;
import bio.terra.javatemplate.config.SamConfiguration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.annotation.RequestScope;

@SpringBootApplication(
    scanBasePackages = {
      // Scan for iam components & configs
      "bio.terra.common.iam",
      // Scan for logging-related components & configs
      "bio.terra.common.logging",
      // Scan for Liquibase migration components & configs
      "bio.terra.common.migrate",
      // Transaction management and DB retry configuration
      "bio.terra.common.retry.transaction",
      // Scan for tracing-related components & configs
      "bio.terra.common.tracing",
      // Scan all service-specific packages beneath the current package
      "bio.terra.javatemplate"
    })
@ConfigurationPropertiesScan("bio.terra.javatemplate")
@EnableRetry
@EnableTransactionManagement
@EnableConfigurationProperties
public class App {
  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class).initializers(new LoggingInitializer()).run(args);
  }

  private final DataSource dataSource;
  private final SamConfiguration samConfiguration;
  private final SamUserFactory samUserFactory;

  public App(
      DataSource dataSource, SamConfiguration samConfiguration, SamUserFactory samUserFactory) {
    this.dataSource = dataSource;
    this.samConfiguration = samConfiguration;
    this.samUserFactory = samUserFactory;
  }

  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule())
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
  }

  // This bean plus the @EnableTransactionManagement annotation above enables the use of the
  // @Transaction annotation to control the transaction properties of the data source.
  @Bean("transactionManager")
  public PlatformTransactionManager getTransactionManager() {
    return new JdbcTransactionManager(this.dataSource);
  }

  @Bean
  @RequestScope
  public BearerToken bearerToken(HttpServletRequest request) {
    return new BearerTokenFactory().from(request);
  }

  @Bean
  @RequestScope
  public SamUser samUser(HttpServletRequest request) {
    return samUserFactory.from(request, samConfiguration.basePath());
  }
}
