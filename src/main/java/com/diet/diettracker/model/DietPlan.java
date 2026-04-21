package com.diet.diettracker.model;

import java.util.List;


public class DietPlan {

    private String id;          
    private String userId;      
    private String planName;    
    private String goal;        
    private int    calories;    
    private List<MealItem> meals;
    private long   createdAt;

    public DietPlan() {}


    public static class MealItem {
        private String mealType;
        private String foodName;
        private double quantity;
        private String unit;      
        private int    kcal;

        public MealItem() {}

        public String getMealType()              { return mealType; }
        public void   setMealType(String t)      { this.mealType = t; }

        public String getFoodName()              { return foodName; }
        public void   setFoodName(String n)      { this.foodName = n; }

        public double getQuantity()              { return quantity; }
        public void   setQuantity(double q)      { this.quantity = q; }

        public String getUnit()                  { return unit; }
        public void   setUnit(String u)          { this.unit = u; }

        public int  getKcal()                    { return kcal; }
        public void setKcal(int k)               { this.kcal = k; }
    }

    // ── Getters & Setters ──────────────────────────────────────

    public String getId()                   { return id; }
    public void   setId(String id)          { this.id = id; }

    public String getUserId()               { return userId; }
    public void   setUserId(String uid)     { this.userId = uid; }

    public String getPlanName()             { return planName; }
    public void   setPlanName(String n)     { this.planName = n; }

    public String getGoal()                 { return goal; }
    public void   setGoal(String g)         { this.goal = g; }

    public int  getCalories()               { return calories; }
    public void setCalories(int c)          { this.calories = c; }

    public List<MealItem> getMeals()            { return meals; }
    public void           setMeals(List<MealItem> m) { this.meals = m; }

    public long getLong()                  { return createdAt; }
    public void setCreatedAt(long ts)      { this.createdAt = ts; }
}