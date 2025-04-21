package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import jakarta.inject.Named
import org.springframework.aop.SpringProxy
import org.springframework.aot.generate.Generated

@AnalyzeClasses(packages = ["pl.writeonly"])
class NamingConventionTest {

    @ArchTest
    val rulesShouldHaveCorrectSuffix: ArchRule = classes()
        .that().resideInAPackage("..named..")
        .and().resideInAPackage("..rule..")
        .and().haveSimpleNameNotEndingWith("IT")
        .and().areNotNestedClasses()
        .and().areNotAnnotatedWith(Generated::class.java)
        .should().haveSimpleNameEndingWith("Rule")

    @ArchTest
    val namedBeansShouldHaveCorrectAnnotation: ArchRule = classes()
        .that().resideInAPackage("..named..")
        .and().haveSimpleNameNotEndingWith("IT")
        .and().areNotNestedClasses()
        .and().areNotAnnotatedWith(Generated::class.java)
        .should().beAnnotatedWith(Named::class.java)

    @ArchTest
    val controllersShouldHaveCorrectSuffix: ArchRule = classes()
        .that().resideInAPackage("..controller..")
        .and().areNotAnnotatedWith(Generated::class.java)
        .and().doNotImplement(SpringProxy::class.java)
        .should().haveSimpleNameEndingWith("Controller")

    @ArchTest
    val servicesShouldHaveCorrectSuffix: ArchRule = classes()
        .that().resideInAPackage("..service..")
        .and().areNotAnnotatedWith(Generated::class.java)
        .and().doNotImplement(SpringProxy::class.java)
        .and().areNotPrivate()
        .should().haveSimpleNameEndingWith("Service")

    @ArchTest
    val repositoriesShouldHaveCorrectSuffix: ArchRule = classes()
        .that().resideInAPackage("..repository..")
        .and().areNotAnnotatedWith(Generated::class.java)
        .should().haveSimpleNameEndingWith("Repository")

    @ArchTest
    val dtosShouldHaveCorrectSuffix: ArchRule = classes()
        .that().resideInAPackage("..dto..")
        .and().areNotAnnotatedWith(Generated::class.java)
        .should().haveSimpleNameEndingWith("Dto")

//    @ArchTest
//    val mappersShouldHaveCorrectSuffix: ArchRule = classes()
//        .that().resideInAPackage("..mapper..")
//        .and().areNotAnnotatedWith(Generated::class.java)
//        .should().haveSimpleNameEndingWith("Mapper")
}
