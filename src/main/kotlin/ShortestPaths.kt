import new.Path


class ShortestPaths(val G: Graph) {

    var shortestPaths: MutableMap<Vertex, Set<Path>> = mutableMapOf()

    init {
        G.vertexPairs.forEach { pair ->
            var bufferForShortestPaths: MutableSet<Path> = mutableSetOf()

            val start = pair.first
            val end = pair.second

            shortestPaths[start] = expandPath(arrayListOf(end), end, start).toSet()
        }
    }

    private fun expandPath(path: Path, v: Vertex, start: Vertex): ArrayList<Path> {
        if (v == start) {
            return arrayListOf(path)
        }
        val paths: ArrayList<Path> = arrayListOf()

        v.adjacentVerticse.forEach { u ->
            val pathCopy: Path = path.clone() as Path
            if (u.dist[start] == v.dist[start]!! - 1) {
                pathCopy.add(0, u)
                paths.addAll(expandPath(pathCopy, u, start))
            }
        }
        return paths
    }
}