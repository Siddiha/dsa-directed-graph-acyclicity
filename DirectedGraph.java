// Student ID: 20240856
// Name: Siddiha Rimzan
// Module: 5SENG003W Algorithms Coursework 2025/26

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DirectedGraph represents a directed graph using an adjacency list.
 *
 * Design Decision:
 * ----------------
 * We use an adjacency list (List of HashSets) combined with:
 *   - outDegree[]  : tracks how many active neighbours each vertex has
 *   - inEdges      : reverse map so removeVertex() can update outDegree efficiently
 *   - active[]     : boolean array to mark removed vertices
 *
 * Why not adjacency matrix?
 *   - Matrix findSink() = O(n^2), our approach = O(V)
 *   - Matrix uses O(n^2) memory, list uses O(V+E)
 *
 * Key operation complexities:
 *   - addEdge()      -> O(1)
 *   - findSink()     -> O(V)
 *   - removeVertex() -> O(in-degree of v)
 *   - Overall sink elimination -> O(V + E)
 */
public class DirectedGraph {

    private final int numVertices;

    // outEdges.get(v) = all vertices v points TO
    private final List<Set<Integer>> outEdges;

    // inEdges.get(v) = all vertices that point TO v
    private final List<Set<Integer>> inEdges;

    // outDegree[v] = number of active outgoing edges from v
    private final int[] outDegree;

    // active[v] = true if vertex v has not been removed
    private final boolean[] active;

    // count of remaining active vertices
    private int activeCount;

    /**
     * Construct an empty directed graph with n vertices (labelled 0 to n-1).
     * @param n number of vertices
     */
    public DirectedGraph(int n) {
        this.numVertices = n;
        this.outEdges    = new ArrayList<>(n);
        this.inEdges     = new ArrayList<>(n);
        this.outDegree   = new int[n];
        this.active      = new boolean[n];
        this.activeCount = n;

        for (int i = 0; i < n; i++) {
            outEdges.add(new HashSet<>());
            inEdges.add(new HashSet<>());
            active[i]    = true;
            outDegree[i] = 0;
        }
    }

    /**
     * Add a directed edge from vertex 'from' to vertex 'to'.
     * Duplicate edges are ignored.
     * @param from source vertex
     * @param to   destination vertex
     */
    public void addEdge(int from, int to) {
        if (from < 0 || from >= numVertices || to < 0 || to >= numVertices) {
            throw new IllegalArgumentException("Vertex out of range: " + from + " -> " + to);
        }
        if (!outEdges.get(from).contains(to)) {
            outEdges.get(from).add(to);
            inEdges.get(to).add(from);
            outDegree[from]++;
        }
    }
    /**
     * Find a sink: an active vertex with no outgoing edges to active vertices.
     * Uses outDegree array for O(V) performance.
     * @return index of a sink, or -1 if no sink exists
     */
    public int findSink() {
        for (int v = 0; v < numVertices; v++) {
            if (active[v] && outDegree[v] == 0) {
                return v;
            }
        }
        return -1;
    }
    /**
     * Remove a vertex from the graph.
     * Updates outDegree of all vertices pointing into v.
     * Time complexity: O(in-degree of v)
     * @param v vertex to remove
     */
    public void removeVertex(int v) {
        if (!active[v]) return;

        // For every u -> v edge, decrement outDegree[u]
        for (int u : inEdges.get(v)) {
            if (active[u]) {
                outDegree[u]--;
            }
        }

        active[v] = false;
        activeCount--;
    }

    /**
     * Check if the graph is empty (all vertices removed).
     * @return true if no active vertices remain
     */
    public boolean isEmpty() {
        return activeCount == 0;
    }

    /**
     * Get the set of all currently active vertices.
     * @return set of active vertex indices
     */
    public Set<Integer> getActiveVertices() {
        Set<Integer> result = new HashSet<>();
        for (int v = 0; v < numVertices; v++) {
            if (active[v]) result.add(v);
        }
        return result;
    }

    /**
     * Get the outgoing neighbours of vertex v (used for DFS cycle detection).
     * @param v vertex
     * @return set of vertices v points to
     */
    public Set<Integer> getOutNeighbours(int v) {
        return outEdges.get(v);
    }

    /**
     * Check if a vertex is still active.
     * @param v vertex
     * @return true if active
     */
    public boolean isActive(int v) {
        return active[v];
    }

    /**
     * Get total number of vertices (including removed).
     * @return number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Get current active out-degree of vertex v.
     * @param v vertex
     * @return active out-degree
     */
    public int getOutDegree(int v) {
        return outDegree[v];
    }

    /**
     * String representation of the active graph.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DirectedGraph (").append(numVertices).append(" vertices, ")
          .append(activeCount).append(" active):\n");
        for (int v = 0; v < numVertices; v++) {
            if (active[v]) {
                sb.append("  ").append(v).append(" -> ").append(outEdges.get(v)).append("\n");
            }
        }
        return sb.toString();
    }
}