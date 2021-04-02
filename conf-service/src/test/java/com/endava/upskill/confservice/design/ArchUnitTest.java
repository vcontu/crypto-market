package com.endava.upskill.confservice.design;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.endava.upskill.confservice")
public class ArchUnitTest {

    private static final String API = "api";

    private static final String SERVICE = "service";

    private static final String API_PACKAGES = "com.endava.upskill.confservice.api..";

    @ArchTest
    public static final ArchRule LAYER_DEPENDENCIES_ARE_RESPECTED = layeredArchitecture()
            .layer(API).definedBy(API_PACKAGES)
            .layer(SERVICE).definedBy("com.endava.upskill.confservice.service..")
            .whereLayer(API).mayNotBeAccessedByAnyLayer()
            .whereLayer(SERVICE).mayOnlyBeAccessedByLayers(API);

    @ArchTest
    public static final ArchRule SERVLET_API_DEPENDENCY = noClasses()
            .that().resideOutsideOfPackage(API_PACKAGES)
            .should().dependOnClassesThat().resideInAPackage("javax.servlet..");
}
