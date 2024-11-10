package org.example

class CrossingSet(var labels: List<Int>) {

    val permutations: List<List<Int>> = permutations(labels)
    val marbles: MutableSet<Vertex> = mutableSetOf()

    override fun toString(): String {
        var out = ""

        marbles.forEach {
            if (it == marbles.first())
                out += it.label
            else
                out += ", ${it.label}"
        }

        return "{$out}"
    }
}

class Intersection(val alpha: Vertex?, val omega: Vertex?) {
    override fun toString(): String {
        return "$alpha, $omega"
    }
}

class PartialCrossingSet(val marbles: Set<Vertex>, val permutation: List<Int>) {
    override fun toString(): String {
        var perm = ""
        var vert = ""

        permutation.forEach {
            if (it == permutation.first())
                perm += it
            else
                perm += ", $it"
        }
        marbles.forEach {
            if (vert == "")
                vert += it.label
            else
                vert += ", ${it.label}"
        }

        return "($perm), {$vert}"
    }
}

data class Intersection_PartialcrossingSet(val intersection: Intersection, val partialCrossingSet: PartialCrossingSet) {
    override fun toString(): String {
        return partialCrossingSet.toString()
    }
}

fun getIntersections(vertices: List<Vertex>, permutation: List<Int>, paths: List<Path>): Intersection_PartialcrossingSet {

    if (permutation.size == 1) {
        val temp: Pair<Vertex, Vertex> = Pair(getVertex(vertices, "s_${permutation.first()}"), getVertex(vertices, "t_${permutation.first()}"))
        return Intersection_PartialcrossingSet(Intersection(temp.first, temp.second), PartialCrossingSet(setOf(temp.first, temp.second), permutation))
    }
    if (permutation.size == 2) {
        val i = permutation[0] - 1
        val j = permutation[1] - 1

        val endPoints = getAlphaOmegaOfTwoPaths(vertices, paths[i], paths[j])

        val crossingSet: MutableSet<Vertex> = mutableSetOf(
            getVertex(vertices, "s_${j+1}"),
            getVertex(vertices, "t_${j+1}")
        )

        endPoints.second.first?.let { crossingSet.add(it) }
        endPoints.second.second?.let { crossingSet.add(it) }

        return Intersection_PartialcrossingSet(
            Intersection(endPoints.second.first, endPoints.second.second),
            PartialCrossingSet(crossingSet, permutation)
        )
    }
    else {

        val sigma_start: List<Int> = permutation.slice(IntRange(0, permutation.size - 2))
        val sigma_end: List<Int> = permutation.slice(IntRange(1, permutation.size - 1))
        val sigma_last_two: List<Int> = listOf(permutation[permutation.size - 1], permutation[permutation.size - 2])

        val T_sigma_start = getIntersections(vertices, sigma_start, paths).intersection
        val T_sigma_end = getIntersections(vertices, sigma_end, paths).intersection
        val T_sigma_last_two = getIntersections(vertices, sigma_last_two, paths).intersection

        var Q: List<Vertex> = listOf()
        var inducedSubpathBySigmaStart: List<Vertex> = listOf()
        var inducedSubpathBySigmaLastTwo: List<Vertex> = listOf()

        if (T_sigma_start.alpha == null || T_sigma_start.omega == null)
            throw Exception("T_start is empty")
        else {
            inducedSubpathBySigmaStart = getPathInducedByEndpoints(
                paths[permutation[permutation.size - 2] - 1],
                T_sigma_start.alpha!!,
                T_sigma_start.omega!!
            )
            inducedSubpathBySigmaLastTwo = getPathInducedByEndpoints(
                paths[permutation[permutation.size - 2] - 1],
                T_sigma_last_two.alpha!!,
                T_sigma_last_two.omega!!
            )
        }

        Q = getMaxCommonSubPath(vertices, Path(-1, inducedSubpathBySigmaStart.toMutableList()), Path(-1, inducedSubpathBySigmaLastTwo.toMutableList()))

        if (Q.isEmpty())
            throw Exception("Q is empty")

        val P: List<Vertex> = getPathInducedByEndpoints(paths[permutation.last() - 1], T_sigma_end.alpha!!, T_sigma_end.omega!!)

        val endpointsOf_P_Q = getAlphaOmegaOfTwoPaths(vertices, Path(vertices = P.toMutableList()), Path(vertices = Q.toMutableList()))
        val T = endpointsOf_P_Q.first
        val crossingSet: MutableSet<Vertex> = mutableSetOf()


        T.first?.let { crossingSet.add(it) }
        T.second?.let { crossingSet.add(it) }

        endpointsOf_P_Q.second.first?.let { crossingSet.add(it) }
        endpointsOf_P_Q.second.second?.let { crossingSet.add(it) }

        crossingSet.add(getVertex(vertices, "s_${P.first().memberOfPath}"))
        crossingSet.add(getVertex(vertices, "t_${P.first().memberOfPath}"))
        crossingSet.add(getVertex(vertices, "s_${Q.first().memberOfPath}"))
        crossingSet.add(getVertex(vertices, "t_${Q.first().memberOfPath}"))



        return Intersection_PartialcrossingSet(
            Intersection(T.first, T.second),
            PartialCrossingSet(crossingSet, permutation)
        )
    }
}

