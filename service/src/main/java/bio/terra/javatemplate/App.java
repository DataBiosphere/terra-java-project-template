package bio.terra.javatemplate;

import bio.terra.common.logging.LoggingInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

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
public class App {
  public static void main(String[] args) {
    new SpringApplicationBuilder(App.class).initializers(new LoggingInitializer()).run(args);
  }
}
