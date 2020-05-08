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
//import java.util.stream.Stream;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;

@Service
public class GraphQLService {

    //@Autowired
   // BookRepository bookRepository;


	//@Value("classpath:schema.graphql")
    @Value("classpath:books.graphql")
    Resource resource;
    
    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;
    
    
    private GraphQL graphQL;
    
  // @Autowired
   // private AllBooksDataFetcher allBooksDataFetcher;
    
   // @Autowired
   // private BookDataFetcher bookDataFetcher;
    
    /*public static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );
    
    public static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );
    */

 /*   public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
        */

    // load schema at application start up
    @PostConstruct
    private void loadSchema() throws IOException {

        //Load Books into the Book Repository
       // loadDataIntoHSQL();
    	

        // get the schema
        File schemaFile = resource.getFile();
        // parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        //RuntimeWiring wiring = buildRuntimeWiring();
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
        
        
        
    }

   /* private void loadDataIntoHSQL() {
    	
    	
        Stream.of(
              new Book("1", "Let Us C", "4th Edition",
                        new String[] {
                        "Yashwanth", "Kanetkar"
                        }, "Nov 2002"),
                new Book("2", "Database System Concepts", "6th Edition",
                        new String[] {
                                "Avi", "Silverchatz"
                        }, "Dec 2013")
                
        ).forEach(book -> {
            bookRepository.save(book);
        });
        
    	
                
      } */


   /* private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("allBooks", allBooksDataFetcher)
                        .dataFetcher("book", bookDataFetcher))
                .build();
    }
   */
    
    /*private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }
    */
    private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
            .type("Query", typeWiring -> typeWiring
                    .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                    .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
            .build();
}

    
    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}

