package com.github.andre10dias.spring_rest_api.integrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.1.0")
                .withDatabaseName("spring-rest-api")
                .withUsername("root")
                .withPassword("root")
                .withStartupTimeout(Duration.ofSeconds(60))
                .waitingFor(Wait.forLogMessage(".*ready for connections.*\\n", 1));

        private static void startContainers() {
            Startables.deepStart(Stream.of(mySQLContainer)).join();
        }

        private static Map<String, Object> createConnectionConfiguration(MySQLContainer<?> mySQLContainer) {
            return Map.of(
                    "spring.datasource.url", mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username", mySQLContainer.getUsername(),
                    "spring.datasource.password", mySQLContainer.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            startContainers();
            ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
            MapPropertySource propertySource = new MapPropertySource(
                    "testcontainers",
                    createConnectionConfiguration(mySQLContainer)
            );
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}
