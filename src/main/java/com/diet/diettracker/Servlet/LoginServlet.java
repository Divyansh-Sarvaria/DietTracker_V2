package com.diet.diettracker.Servlet;

import com.diet.services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

/**
 * POST /api/login
 * Body: { "email": "...", "password": "..." }
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        var body    = gson.fromJson(req.getReader(), Map.class);
        String email    = (String) body.get("email");
        String password = (String) body.get("password");

        var result = userService.login(email, password);

        if (result.success()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("userId",   result.user().getId());
            session.setAttribute("userName", result.user().getName());

            res.setStatus(200);
            res.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", result.message(),
                    "userId",  result.user().getId(),
                    "name",    result.user().getName()
            )));
        } else {
            res.setStatus(401);
            res.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", result.message()
            )));
        }
    }
}