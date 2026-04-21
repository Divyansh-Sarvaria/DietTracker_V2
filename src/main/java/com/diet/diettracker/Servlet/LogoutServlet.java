package com.diet.diettracker.Servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;


@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();

        res.setStatus(200);
        res.getWriter().write(gson.toJson(Map.of("success", true, "message", "Logged out.")));
    }
}