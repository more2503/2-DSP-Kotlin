package new

import Graph
import Vertex

typealias Permutation = List<Int>
typealias Path = ArrayList<Vertex>
typealias Marbles = Map<MarbleType, Vertex>

enum class MarbleType {
    START,
    DELTA,
    ALPHA,
    RHO,
    OMEGA,
    _OMEGA,
    END
}


class CrossingSet(val G: Graph, val permutation: Permutation, val marbles: Marbles) {


    override fun toString(): String {
        return "$permutation: {${marbles.s.label}, ${marbles.delta?.label}, ${marbles.t.label}}"
    }
}