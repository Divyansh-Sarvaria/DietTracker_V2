package com.diet.diettracker.Servlet;

import com.diet.diettracker.model.DietPlan;
import com.diet.diettracker.model.DietPlan.MealItem;
import com.diet.services.DietPlanService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REST endpoints for diet plans.
 *
 * GET    /api/diet-plans        → list all plans for logged-in user
 * POST   /api/diet-plans        → create new plan
 * DELETE /api/diet-plans/{id}   → delete a plan
 */
@WebServlet("/api/diet-plans/*")
public class DietPlanServlet extends HttpServlet {

    private final DietPlanService dietPlanService = new DietPlanService();
    private final Gson gson = new Gson();

    // ── GET: list plans ────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        String userId = getSessionUserId(req);
        if (userId == null) { sendUnauth(res); return; }

        List<DietPlan> plans = dietPlanService.getPlansForUser(userId);
        res.setStatus(200);
        res.getWriter().write(gson.toJson(Map.of("success", true, "plans", plans)));
    }

    // ── POST: create plan ──────────────────────────────────────

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        String userId = getSessionUserId(req);
        if (userId == null) { sendUnauth(res); return; }

        Map<String, Object> body = gson.fromJson(req.getReader(), Map.class);

        DietPlan plan = new DietPlan();
        plan.setUserId(userId);
        plan.setPlanName((String) body.get("planName"));
        plan.setGoal((String) body.get("goal"));
        plan.setCalories(((Number) body.getOrDefault("calories", 2000)).intValue());
        plan.setCreatedAt(System.currentTimeMillis());

        // Parse meals array
        List<MealItem> meals = new ArrayList<>();
        List<Map<String, Object>> rawMeals = (List<Map<String, Object>>) body.get("meals");
        if (rawMeals != null) {
            for (Map<String, Object> rm : rawMeals) {
                MealItem mi = new MealItem();
                mi.setMealType((String) rm.get("mealType"));
                mi.setFoodName((String) rm.get("foodName"));
                mi.setQuantity(((Number) rm.getOrDefault("quantity", 100)).doubleValue());
                mi.setUnit((String) rm.getOrDefault("unit", "g"));
                mi.setKcal(((Number) rm.getOrDefault("kcal", 0)).intValue());
                meals.add(mi);
            }
        }
        plan.setMeals(meals);

        var result = dietPlanService.createPlan(plan);

        if (result.success()) {
            res.setStatus(201);
            res.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", result.message(),
                    "planId",  result.data())));
        } else {
            res.setStatus(400);
            res.getWriter().write(gson.toJson(Map.of("success", false, "message", result.message())));
        }
    }

    // ── DELETE: remove plan ────────────────────────────────────

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        String userId = getSessionUserId(req);
        if (userId == null) { sendUnauth(res); return; }

        String pathInfo = req.getPathInfo();          // e.g. "/abc123"
        if (pathInfo == null || pathInfo.equals("/")) {
            res.setStatus(400);
            res.getWriter().write("{\"success\":false,\"message\":\"Plan ID required\"}");
            return;
        }
        String planId = pathInfo.substring(1);        // strip leading /

        boolean deleted = dietPlanService.deletePlan(planId, userId);
        if (deleted) {
            res.setStatus(200);
            res.getWriter().write("{\"success\":true,\"message\":\"Plan deleted.\"}");
        } else {
            res.setStatus(404);
            res.getWriter().write("{\"success\":false,\"message\":\"Plan not found.\"}");
        }
    }

    // ── Helpers ────────────────────────────────────────────────

    private String getSessionUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute("userId");
    }

    private void sendUnauth(HttpServletResponse res) throws IOException {
        res.setStatus(401);
        res.getWriter().write("{\"success\":false,\"message\":\"Not authenticated.\"}");
    }
}