package pl.writeonly.omnibus

import org.springframework.boot.runApplication
import java.io.File
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class GenerateTestData

fun main(args: Array<String>) {

  val dataSql = File("src/main/resources/data.sql")
    dataSql.writeText("")

    repeat(100) { i ->
        val timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(100 - i.toLong()))
        dataSql.appendText(
            "INSERT INTO post(id, title, content, created)"
                    + " VALUES(${i}, 'Test Post ${i}', 'Content ${i}', TIMESTAMP '${timestamp}');\n"
        )
    }

    repeat(1000) { i ->
        val post_id = i/10
        val timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(100 - i.toLong()))
        dataSql.appendText(
            "INSERT INTO comment(id, post_id, content, created)"
                    + " VALUES(${i}, ${post_id}, 'Content ${i}', TIMESTAMP '${timestamp}');\n"
        )
    }
}
