package com.endava.internship.cryptomarket.confservice.api.servlets;

import com.endava.internship.cryptomarket.confservice.service.annotations.ServletAnnotation;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@ServletAnnotation(path = "/restricted")
public class RestrictedServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter writer = resp.getWriter();
        writer.println("Access Granted for user: " + req.getHeader("username"));
    }

}
