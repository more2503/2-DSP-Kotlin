package new

import Graph
import Vertex

typealias Permutation = List<Int>
typealias Path = ArrayList<Vertex>


data class Marbles(
    var s: Vertex,
    var rho: Vertex? = null,
    var alpha: Vertex? = null,
    var omega: Vertex? = null,
    var _omega: Vertex? = null,
    var delta: Vertex? = null,
    var t: Vertex
)

class CrossingSet(val G: Graph, permutation: Permutation, marbles: Marbles) {

}