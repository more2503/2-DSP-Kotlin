package new

import Graph
import Vertex
import org.example.legacy.permutationsRecursive
import java.util.*

fun BFS(graph: Graph, start: Vertex) {
    val visited: MutableSet<Vertex> = mutableSetOf(start)
    val queue: LinkedList<Vertex> = LinkedList()

    assert(start.dist[start] != null)

    queue.add(start)

    while (queue.isNotEmpty()) {
        val v = queue.poll()

        v.adjacentVerticse.forEach { neighbor ->
            if (!visited.contains(neighbor)) {
                visited.add(neighbor)
                queue.add(neighbor)
                neighbor.dist[start] = v.dist[start]!! + 1
            }
        }

    }
}

fun getAllPermutation(labels: List<Int>): List<Permutation> {
    val solutions = mutableListOf<List<Int>>()
    permutationsRecursive(labels, 0, solutions)
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

fun getAllSubsets(input: List<Int>): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val currentSubset = mutableListOf<Int>()

    // Backtracking function to generate subsets
    fun backtrack(start: Int) {
        // Add the current subset to the result
        result.add(ArrayList(currentSubset))

        // Explore further subsets
        for (i in start until input.size) {
            // Include nums[i] in the current subset
            currentSubset.add(input[i])
            // Recurse with the next element
            backtrack(i + 1)
            // Backtrack: remove nums[i] from the current subset
            currentSubset.removeAt(currentSubset.size - 1)
        }
    }

    // Start backtracking from the first element
    backtrack(0)

    return result
}

fun checkIfCrossingsetIsValid(crossingset: CrossingSet): Boolean {

    var marbles = crossingset.marbles

    if (marbles.delta != null) {
        if (marbles.rho != null || marbles.alpha != null || marbles.omega != null || marbles._omega != null)
            return false
    }
    
}