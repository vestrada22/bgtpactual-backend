package com.btgpactual.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MongoConfigIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void givenMongoConfig_whenConnectToDatabase_thenConnectionSuccessful() {
        // When
        boolean connectionSuccessful = mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1)).containsKey("ok");

        // Then
        assertTrue(connectionSuccessful, "Should be able to connect to MongoDB");
    }

    @Test
    void givenMongoConfig_whenGetDatabaseName_thenCorrectNameReturned() {
        // When
        String databaseName = mongoTemplate.getDb().getName();

        // Then
        assertEquals("bgtpactual", databaseName, "Database name should match the configured name");
    }

    @Test
    void givenMongoConfig_whenPerformCRUDOperations_thenOperationsSuccessful() {
        // Given
        String collectionName = "test_collection";
        org.bson.Document testDocument = new org.bson.Document("test_key", "test_value");

        // When - Create
        mongoTemplate.insert(testDocument, collectionName);

        // Then - Read
        org.bson.Document retrievedDocument = mongoTemplate.findOne(new Query(), org.bson.Document.class, collectionName);
        assertNotNull(retrievedDocument, "Should be able to retrieve the inserted document");
        assertEquals("test_value", retrievedDocument.getString("test_key"), "Retrieved document should have the correct value");

        // When - Update
        retrievedDocument.put("test_key", "updated_value");
        mongoTemplate.save(retrievedDocument, collectionName);

        // Then
        org.bson.Document updatedDocument = mongoTemplate.findOne(new Query(), org.bson.Document.class, collectionName);
        assertNotNull(updatedDocument, "Should be able to retrieve the updated document");
        assertEquals("updated_value", updatedDocument.getString("test_key"), "Updated document should have the new value");

        // When - Delete
        mongoTemplate.remove(updatedDocument, collectionName);

        // Then
        long count = mongoTemplate.count(new Query(), collectionName);
        assertEquals(0, count, "Collection should be empty after deletion");
    }
}