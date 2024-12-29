import new.BFS
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class GraphTest {

    @Test
    fun testToString() {
        val v = Vertex()

        // assertEquals(v.toString() )
    }

    @Test
    fun testGraph() {
        val u = Vertex("u")
        val v = Vertex("v")
        val w1 = Vertex("w1")
        val w2 = Vertex("w2")
        val w3 = Vertex("w3")
        val w4 = Vertex("w4")
        val y1 = Vertex("y1")
        val y2 = Vertex("y2")
        val y3 = Vertex("y3")

        val vertices = listOf(u, v, w1, w2, w3, w4, y1, y2, y3)


        val edges = listOf(
            Edge(u, v),
            Edge(u, w1),
            Edge(v, w1),
            Edge(w1, w2),
            Edge(w2, w3),
            Edge(w3, w4),
            Edge(u, y1),
            Edge(y1, y2),
            Edge(y2, y3),
            Edge(y3, w4),

            Edge(w2, y3),
            Edge(y2, w3)
        )

        val G = Graph(vertices, edges, arrayListOf(Pair(u, w4), Pair(y1, y3), Pair(w1, w3)))

        G.vertexPairs.forEach { pair ->
            BFS(G, pair.first)
        }

        val shortestPaths = ShortestPaths(G)


        assertContentEquals(edges, G.edges)
        assertContentEquals(vertices, G.vertices)
        assertContentEquals(listOf(v, w1, y1), u.adjacentVerticse)
        assertContentEquals(listOf(v, w1, y1), u.adjacentVerticse)
        assertEquals(w1.dist[u], 1)
        assertEquals(w4.dist[u], 4)
        assertEquals(y3.dist[u], 3)
        G.vertices.forEach {
            assertNotNull(v.dist[u])
        }
    }
}