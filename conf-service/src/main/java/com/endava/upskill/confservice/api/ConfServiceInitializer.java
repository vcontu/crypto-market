package com.endava.upskill.confservice.api;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;

import static javax.servlet.DispatcherType.REQUEST;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.endava.upskill.confservice.ConfServiceApplication;
import com.endava.upskill.confservice.api.annotations.FilterComponent;
import com.endava.upskill.confservice.api.annotations.ServletComponent;

public class ConfServiceInitializer implements ServletContainerInitializer {

    private ApplicationContext applicationContext;

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        applicationContext = new AnnotationConfigApplicationContext(ConfServiceApplication.class);
        addServletsToContext(ctx);
        addFiltersToContext(ctx);
    }

    private void addServletsToContext(ServletContext ctx) {
        Map<String, HttpServlet> servletList = applicationContext.getBeansOfType(HttpServlet.class);

        for(Map.Entry<String, HttpServlet> bean : servletList.entrySet()){
            final HttpServlet servlet = bean.getValue();
            final Annotation[] annotations = servlet.getClass().getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation instanceof ServletComponent){
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

        for(Map.Entry<String, HttpFilter> bean : filterList.entrySet()){
            final HttpFilter filter = bean.getValue();
            final Annotation[] annotations = filter.getClass().getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation instanceof FilterComponent){
                    final FilterComponent filterComponent = (FilterComponent) annotation;
                    final String path = filterComponent.path();
                    final String name = bean.getKey();

                    ctx.addFilter(name, filter).addMappingForUrlPatterns(EnumSet.of(REQUEST), true, path);
                }
            }
        }
    }
}
