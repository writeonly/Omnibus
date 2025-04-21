package pl.writeonly.omnibus.jakarta.common.ast

enum class BooleanOperator {
    AND,
    OR,
}

sealed interface BooleanExpression

data class RelationalBooleanExpression(val relational: RelationalExpression) : BooleanExpression
data class ListBooleanExpression(val operator: BooleanOperator, val list: List<BooleanExpression>) : BooleanExpression
