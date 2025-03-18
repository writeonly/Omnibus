package pl.writeonly.omnibus

import pl.writeonly.omnibus.GenerateTestData.N
import pl.writeonly.omnibus.GenerateTestData.NN
import pl.writeonly.omnibus.GenerateTestData.NNN
import java.io.File
import java.sql.Timestamp
import java.time.LocalDateTime

object GenerateTestData {
    const val N = 10
    const val NN = 100
    const val NNN = 1000
}

fun main() {
    val dataSql = File("src/main/resources/data.sql")
    dataSql.writeText("")

    repeat(NN) { i ->
        val localDataTime = LocalDateTime.now().minusDays(NN - i.toLong())
        val timestamp = Timestamp.valueOf(localDataTime)
        dataSql.appendText(
            "INSERT INTO post(id, title, content, created)" +
                " VALUES($i, 'Test Post $i', 'Content $i', TIMESTAMP '$timestamp');\n"
        )
    }

    repeat(NNN) { i ->
        val postId = i / N
        val localDataTime = LocalDateTime.now().minusDays(NN - i.toLong())
        val timestamp = Timestamp.valueOf(localDataTime)
        dataSql.appendText(
            "INSERT INTO comment(id, post_id, content, created)" +
                " VALUES($i, $postId, 'Content $i', TIMESTAMP '$timestamp');\n"
        )
    }
}
