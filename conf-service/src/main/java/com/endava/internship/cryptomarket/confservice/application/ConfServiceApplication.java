package com.endava.internship.cryptomarket.confservice.application;

import com.endava.internship.cryptomarket.confservice.service.annotations.FilterAnnotation;
import com.endava.internship.cryptomarket.confservice.service.annotations.ServletAnnotation;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

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

        for(Map.Entry<String, HttpServlet> bean : servletList.entrySet()){
            final HttpServlet servlet = bean.getValue();
            final Annotation[] annotations = servlet.getClass().getAnnotations();

            for(Annotation annotation : annotations){
                if(annotation instanceof ServletAnnotation){
                    final ServletAnnotation servletAnnotation = (ServletAnnotation) annotation;
                    final String path = servletAnnotation.path();
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
                if(annotation instanceof FilterAnnotation){
                    final FilterAnnotation filterAnnotation = (FilterAnnotation) annotation;
                    final String path = filterAnnotation.path();
                    final String name = bean.getKey();

                    ctx.addFilter(name, filter).addMappingForUrlPatterns(EnumSet.of(REQUEST), true, path);
                }
            }
        }
    }
}
