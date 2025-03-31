package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = ["pl.writeonly"])
class CyclicDependencyTest {

//    @ArchTest
//    val noCyclicDependencies: ArchRule = noClasses()
//        .should().dependOnClassesThat().dependOnClassesThat().dependOnClassesThat().resideInAnyPackage("..service..")
}
