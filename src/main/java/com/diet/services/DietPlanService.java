package com.diet.services;

import com.diet.diettracker.dao.DietPlanDAO;
import com.diet.diettracker.model.DietPlan;
import java.util.List;

public class DietPlanService {

    private final DietPlanDAO dietPlanDAO = new DietPlanDAO();


    public ServiceResult<String> createPlan(DietPlan plan) {
        if (plan.getPlanName() == null || plan.getPlanName().isBlank())
            return new ServiceResult<>(false, "Plan name is required.", null);
        if (plan.getUserId() == null || plan.getUserId().isBlank())
            return new ServiceResult<>(false, "User ID is required.", null);
        if (plan.getCalories() <= 0)
            return new ServiceResult<>(false, "Calorie target must be positive.", null);

        String id = dietPlanDAO.save(plan);
        return new ServiceResult<>(true, "Diet plan saved.", id);
    }


    public List<DietPlan> getPlansForUser(String userId) {
        return dietPlanDAO.findByUserId(userId);
    }


    public DietPlan getPlan(String planId, String userId) {
        return dietPlanDAO.findByIdAndUserId(planId, userId);
    }

    public boolean deletePlan(String planId, String userId) {
        return dietPlanDAO.delete(planId, userId);
    }


    public record ServiceResult<T>(boolean success, String message, T data) {}
}