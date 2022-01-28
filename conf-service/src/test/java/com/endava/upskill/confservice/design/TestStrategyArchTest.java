package com.endava.upskill.confservice.design;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {
        "com.endava.upskill.confservice"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
class TestStrategyArchTest {

    private static final ProfileTestAnnotation profileTestAnnotation = new ProfileTestAnnotation();

    @ArchTest
    public static final ArchRule doNotUseProfileTest = noClasses()
            .should().beAnnotatedWith(profileTestAnnotation)
            .as("Production code should not use @Profile(\"*test*\")");

    private static final class ProfileTestAnnotation extends DescribedPredicate<JavaAnnotation<?>> {

        private ProfileTestAnnotation() {
            super("Spring @Profile(\"*test*\")");
        }

        @Override
        public boolean apply(JavaAnnotation<?> input) {
            return Stream.of(input)
                    .filter(javaAnnotation -> javaAnnotation.getRawType().isAssignableFrom(Profile.class))
                    .map(javaAnnotation -> javaAnnotation.as(Profile.class))
                    .map(Profile::value)
                    .flatMap(Arrays::stream)
                    .anyMatch(s -> s.toLowerCase().contains("test"));
        }
    }
}
