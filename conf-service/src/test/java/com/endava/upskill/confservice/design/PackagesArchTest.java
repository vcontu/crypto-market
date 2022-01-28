package com.endava.upskill.confservice.design;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

@AnalyzeClasses(packages = {
        "com.endava.upskill.confservice.api",
        "com.endava.upskill.confservice.domain",
        "com.endava.upskill.confservice.persistence"
},
        importOptions = ImportOption.DoNotIncludeTests.class)
public class PackagesArchTest {

    private static final String API = "api";

    private static final String API_PACKAGES = "com.endava.upskill.confservice.api..";

    private static final String PERSISTENCE = "persistence";

    private static final String PERSISTENCE_PACKAGES = "com.endava.upskill.confservice.persistence..";

    private static final String SERVICE_PACKAGES = "com.endava.upskill.confservice.domain.service..";

    private static final String DAO_PACKAGES = "com.endava.upskill.confservice.domain.dao..";

    private static final String DOMAIN_MODEL = "com.endava.upskill.confservice.domain.model..";

    private static final String VALIDATION_PACKAGES = "com.endava.upskill.confservice.domain.validation";

    @Test
    void hexagonalArchitecture() {
        final JavaClasses javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.endava.upskill.confservice");

        ArchRule hexagonalArchitectureRule = onionArchitecture()
                .domainModels(DOMAIN_MODEL)
                .domainServices(SERVICE_PACKAGES, DAO_PACKAGES)
                .applicationServices("com.endava.upskill.confservice", "com.endava.upskill.confservice.application" )
                .adapter(API, API_PACKAGES)
                .adapter(PERSISTENCE, PERSISTENCE_PACKAGES)
                .as("Application implements Hexagonal Architecture");

        hexagonalArchitectureRule.check(javaClasses);
    }

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
                    "..api.interceptor..")
            .as("Services should only be accessed by controllers, interceptors or other services");

    @ArchTest
    static final ArchRule dao_should_only_be_interfaces_and_accessed_by_services_only = classes()
            .that().resideInAPackage("..domain.dao..")
            .should().beInterfaces().andShould().onlyBeAccessed().byAnyPackage("..domain.service..")
            .as("Dao should only be interfaces and accessed by services");

    @ArchTest
    static final ArchRule repository_implementations_should_reside_in_persistence = classes()
            .that().resideInAPackage("..persistence..")
            .should().implement(new AnyClassFromPackage(DAO_PACKAGES));

    private static class AnyClassFromPackage extends DescribedPredicate<JavaClass> {

        private final JavaClasses interfaces;

        public AnyClassFromPackage(String packageName) {
            super("at least one interface by implementing " + packageName);
            this.interfaces = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages(packageName);
        }

        @Override
        public boolean apply(JavaClass input) {
            return interfaces.stream()
                    .anyMatch(daoInterface -> daoInterface.isAssignableTo(input.reflect()));
        }
    }
}
