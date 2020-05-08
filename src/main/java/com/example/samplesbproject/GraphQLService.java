package com.example.samplesbproject;


import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

//import com.google.common.collect.ImmutableMap;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;


@Service
public class GraphQLService {

  
    @Value("classpath:books.graphql")
    Resource resource;
    
    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;
    
    
    private GraphQL graphQL;
    

    // load schema at application start up
    @PostConstruct
    private void loadSchema() throws IOException {


        // get the schema
        File schemaFile = resource.getFile();
        // parsing the schema
        
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        
        //RuntimeWiring wiring = buildRuntimeWiring();
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
        
    }
  
   
    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type("Book", typeWiring -> typeWiring
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }

  
    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }
}

