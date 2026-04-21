
package com.diet.diettracker.config;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConfig {

    private static final String MONGO_URI = "mongodb+srv://divyanshsarvaria_db_user:39m7tVBl3xQk8hul@cluster0.kjdbq3e.mongodb.net/?appName=Cluster0";
    private static final String DB_NAME   = "diet_tracker";

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private MongoConfig() {}

    public static synchronized MongoDatabase getDatabase() {
        if (database == null) {
            mongoClient = MongoClients.create(MONGO_URI);
            database    = mongoClient.getDatabase(DB_NAME);
            System.out.println("[MongoDB] Connected to: " + DB_NAME);
        }
        return database;
    }

    public static synchronized void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("[MongoDB] Connection closed.");
        }
    }
}