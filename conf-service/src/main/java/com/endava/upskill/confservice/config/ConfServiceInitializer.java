package com.endava.upskill.confservice.config;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;

import static javax.servlet.DispatcherType.REQUEST;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.endava.upskill.confservice.ConfServiceApplication;
import com.endava.upskill.confservice.api.annotation.FilterComponent;
import com.endava.upskill.confservice.api.annotation.ServletComponent;

public class ConfServiceInitializer implements ServletContainerInitializer {

    private ApplicationContext applicationContext;

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) {
        applicationContext = new AnnotationConfigApplicationContext(ConfServiceApplication.class);
        addServletsToContext(ctx);
        addFiltersToContextByAnnotatedPriority(ctx);
    }

    private void addServletsToContext(ServletContext ctx) {
        Map<String, HttpServlet> servletBeans = applicationContext.getBeansOfType(HttpServlet.class);
        for (var entry : servletBeans.entrySet()) {
            HttpServlet httpServlet = entry.getValue();
            if (httpServlet.getClass().isAnnotationPresent(ServletComponent.class)) {
                ServletComponent annotation = httpServlet.getClass().getAnnotation(ServletComponent.class);
                ctx.addServlet(entry.getKey(), httpServlet).addMapping(annotation.path());
            }
        }
    }

    private void addFiltersToContextByAnnotatedPriority(ServletContext ctx) {
        final List<HttpFilter> filtersByPriority = applicationContext.getBeansOfType(HttpFilter.class)
                .values().stream()
                .filter(httpFilter -> httpFilter.getClass().isAnnotationPresent(FilterComponent.class))
                .sorted(Comparator.comparingInt(this::getFilterPriority))
                .toList();

        for (var httpFilter : filtersByPriority) {
            FilterComponent annotation = httpFilter.getClass().getAnnotation(FilterComponent.class);
            ctx.addFilter("Filter" + getFilterPriority(httpFilter), httpFilter)
                    .addMappingForUrlPatterns(EnumSet.of(REQUEST), true, annotation.path());
        }
    }

    private int getFilterPriority(HttpFilter httpFilter) {
        return httpFilter.getClass().getAnnotation(FilterComponent.class).priority();
    }
}
