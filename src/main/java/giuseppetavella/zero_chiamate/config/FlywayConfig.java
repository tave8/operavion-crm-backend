package giuseppetavella.zero_chiamate.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    /**
     * Manually configures and runs Flyway for database migrations.
     *
     * WHY THIS IS MANUAL:
     * Normally, Spring Boot auto-configures Flyway by detecting it on the classpath
     * and reading properties like "spring.flyway.*" from application.properties.
     * However, Spring Boot 4.0.5 (bleeding edge) has a broken or changed
     * auto-configuration contract with Flyway 12.x — Flyway was on the classpath
     * but no bean was created, so migrations never ran.
     * This bean bypasses auto-configuration entirely and wires Flyway manually.
     *
     * WHAT THIS DOES:
     * - Connects Flyway to the application's DataSource (same DB as Hibernate)
     * - Tells Flyway where to find migration scripts (db/migration folder)
     * - Treats the existing schema as baseline V1, so Flyway does not attempt
     *   to recreate tables that already exist
     * - Calls migrate() on startup, which runs any pending migration scripts
     *   that have not yet been recorded in the flyway_schema_history table
     *
     * MIGRATION NAMING CONVENTION:
     * V2__description.sql, V3__description.sql, etc.
     * Flyway runs them in order, once per environment, and never runs them again.
     *
     * @param dataSource injected by Spring from application.properties DB config
     * @return configured and migrated Flyway instance
     */
    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .load();
        flyway.migrate();
        return flyway;
    }
    
}
