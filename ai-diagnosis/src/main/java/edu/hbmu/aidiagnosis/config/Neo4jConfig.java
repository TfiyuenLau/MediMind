package edu.hbmu.aidiagnosis.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class Neo4jConfig {

    @Value("${spring.data.neo4j.uri}")
    private String uri;
    @Value("${spring.data.neo4j.username}")
    private String userName;
    @Value("${spring.data.neo4j.password}")
    private String password;

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        return new org.neo4j.ogm.config.Configuration.Builder().uri(uri).connectionPoolSize(100)
                .credentials(userName, password).withBasePackages("com.hbmu.aidiagnosis.dao").build();
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(getConfiguration(), "edu.hbmu.aidiagnosis.domain.node");
    }

    /**
     * 默认事务管理器——解决事务处理bug
     *
     * @param dataSource
     * @return
     */
    @Bean("transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("neo4jTransaction")
    public Neo4jTransactionManager neo4jTransactionManager(SessionFactory sessionFactory) {
        return new Neo4jTransactionManager(sessionFactory);
    }

}
