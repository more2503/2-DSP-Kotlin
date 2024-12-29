import new.*

class Vertex(var label: String = "") {
    var dist: MutableMap<Vertex, Int> = mutableMapOf()
    var adjacentVerticse: List<Vertex> = listOf()

    fun distanceToString(): String {
        val out = StringBuilder()
        out.append("(")

        dist.forEach { v, d ->
            out.append(d)
            out.append(", ")
        }

        out.setLength(out.length - 2) // remove trailing ", "
        out.append(")")

        return out.toString()
    }

    override fun toString(): String {
        if (label != "") {
            if (dist.isNotEmpty())
                return "${label}: ${distanceToString()}"
            else
                return label
        } else {
            if (dist.isNotEmpty())
                return distanceToString()
            else
                return ""
        }
    }
}


data class Edge(val u: Vertex, val v: Vertex) {
    override fun toString(): String {
        return "${u.label}-${v.label}"
    }
}


class Graph(val vertices: List<Vertex>, val edges: List<Edge>, val vertexPairs: ArrayList<Pair<Vertex, Vertex>>) {

    lateinit var shortestPaths: MutableMap<Vertex, Set<Path>>
    val labels: List<Int> = vertexPairs.indices.toList()
    val subsets: List<List<Int>> = getAllSubsets(labels)

    val permutation: MutableMap<List<Int>, List<Permutation>> = mutableMapOf()


    init {
        vertices.forEach { v ->
            val adjacentVertices: MutableList<Vertex> = mutableListOf()
            edges.forEach { e ->
                if (v == e.u) {
                    adjacentVertices.add(e.v)
                } else if (v == e.v) {
                    adjacentVertices.add(e.u)
                }
            }
            v.adjacentVerticse = adjacentVertices

            vertexPairs.forEach { pair ->
                if (v == pair.first)
                    v.dist[pair.first] = 0
                else
                    v.dist[pair.first] = -1
            }
        }

        this.vertexPairs.forEach { pair ->
            BFS(this, pair.first)
        }

        shortestPaths = ShortestPaths(this).shortestPaths

        subsets.forEach { subset ->
            permutation[subset] = getAllPermutation(subset)
        }
    }

    fun guessCrossingSets(): List<CrossingSet> {

    }
}
