package new

import Edge
import Graph
import ShortestPaths
import Vertex

fun main() {

    val v: MutableList<Vertex> = mutableListOf()

    for (i in 0..13) {
        when (i) {
            0 -> v.add(Vertex("t1"))
            6 -> v.add(Vertex("t2"))
            7 -> v.add(Vertex("s1"))
            11 -> v.add(Vertex("s2"))
            else -> v.add(Vertex())
        }
    }

    // v[0] = t1, v[6] = t2, v[7] = s1, v[11] = s2

    val edges = listOf(
        Edge(v[0], v[1]),
        Edge(v[1], v[2]),
        Edge(v[2], v[3]),
        Edge(v[3], v[4]),
        Edge(v[4], v[5]),
        Edge(v[5], v[6]),

        Edge(v[7], v[8]),
        Edge(v[8], v[9]),
        Edge(v[9], v[10]),
        Edge(v[10], v[11]),

        Edge(v[8], v[10]),
        Edge(v[2], v[9]),
        Edge(v[4], v[9]),
        Edge(v[3], v[8]),
        Edge(v[3], v[10]),

        Edge(v[1], v[12]),
        Edge(v[12], v[13]),
        Edge(v[11], v[13]),
    )

    val G = Graph(v, edges, arrayListOf(Pair(v[7], v[0]), Pair(v[11], v[6])))

    G.vertexPairs.forEach { pair ->
        BFS(G, pair.first)
    }

    val shortestPaths = ShortestPaths(G)
    val crossingset = G.guessCrossingSetsGivenPermutation(G.permutation[listOf(0,1)]!![0])

    println("Done")
}