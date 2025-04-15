package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses

@AnalyzeClasses(packages = ["pl.writeonly"])
class ArchitectureTest {

//    @ArchTest
//    val layeredArchitecture: ArchRule = Architectures.layeredArchitecture()
//        .layer("Controller").definedBy("..controller..")
//        .layer("Service").definedBy("..service..")
//        .layer("Repository").definedBy("..repository..")
//
//        .whereLayer("Controller").mayOnlyBeAccessedByLayers("Service")
//        .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Repository")
//        .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
}
