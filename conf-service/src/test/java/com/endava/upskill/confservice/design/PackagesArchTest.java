package com.endava.upskill.confservice.design;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

@AnalyzeClasses(packages = {
        "com.endava.upskill.confservice.api",
        "com.endava.upskill.confservice.domain"})
class PackagesArchTest {

    private static final String API = "api";

    private static final String DOMAIN = "domain";

    private static final String API_PACKAGES = "com.endava.upskill.confservice.api..";

    private static final String DOMAIN_PACKAGES = "com.endava.upskill.confservice.domain..";

    @ArchTest
    public static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .layer(API).definedBy(API_PACKAGES)
            .layer(DOMAIN).definedBy(DOMAIN_PACKAGES)
            .whereLayer(API).mayNotBeAccessedByAnyLayer()
            .whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(API)
            .as("Layered Architecture is applied to the project");

    @ArchTest
    static final ArchRule no_accesses_to_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

    @ArchTest
    public static final ArchRule servlets_should_only_be_dependable_by_api = noClasses()
            .that().resideOutsideOfPackages(API_PACKAGES)
            .should().dependOnClassesThat().resideInAPackage("javax.servlet..")
            .as("Classes outside of api packages should not depend on web libraries like javax.servlet");

    @ArchTest
    static final ArchRule services_should_only_be_accessed_by_limited_packages = classes()
            .that().resideInAPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..api.controller..", "..domain.service..",
                    "..api.filter..")
            .as("Services should only be accessed by controllers, filters or other services");

    @ArchTest
    static final ArchRule repository_should_only_be_accessed_by_services = classes()
            .that().resideInAPackage("..domain.dao..")
            .should().onlyBeAccessed().byAnyPackage("..domain.dao..", "..domain.service..")
            .as("Repositories should only be accessed by services or other repositories");
}
