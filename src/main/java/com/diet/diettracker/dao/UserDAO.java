package com.diet.diettracker.dao;

import com.diet.diettracker.config.MongoConfig;
import com.diet.diettracker.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {

    private static final String COLLECTION = "users";

    private MongoCollection<Document> getCollection() {
        MongoDatabase db = MongoConfig.getDatabase();
        return db.getCollection(COLLECTION);
    }


    public String save(User user) {
        Document doc = new Document()
                .append("name",         user.getName())
                .append("email",        user.getEmail())
                .append("passwordHash", user.getPasswordHash())
                .append("createdAt",    System.currentTimeMillis());

        getCollection().insertOne(doc);
        return doc.getObjectId("_id").toHexString();
    }


    public User findByEmail(String email) {
        Document doc = getCollection().find(eq("email", email)).first();
        return doc == null ? null : docToUser(doc);
    }


    public User findById(String id) {
        Document doc = getCollection().find(eq("_id", new ObjectId(id))).first();
        return doc == null ? null : docToUser(doc);
    }


    public boolean emailExists(String email) {
        return getCollection().countDocuments(eq("email", email)) > 0;
    }


    private User docToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id").toHexString());
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPasswordHash(doc.getString("passwordHash"));
        return user;
    }
}