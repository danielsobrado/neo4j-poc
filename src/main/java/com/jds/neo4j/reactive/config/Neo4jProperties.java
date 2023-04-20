package com.jds.neo4j.reactive.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "neo4j")
public class Neo4jProperties {
    private String uri;
    private String username;
    private String password;

}
