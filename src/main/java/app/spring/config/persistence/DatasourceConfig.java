package app.spring.config.persistence;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:properties/application-default.properties"),
        @PropertySource(value = "classpath:properties/application-${spring.profiles.active}.properties",
                ignoreResourceNotFound = true)
})
public class DatasourceConfig {

    @Value("${db.init.schema}")
    private Resource INIT_SCHEMA_SCRIPT;

    @Value("${db.init.data}")
    private Resource INIT_DATA_SCRIPT;

    @Value("${db.clean.schema}")
    private Resource CLEAN_SCHEMA_SCRIPT;

    private final Environment env;

    @Autowired
    public DatasourceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(env.getProperty("db.driver"));
        ds.setJdbcUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));

        // 최대 커넥션 개수 (기본값: 10개) - 가장 중요
        ds.setMaximumPoolSize(10);
        // 커넥션 타임아웃 값 (기본값: 30초)
        ds.setConnectionTimeout(30000);
        // 커넥션 최대 생명 시간 (기본값: 30분), 30초 이상 DB 기본 타임아웃 값 이하로 설정
        ds.setMaxLifetime(1800000);
        // 유휴 커넥션 타임아웃 값 (기본값: 10분), MinimumIdle값이 MaximumPoolSize이하로 설정되어있을 때만 유효
        ds.setIdleTimeout(600000);
        // 최소 유휴 커넥션 개수 (기본값: MaximumPoolSize), 설정안하는 것을 권장
        // ds.setMinimumIdle(10);

        // Caching (MySQL Recommended)
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts", "true");
        return ds;
    }

    @Profile("!production")
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {

        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        initializer.setDatabaseCleaner(databaseCleaner());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(INIT_SCHEMA_SCRIPT);
        populator.addScript(INIT_DATA_SCRIPT);
        return populator;
    }

    private DatabasePopulator databaseCleaner() {
        final ResourceDatabasePopulator cleaner = new ResourceDatabasePopulator();
        cleaner.addScript(CLEAN_SCHEMA_SCRIPT);
        return cleaner;
    }
}