package com.diet.diettracker.Servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * GET /api/food-search?q=apple
 *
 * Proxies the Open Food Facts API — free, no API key needed.
 * Returns simplified list: [ { name, kcal, protein, carbs, fat } ]
 */
@WebServlet("/api/food-search")
public class FoodApiServlet extends HttpServlet {

    private static final String OPEN_FOOD_API =
            "https://world.openfoodfacts.org/cgi/search.pl?action=process&json=true&page_size=10&fields=product_name,nutriments&search_terms=";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        String query = req.getParameter("q");
        if (query == null || query.isBlank()) {
            res.setStatus(400);
            res.getWriter().write("{\"success\":false,\"message\":\"Query param 'q' is required.\"}");
            return;
        }

        String encoded = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        String url = OPEN_FOOD_API + encoded;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "DietTracker/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Forward raw JSON back to client (frontend parses it)
            res.setStatus(200);
            res.getWriter().write(response.body());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            res.setStatus(502);
            res.getWriter().write(gson.toJson(Map.of("success", false, "message", "Food API unavailable.")));
        }
    }
}