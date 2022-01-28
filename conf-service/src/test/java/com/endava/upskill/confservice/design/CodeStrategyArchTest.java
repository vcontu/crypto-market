package com.endava.upskill.confservice.design;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {
        "com.endava.upskill.confservice.api",
        "com.endava.upskill.confservice.domain"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
class CodeStrategyArchTest {

    @ArchTest
    public static final ArchRule doNotCallNowWithoutClock = noClasses()
            .should().callMethod(LocalDateTime.class, "now")
            .orShould().callMethod(LocalDate.class, "now")
            .orShould().callMethod(LocalTime.class,"now")
            .as("Invocations of now() methods should be done with Clock parameter");
}
