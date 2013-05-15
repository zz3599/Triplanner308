/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.User;
import com.triplanner.model.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author brook
 */
public class UserController {
    public static void doLoginPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("loginemail");
        String password = request.getParameter("loginpassword");
        User user = UserDAO.loginUser(email, password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid username/password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("app/home");
        }
    }

    /**
     * Handler for registration
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void doRegisterPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String first = request.getParameter("firstname");
        String last = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Missing required fields");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        User user = UserDAO.createUser(email.trim(), first.trim(), last.trim(), password);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid registration");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("user", user);
            response.sendRedirect("app/home.jsp");
        }
    }
}
