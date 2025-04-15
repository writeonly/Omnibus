package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses

@AnalyzeClasses(packages = ["pl.writeonly"])
class LoggingConventionTest {

//    @ArchTest
//    val noLoggerInService: ArchRule = fields()
//        .that().areDeclaredInClassesThat().resideInAPackage("..service..")
//        .and().haveRawType(org.slf4j.Logger::class.java)
//        .should().notExist()
}
