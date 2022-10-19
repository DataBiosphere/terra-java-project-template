package bio.terra.catalog.app;

import bio.terra.catalog.config.CatalogDatabaseConfiguration;
import bio.terra.common.migrate.LiquibaseMigrator;
import org.springframework.context.ApplicationContext;

public enum StartupInitializer {
  UNUSED;
  private static final String CHANGELOG_PATH = "db/changelog.xml";

  public static void initialize(ApplicationContext applicationContext) {
    // Initialize or upgrade the database depending on the configuration
    LiquibaseMigrator migrateService = applicationContext.getBean(LiquibaseMigrator.class);
    var databaseConfiguration = applicationContext.getBean(CatalogDatabaseConfiguration.class);

    // Migrate the database
    if (databaseConfiguration.isInitializeOnStart()) {
      migrateService.initialize(CHANGELOG_PATH, databaseConfiguration.getDataSource());
    } else if (databaseConfiguration.isUpgradeOnStart()) {
      migrateService.upgrade(CHANGELOG_PATH, databaseConfiguration.getDataSource());
    }
  }
}
