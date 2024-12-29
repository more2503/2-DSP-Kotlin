package org.example.legacy

class Vertex(var label: String = "", val memberOfPath: Int = -1) {
    var dist: IntArray = IntArray(3)

    override fun toString(): String {
        return "${label}: (${dist[0]}, ${dist[1]}, ${dist[2]})"
    }
}

class Edge(val u: Vertex, val v: Vertex) {
    override fun toString(): String {
        return "${u.label}-${v.label}"
    }
}

class Graph(val vertices: List<Vertex>, val edges: List<Edge>) {
    fun getVertex(label: String): Vertex? {
        return vertices.find { v: Vertex -> v.label == label }
    }

    fun getAdjacentVertices(vertex: Vertex): Set<Vertex> {
        val neighbors: MutableSet<Vertex> = mutableSetOf()

        for (e in edges) {
            if (vertex == e.u)
                neighbors.add(e.v)
            if (vertex == e.v)
                neighbors.add(e.u)
        }

        return neighbors
    }
}

class Path(val id: Int = -1, val vertices: MutableList<Vertex> = mutableListOf()) {

    fun addMiddleVertex(vertex: Vertex) {
        vertices.add(vertex)
    }

    fun getLabelOfPath(): Int {
        return vertices[0].memberOfPath
    }

    fun getIthVertex(i: Int): Vertex? {
        assert(i - 1 < vertices.size)
        for (v in vertices) {
            if (v.dist[this.getLabelOfPath() - 1] == i)
                return v
        }
        return null
    }

    override fun toString(): String {
        var out: String = ""
        out += "${vertices.first().label}-${vertices.last().label} path"
        return out
    }
}