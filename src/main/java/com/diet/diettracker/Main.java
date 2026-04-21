package com.diet.diettracker;

// import com.diet.diettracker.Servlet.*;
import com.diet.diettracker.Servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;



public class Main {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // String webappDir = Main.class.getResource("/webapp") != null
        //         ? Main.class.getResource("/webapp").toExternalForm()
        //         : "src/main/webapp";
        context.setResourceBase("src/main/webapp");
//context.setResourceBase(webappDir);   
  context.setWelcomeFiles(new String[]{"index.html"});

        context.addServlet(new ServletHolder(new SignupServlet()),      "/api/signup");
        context.addServlet(new ServletHolder(new LoginServlet()),       "/api/login");
        context.addServlet(new ServletHolder(new LogoutServlet()),      "/api/logout");
        context.addServlet(new ServletHolder(new DietPlanServlet()),    "/api/diet-plans/*");
        context.addServlet(new ServletHolder(new FoodApiServlet()),     "/api/food-search");

        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("dirAllowed", "false");
        context.addServlet(defaultServlet, "/");

        server.setHandler(context);
        server.start();

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   🥗  Diet Tracker running on :8080      ║");
        System.out.println("║   Open → http://localhost:8080            ║");
        System.out.println("╚══════════════════════════════════════════╝");

        server.join();
    }
}