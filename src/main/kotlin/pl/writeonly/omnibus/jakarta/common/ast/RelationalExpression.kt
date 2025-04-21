package pl.writeonly.omnibus.jakarta.common.ast

enum class RelationalOperator {
    EQ,
    NE,
    LT,
    GT,
    LE,
    GE
}

data class RelationalExpression(
    val operator: RelationalOperator,
    val left: Value,
    val right: Value,
)
