package pl.writeonly.omnibus.rule.rules

data class CandidateBid(
    val bid: String,
    val priority: Int,
    val reason: String,
)

