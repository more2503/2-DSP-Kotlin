package org.example.legacy

fun main() {
    println("Hello World!")

    val vertices: MutableList<Vertex> = mutableListOf()
    val edges: MutableList<Edge> = mutableListOf()

    vertices.addAll(
        listOf(
            Vertex("s_1", 1),
            Vertex("s_2", 2),
            Vertex("s_3", 3),
            Vertex("t_1", 1),
            Vertex("t_2", 2),
            Vertex("t_3", 3),
        )
    )

    for (path in 1..3) {
        for (i in 1..5) {
            vertices.add(Vertex("v_${path}_${i}", path))
        }
    }

// adding edges of paths
    for (path in 1..3) {
        edges.add(Edge(vertices.find { v -> v.label == "s_${path}" }!!, vertices.find { v -> v.label == "v_${path}_1"}!! ))
        for (i in 1..5) {
            if (i < 5) {
                edges.add(Edge( vertices.find { v -> v.label == "v_${path}_$i" }!!, vertices.find { v -> v.label == "v_${path}_${i+1}" }!! ))
            } else {
                edges.add(Edge( vertices.find { v -> v.label == "v_${path}_$i" }!!, vertices.find { v -> v.label == "t_${path}" }!! ))
            }
        }
    }

// add bridges between Paths
    vertices.addAll(
        listOf(
            Vertex("b_21_1"),  // b_${paths_connecting}_{counter}
            Vertex("b_21_2"),
            Vertex("b_32_1"),
            Vertex("b_32_2"),
        )
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_2_2", "b_21_1", "b_21_2", "t_1"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_3_2", "b_32_1", "b_32_2", "t_2")),
    )
//============================================

// add internal briges
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_1_2", "v_2_3", "v_1_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_1_2", "v_3_3", "v_1_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_2_2", "v_1_3", "v_2_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_2_2", "v_3_3", "v_2_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_3_2", "v_1_3", "v_3_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_3_2", "v_2_3", "v_3_4"))
    )
    edges.addAll(
        createPathFromVertices(vertices, listOf("v_1_2", "v_3_5"))
    )
//============================================






    val graph: Graph = Graph(vertices, edges)

    BFS(graph, getVertex(vertices, "s_1"))
    BFS(graph, getVertex(vertices, "s_2"))
    BFS(graph, getVertex(vertices, "s_3"))

// add guessed paths
    val guessedPaths: Array<Path> = arrayOf(
        constructPath(vertices, 1),
        constructPath(vertices, 2),
        constructPath(vertices, 3)
    )
//============================================


// Calculate Crossing Set

    var crossingsSet: CrossingSet = CrossingSet(listOf(1, 2, 3))
    var permutations: MutableList<List<Int>> = crossingsSet.permutations.toMutableList()
    // var permutations: List<List<Int>> = listOf(listOf(2,3,1))

    permutations.addAll(permutations(listOf(1,2)))
    permutations.addAll(permutations(listOf(2,3)))
    permutations.addAll(permutations(listOf(1,3)))
    permutations.add(listOf(1))
    permutations.add(listOf(2))
    permutations.add(listOf(3))


    crossingsSet.permutations = permutations

    val result: MutableList<Intersection_PartialcrossingSet> = mutableListOf()

    for (perm in permutations) {
        println(perm)
        val intersection_PartialcrossingSet = getIntersections(vertices, perm, guessedPaths.toList())
        result.add(intersection_PartialcrossingSet)
        crossingsSet.marbles.addAll(intersection_PartialcrossingSet.partialCrossingSet.marbles)
        if (intersection_PartialcrossingSet.intersection.alpha != null && intersection_PartialcrossingSet.intersection.omega != null)
            crossingsSet.ends.add(Pair(intersection_PartialcrossingSet.intersection.alpha, intersection_PartialcrossingSet.intersection.omega))
        else
            crossingsSet.ends.add(Pair(null, null))
    }

    val T: List<MutableList<Vertex>> = listOf(mutableListOf(), mutableListOf(), mutableListOf())

    for (path in guessedPaths) {
        for (v in path.vertices) {
            if (crossingsSet.marbles.contains(v))
                T[path.id - 1].add(v)
        }
    }

// ll 3
    var P: MutableList<MutableList<Pair<Vertex, Vertex>>> = mutableListOf(
        mutableListOf(),
        mutableListOf(),
        mutableListOf()
    )

// ll 5
    var minimalSegments: List<List<Vertex>> = calculateMinimalSegments(crossingsSet, guessedPaths.toList())

    for (S in minimalSegments) {
        var marks: MutableSet<Int> = mutableSetOf()
        val label: Int = S.first().memberOfPath - 1

        for (i in 0..<crossingsSet.permutations.size) {
            val permutation = crossingsSet.permutations[i]
            val ends = crossingsSet.ends[i]

            if (ends.first != null && ends.second != null) {
                if (ends.first!!.dist[label] <= S.first().dist[label] && S.first().dist[label] < S.last().dist[label] && S.last().dist[label] <= ends.second!!.dist[label]) {
                    permutation.forEach { marks.add(it) }
                }
            }
        }

        val j = marks.min()
        val x = S.minBy { it.dist[j - 1] }
        val y = S.maxBy { it.dist[j - 1] }

        P[j - 1].add(Pair(x, y))
    }


//============================================



    println("Done.")
}
