package pl.writeonly.omnibus.archunit

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields

@AnalyzeClasses(packages = ["pl.writeonly"])
class LoggingConventionTest {

//    @ArchTest
//    val noLoggerInService: ArchRule = fields()
//        .that().areDeclaredInClassesThat().resideInAPackage("..service..")
//        .and().haveRawType(org.slf4j.Logger::class.java)
//        .should().notExist()
}
