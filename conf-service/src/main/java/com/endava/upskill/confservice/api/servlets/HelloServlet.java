package com.endava.upskill.confservice.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.service.HelloService;
import com.endava.upskill.confservice.service.HelloServiceImpl;

import lombok.Setter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Setter
    private HelloService helloService;

    @Override
    public void init() {
        helloService = new HelloServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter printWriter = resp.getWriter();
        printWriter.println(helloService.getResponse());
    }
}
