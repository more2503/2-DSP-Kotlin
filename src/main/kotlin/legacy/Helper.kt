package org.example.legacy

import java.util.*

class Permutation(val permutation: List<Int>) {
    override fun toString(): String {
        var out = ""
        permutation.forEach { it ->
            if (it == permutation.first())
                out += it
            else
                out += ", $it"
        }

        return "($out)"
    }

    fun get(): List<Int> = permutation
}


fun createPathFromVertices(vertices: List<Vertex>, path: List<String>): List<Edge> {
    val edges: MutableList<Edge> = mutableListOf()
    for (i in path.indices) {
        if (i < path.size - 1) {
            edges.add(Edge(vertices.find { v -> v.label == path[i] }!!, vertices.find { v -> v.label == path[i+1] }!!))
        }
    }

    return edges
}

fun getVertex(vertices: List<Vertex>, label: String): Vertex {
    try {
        return vertices.find { v -> v.label == label }!!
    } catch (e: Exception) {
        throw Exception("Vertex $label not found ${e.message}")
    }
}

fun BFS(graph: Graph, start: Vertex) {
    val visited: MutableSet<Vertex> = mutableSetOf(start)
    val queue: LinkedList<Vertex> = LinkedList()

    queue.add(start)

    while (queue.isNotEmpty()) {
        val v = queue.poll()

        graph.getAdjacentVertices(v).forEach { neighbor ->
            if (!visited.contains(neighbor)) {
                visited.add(neighbor)
                queue.add(neighbor)
                neighbor.dist[start.memberOfPath - 1] = v.dist[start.memberOfPath - 1] + 1
            }
        }

    }
}

fun constructPath(vertices: List<Vertex>, pathId: Int): Path {
    val pathVertices: MutableList<Vertex> = mutableListOf()

    val path: Path = Path(pathId)

    for (v in vertices) {
        if (v.memberOfPath == pathId) {
            pathVertices.add(v)
        }
    }

    pathVertices.sortBy { v -> v.dist[pathId - 1] }

    for (v in pathVertices) {
        path.addMiddleVertex(v)
    }

    return path
}

fun permutations(input: List<Int>): List<List<Int>> {
    val solutions = mutableListOf<List<Int>>()
    permutationsRecursive(input, 0, solutions)
    return solutions
}


fun permutationsRecursive(input: List<Int>, index: Int, output: MutableList<List<Int>>) {
    if (index == input.lastIndex) output.add(input.toList())
    for (i in index .. input.lastIndex) {
        Collections.swap(input, index, i)
        permutationsRecursive(input, index + 1, output)
        Collections.swap(input, i, index)
    }
}

fun Any?.notNull(f: ()-> Unit){
    if (this != null){
        f()
    }
}

fun calculateMinimalSegments(crossingSet: CrossingSet, paths: List<Path>): List<List<Vertex>> {

    val marblesOfPath_i: MutableList<List<Vertex>> = mutableListOf()
    val result: MutableList<List<Vertex>> = mutableListOf()

    marblesOfPath_i.add(crossingSet.marbles.filter { v -> v.memberOfPath == 1 })
    marblesOfPath_i.add(crossingSet.marbles.filter { v -> v.memberOfPath == 2 })
    marblesOfPath_i.add(crossingSet.marbles.filter { v -> v.memberOfPath == 3 })

    marblesOfPath_i.forEachIndexed { idx, path ->
        val tmp = path.sortedBy { it.dist[it.memberOfPath - 1] }
        marblesOfPath_i[idx] = tmp
    }

    for (path in marblesOfPath_i) {
        for (i in 0..path.size - 2)
            result.add(getPathInducedByEndpoints(paths[path.first().memberOfPath - 1], path[i], path[i+1]))
    }

    return result
}