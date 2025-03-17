package pl.writeonly.omnibus.partialfunction


import io.vavr.PartialFunction

interface Rule<A, B>: PartialFunction<A, B> {

    companion object {
        fun <A, B> apply(a: A, fs: List<PartialFunction<A, B>>): B {
            return fs.first { it.isDefinedAt(a) }.apply(a)
        }
    }
}
