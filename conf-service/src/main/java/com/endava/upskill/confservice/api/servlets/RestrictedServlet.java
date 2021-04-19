package com.endava.upskill.confservice.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.CustomHeaders;
import com.endava.upskill.confservice.api.annotations.ServletComponent;

@ServletComponent(path = "/restricted")
public class RestrictedServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter writer = resp.getWriter();
        writer.println("Access Granted for user: " + req.getHeader(CustomHeaders.USERNAME));
    }
}
