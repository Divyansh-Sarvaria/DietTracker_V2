package com.diet.diettracker.dao;
import com.diet.diettracker.config.MongoConfig;
import com.diet.diettracker.model.DietPlan;
import com.diet.diettracker.model.DietPlan.MealItem;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DietPlanDAO {

    private static final String COLLECTION = "diet_plans";

    private MongoCollection<Document> getCollection() {
        MongoDatabase db = MongoConfig.getDatabase();
        return db.getCollection(COLLECTION);
    }


    public String save(DietPlan plan) {
        List<Document> mealDocs = new ArrayList<>();
        if (plan.getMeals() != null) {
            for (MealItem m : plan.getMeals()) {
                mealDocs.add(new Document()
                        .append("mealType", m.getMealType())
                        .append("foodName", m.getFoodName())
                        .append("quantity", m.getQuantity())
                        .append("unit",     m.getUnit())
                        .append("kcal",     m.getKcal()));
            }
        }

        Document doc = new Document()
                .append("userId",    plan.getUserId())
                .append("planName",  plan.getPlanName())
                .append("goal",      plan.getGoal())
                .append("calories",  plan.getCalories())
                .append("meals",     mealDocs)
                .append("createdAt", System.currentTimeMillis());

        getCollection().insertOne(doc);
        return doc.getObjectId("_id").toHexString();
    }


    public List<DietPlan> findByUserId(String userId) {
        List<DietPlan> plans = new ArrayList<>();
        for (Document doc : getCollection().find(eq("userId", userId))) {
            plans.add(docToPlan(doc));
        }
        return plans;
    }


    public DietPlan findByIdAndUserId(String id, String userId) {
        Document doc = getCollection()
                .find(and(eq("_id", new ObjectId(id)), eq("userId", userId)))
                .first();
        return doc == null ? null : docToPlan(doc);
    }


    public boolean delete(String id, String userId) {
        var result = getCollection()
                .deleteOne(and(eq("_id", new ObjectId(id)), eq("userId", userId)));
        return result.getDeletedCount() > 0;
    }


    @SuppressWarnings("unchecked")
    private DietPlan docToPlan(Document doc) {
        DietPlan plan = new DietPlan();
        plan.setId(doc.getObjectId("_id").toHexString());
        plan.setUserId(doc.getString("userId"));
        plan.setPlanName(doc.getString("planName"));
        plan.setGoal(doc.getString("goal"));
        plan.setCalories(doc.getInteger("calories", 0));
        plan.setCreatedAt(doc.getLong("createdAt") != null ? doc.getLong("createdAt") : 0);

        List<MealItem> meals = new ArrayList<>();
        List<Document> mealDocs = (List<Document>) doc.get("meals");
        if (mealDocs != null) {
            for (Document md : mealDocs) {
                MealItem mi = new MealItem();
                mi.setMealType(md.getString("mealType"));
                mi.setFoodName(md.getString("foodName"));
                mi.setQuantity(md.getDouble("quantity") != null ? md.getDouble("quantity") : 0);
                mi.setUnit(md.getString("unit"));
                mi.setKcal(md.getInteger("kcal", 0));
                meals.add(mi);
            }
        }
        plan.setMeals(meals);
        return plan;
    }
}