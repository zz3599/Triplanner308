/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.User;
import com.triplanner.model.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller servlet provides interface between the web page and the
 * server side model and entities.
 *
 * @author brook
 */
@WebServlet(
        urlPatterns = {
    "/login",
    "/register"
},
        asyncSupported = true)
public class ControllerServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resource = getResource(request);
        if(resource.equals("app/home")){
            System.out.println("home");
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String resource = getResource(request);
        if (resource.equals("/login")) {
            doLoginPost(request, response);
        } else if (resource.equals("/register")) {
            doRegisterPost(request, response);
        }
    }

    private void doLoginPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("loginemail");
        String password = request.getParameter("loginpassword");
        User user = UserDAO.loginUser(email, password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid username/password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession(true).setAttribute("user", user);
            response.sendRedirect("app/home.jsp");
        }
    }
    
    private void doRegisterPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String first = request.getParameter("firstname");
        String last = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if(first.isEmpty() || last.isEmpty() || email.isEmpty() || password.isEmpty()){
            request.setAttribute("errorMessage", "Missing required fields");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        User user = UserDAO.createUser(email, first, last, password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid registration");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession(true).setAttribute("user", user);
            response.sendRedirect("app/home.jsp");
        }
    }

    private String getResource(HttpServletRequest request) {
        String requestString = request.getRequestURI();
        return requestString.substring(requestString.lastIndexOf("/"));
    }
}
