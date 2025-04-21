package pl.writeonly.omnibus.jakarta.common.ast

import pl.writeonly.omnibus.jakarta.common.system.Suit

sealed interface Value

data class Number(val value: UInt) : Value
data class Len(val suit: Suit) : Value
data class Strength(val name: String = "") : Value
