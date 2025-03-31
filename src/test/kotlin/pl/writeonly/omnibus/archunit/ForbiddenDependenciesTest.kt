package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = ["pl.writeonly"])
class ForbiddenDependenciesTest {

    @ArchTest
    val noOldJavaDateUsage: ArchRule = noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("java.util.Date")
}