fun getAlphaOmegaOfTwoPaths(vertices: List<Vertex>, path1: Path, path2: Path): Pair<Pair<Vertex?, Vertex?>, Pair<Vertex?, Vertex?>> {
    var inIntersection: Boolean = false
    var alpha: Vertex? = null
    var omega: Vertex? = null
    
    val l_i = path1.getLabelOfPath() - 1
    val l_j = path2.getLabelOfPath() - 1
    
    var start_i: Vertex = getVertex(vertices, "s_${l_i+1}")
    var start_j: Vertex = getVertex(vertices, "s_${l_j+1}")
    var end_i: Vertex = getVertex(vertices, "s_${l_i+1}")
    var end_j: Vertex = getVertex(vertices, "t_${l_j+1}")
    
    loop@ for (v_i in path1.vertices) {
        for (v_j in path2.vertices) {
            if (v_i.dist[l_i] == v_j.dist[l_i] && v_i.dist[l_j] == v_j.dist[l_j] && !inIntersection) {
                start_i = v_i
                start_j = v_j
                alpha = start_j
                end_j = v_j
                inIntersection = true
                break@loop
            }
        }
    }

    if (inIntersection) {
        var v_i = start_i
        var v_j = start_j

        while (v_i.dist[l_i] == v_j.dist[l_i] && v_i.dist[l_j] == v_j.dist[l_j]) {
//            if (v_i.dist[l_i] + 1 >= path1.vertices.size || v_i.dist[l_i] + 1 >= path1.vertices.size)
//                break // if intersection goes until t_i, t_j, then don't check further beyond t_i, t_j for intersection
            if (path1.getIthVertex(v_i.dist[l_i] + 1) == null || path2.getIthVertex(v_j.dist[l_j] + 1) == null) {
                // end_i = path1.getIthVertex(v_i.dist[l_i] - 1)!!
                // end_j = path2.getIthVertex(v_j.dist[l_j] - 1)!!

                end_i = path1.getIthVertex(v_i.dist[l_i])!!
                end_j = path2.getIthVertex(v_j.dist[l_j])!!

                return Pair(Pair(start_i, end_i), Pair(start_j, end_j))
            } else {
                v_i = path1.getIthVertex(v_i.dist[l_i] + 1)!!
                v_j = path2.getIthVertex(v_j.dist[l_j] + 1)!!
            }

//            v_i = path1.vertices[v_i.dist[l_i] + 1]
//            v_j = path2.vertices[v_j.dist[l_j] + 1]

        }

        end_i = path1.getIthVertex(v_i.dist[l_i] - 1)!!
        end_j = path2.getIthVertex(v_j.dist[l_j] - 1)!!
    }

    return Pair(Pair(start_i, end_i), Pair(start_j, end_j))
}

fun getPathInducedByEndpoints(path: Path, start: Vertex, end: Vertex): List<Vertex> {

    val subPath: MutableList<Vertex> = mutableListOf()

    var inIntersection = false

    for (v in path.vertices) {
        if (v == start)
            inIntersection = true
        if (inIntersection)
            subPath.add(v)
        if (v == end)
            inIntersection = false
    }

    // subPath.add(end)

    return subPath
}

fun getMaxCommonSubPath(vertices: List<Vertex>, path1: Path, path2: Path): List<Vertex> {

    val commonVertices: MutableList<Vertex> = mutableListOf()

    var start: Vertex? = null

    var i = 0
    var j = 0

    loop@ for (l_1 in path1.vertices.indices) {
        for (l_2 in path2.vertices.indices) {
            if (path1.vertices[l_1] == path2.vertices[l_2]) {
                start = path1.vertices[l_1]
                i = l_1
                j = l_2
                break@loop
            }
        }
    }

    while (path1.vertices[i] == path2.vertices[j]) {
        commonVertices.add(path1.vertices[i])

        i = i.inc()
        j = j.inc()

        if (path1.vertices.size <= i || path2.vertices.size <= j)
            break
    }

    return commonVertices
}

