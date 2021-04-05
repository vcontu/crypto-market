package com.endava.internship.cryptomarket.confservice.api.servlets;

import com.endava.internship.cryptomarket.confservice.service.HelloService;
import com.endava.internship.cryptomarket.confservice.service.annotations.ServletAnnotation;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@ServletAnnotation(path = "/hello")
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
