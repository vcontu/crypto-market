package com.endava.internship.cryptomarket.confservice.api.servlets;

import com.endava.internship.cryptomarket.confservice.service.HelloService;
import com.endava.internship.cryptomarket.confservice.service.HelloServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Setter
    private HelloService helloService;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter printWriter = resp.getWriter();
        printWriter.println(helloService.getResponse());
    }

    @Override
    public void init() {
        helloService = new HelloServiceImpl();
    }
}
