package com.endava.internship.cryptomarket.confservice.application;

import com.endava.internship.cryptomarket.confservice.api.annotations.FilterComponent;
import com.endava.internship.cryptomarket.confservice.api.annotations.ServletComponent;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Annotation;
import java.util.*;

import static jakarta.servlet.DispatcherType.REQUEST;

public class ConfServiceApplication implements ServletContainerInitializer {

    private ApplicationContext applicationContext;

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        applicationContext = new AnnotationConfigApplicationContext(ConfServiceConfig.class);
        addServletsToContext(ctx);
        addFiltersToContext(ctx);
    }

    private void addServletsToContext(ServletContext ctx) {
        Map<String, HttpServlet> servletList = applicationContext.getBeansOfType(HttpServlet.class);

        for (Map.Entry<String, HttpServlet> bean : servletList.entrySet()) {
            final HttpServlet servlet = bean.getValue();
            final Annotation[] annotations = servlet.getClass().getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation instanceof ServletComponent) {
                    final ServletComponent servletComponent = (ServletComponent) annotation;
                    final String path = servletComponent.path();
                    final String name = bean.getKey();

                    ctx.addServlet(name, servlet).addMapping(path);
                }
            }
        }
    }

    private void addFiltersToContext(ServletContext ctx) {

        Map<String, HttpFilter> filterList = applicationContext.getBeansOfType(HttpFilter.class);

        class Filter {
            final HttpFilter filter;
            final String path;
            final int priority;

            Filter(HttpFilter filter, String path, int priority) {
                this.filter = filter;
                this.path = path;
                this.priority = priority;
            }
        }

        TreeSet<Filter> order = new TreeSet<>(Comparator.comparingInt((Filter f) -> f.priority));

        for (Map.Entry<String, HttpFilter> bean : filterList.entrySet()) {
            final HttpFilter filter = bean.getValue();
            final Annotation[] annotations = filter.getClass().getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation instanceof FilterComponent) {

                    final FilterComponent filterComponent = (FilterComponent) annotation;
                    final String path = filterComponent.path();
                    final int priority = filterComponent.priority();

                    order.add(new Filter(filter, path, priority));
                }
            }
        }
        order.iterator().forEachRemaining(f -> ctx.addFilter("Fitler" + f.priority, f.filter)
                .addMappingForUrlPatterns(EnumSet.of(REQUEST), true, f.path));
    }
}
