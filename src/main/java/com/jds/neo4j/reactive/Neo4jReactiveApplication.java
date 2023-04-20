package com.jds.neo4j.reactive;

import com.jds.neo4j.reactive.util.GenerateSamples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.jds.neo4j.reactive")
public class Neo4jReactiveApplication {

    @Autowired
    private GenerateSamples generateSamples;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Neo4jReactiveApplication.class, args);
        GenerateSamples generateSamples = context.getBean(GenerateSamples.class);
        generateSamples.generateSampleData();
    }


}
