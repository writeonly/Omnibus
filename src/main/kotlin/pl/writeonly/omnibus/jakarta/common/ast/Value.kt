package pl.writeonly.omnibus.jakarta.common.ast

sealed interface Value

data class Literal(val value: UInt) : Value
data class FunctionCall(val name: String) : Value
