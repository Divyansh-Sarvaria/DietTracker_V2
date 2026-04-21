package com.diet.diettracker.Servlet;
import com.diet.services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

/**
 * POST /api/signup
 * Body: { "name": "...", "email": "...", "password": "..." }
 */
@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        // Parse JSON body
        var body = gson.fromJson(req.getReader(), Map.class);
        String name     = (String) body.get("name");
        String email    = (String) body.get("email");
        String password = (String) body.get("password");

        var result = userService.signup(name, email, password);

        if (result.success()) {
            // Create session
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", result.user().getId());
            session.setAttribute("userName", result.user().getName());

            res.setStatus(201);
            res.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", result.message(),
                    "userId",  result.user().getId(),
                    "name",    result.user().getName()
            )));
        } else {
            res.setStatus(400);
            res.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", result.message()
            )));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) {
        setCorsHeaders(res);
        res.setStatus(204);
    }

    private void setCorsHeaders(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin",  "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}