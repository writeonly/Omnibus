package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses

@AnalyzeClasses(packages = ["pl.writeonly"])
class CyclicDependencyTest {

//    @ArchTest
//    val noCyclicDependencies: ArchRule = noClasses()
//        .should().dependOnClassesThat().dependOnClassesThat().dependOnClassesThat().resideInAnyPackage("..service..")
}
