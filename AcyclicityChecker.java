// Student ID: 20240856
// Name: Siddiha Rimzan
// Module: 5SENG003W Algorithms Coursework 2025/26

import java.util.ArrayList;
import java.util.List;

/**
 * AcyclicityChecker implements:
 *
 *   Task 4 - isAcyclic():
 *     Sink Elimination Algorithm to check if a directed graph is acyclic.
 *     Steps:
 *       1. If the graph is empty -> return true (acyclic)
 *       2. If no sink exists     -> return false (cycle detected)
 *       3. Remove a sink and repeat
 *     Prints every step so the result can be verified independently.
 *
 *   Task 5 - findCycle():
 *     If the graph is not acyclic, find and return the actual cycle.
 *     Uses standard 3-colour DFS:
 *       WHITE (0) = not yet visited
 *       GREY  (1) = currently on the DFS stack (being explored)
 *       BLACK (2) = fully explored
 *     A back edge to a GREY vertex means a cycle exists.
 */
public class AcyclicityChecker {

    // -------------------------------------------------------------------
    // Task 4: Sink Elimination Algorithm
    // -------------------------------------------------------------------

    /**
     * Check whether the given graph is acyclic using sink elimination.
     * Prints each step showing which sink was found and removed.
     *
     * NOTE: This method modifies the graph (removes vertices).
     *       Pass a fresh copy if you need the graph intact afterwards.
     *
     * @param graph the DirectedGraph to check
     * @return true if acyclic, false if a cycle exists
     */
    public static boolean isAcyclic(DirectedGraph graph) {
        System.out.println("--- Running Sink Elimination Algorithm ---");

        int step = 1;

        while (!graph.isEmpty()) {
            int sink = graph.findSink();

            if (sink == -1) {
                // No sink found in the remaining subgraph -> cycle exists
                System.out.println("Step " + step + ": No sink found among remaining vertices: "
                        + graph.getActiveVertices());
                System.out.println("=> Result: NO - graph is NOT acyclic (cycle detected).");
                return false;
            }

            // Found a sink -> print and remove it
            System.out.println("Step " + step + ": Sink found -> vertex " + sink
                    + " (out-degree = 0). Removing it. "
                    + "Vertices remaining after removal: "
                    + (graph.getActiveVertices().size() - 1));
            graph.removeVertex(sink);
            step++;
        }

        System.out.println("=> Result: YES - graph IS acyclic "
                + "(all " + (step - 1) + " vertices successfully eliminated as sinks).");
        return true;
    }


    // -------------------------------------------------------------------
    // Task 5: Cycle Detection using DFS
    // -------------------------------------------------------------------

    private static final int WHITE = 0; // not visited
    private static final int GREY  = 1; // on current DFS path
    private static final int BLACK = 2; // fully processed

    /**
     * Find and return a cycle in the graph using DFS.
     * Returns null if the graph is acyclic.
     *
     * @param graph the DirectedGraph to search (not modified)
     * @return a List of vertex indices forming the cycle (first == last),
     *         or null if no cycle exists
     */
    public static List<Integer> findCycle(DirectedGraph graph) {
        int n = graph.getNumVertices();
        int[] colour = new int[n];
        int[] parent = new int[n];

        for (int i = 0; i < n; i++) {
            colour[i] = WHITE;
            parent[i] = -1;
        }

        // Launch DFS from every unvisited vertex
        for (int v = 0; v < n; v++) {
            if (colour[v] == WHITE) {
                List<Integer> cycle = dfs(v, colour, parent, graph);
                if (cycle != null) return cycle;
            }
        }

        return null; // no cycle found
    }

    /**
     * Recursive DFS from vertex v.
     * Returns a cycle list if a back edge is found, otherwise null.
     */
    private static List<Integer> dfs(int v, int[] colour, int[] parent, DirectedGraph graph) {
        colour[v] = GREY; // v is now on the current exploration path

        for (int neighbour : graph.getOutNeighbours(v)) {
            if (colour[neighbour] == GREY) {
                // Back edge found: neighbour is already on our path -> cycle!
                return reconstructCycle(neighbour, v, parent);
            }

            if (colour[neighbour] == WHITE) {
                parent[neighbour] = v;
                List<Integer> cycle = dfs(neighbour, colour, parent, graph);
                if (cycle != null) return cycle;
            }
            // colour[neighbour] == BLACK: already fully explored, skip
        }

        colour[v] = BLACK; // fully explored
        return null;
    }

    /**
     * Reconstruct the cycle path from the parent array.
     *
     * @param cycleStart the vertex where the back edge points (GREY vertex)
     * @param cycleEnd   the vertex from which the back edge was taken
     * @param parent     DFS parent array
     * @return list of vertices in the cycle, with cycleStart repeated at the end
     */
    private static List<Integer> reconstructCycle(int cycleStart, int cycleEnd, int[] parent) {
        List<Integer> cycle = new ArrayList<>();

        // Walk backwards from cycleEnd to cycleStart via parent pointers
        int current = cycleEnd;
        while (current != cycleStart) {
            cycle.add(current);
            current = parent[current];
        }
        cycle.add(cycleStart);

        // Reverse to get forward (natural) order
        java.util.Collections.reverse(cycle);

        // Repeat cycleStart at the end to clearly show it's a cycle
        cycle.add(cycleStart);

        return cycle;
    }

    /**
     * Print the cycle to standard output in a readable format.
     * Example output: Cycle found: 3 -> 7 -> 2 -> 3
     *
     * @param cycle list of vertices forming the cycle, or null
     */
    public static void printCycle(List<Integer> cycle) {
        if (cycle == null) {
            System.out.println("No cycle found.");
            return;
        }
        System.out.print("Cycle found: ");
        for (int i = 0; i < cycle.size(); i++) {
            System.out.print(cycle.get(i));
            if (i < cycle.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }
}