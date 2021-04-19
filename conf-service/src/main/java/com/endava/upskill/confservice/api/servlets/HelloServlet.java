package com.endava.upskill.confservice.api.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.endava.upskill.confservice.api.annotations.ServletComponent;
import com.endava.upskill.confservice.service.HelloService;

import lombok.RequiredArgsConstructor;

@ServletComponent(path = "/hello")
@RequiredArgsConstructor
public class HelloServlet extends HttpServlet {

    private final HelloService helloService;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        PrintWriter printWriter = resp.getWriter();
        printWriter.println(helloService.getResponse());
    }
}
