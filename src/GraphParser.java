// Student ID: 20240856
// Name: Siddiha 
// Module: 5SENG003W Algorithms Coursework 2025/26

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GraphParser reads a directed graph from a plain text input file.
 *
 * Expected file format (as per coursework specification):
 *   - Each line contains exactly two integers "u v" meaning a directed edge u -> v
 *   - No header line required
 *   - Vertex labels can be 0-based or 1-based (detected automatically)
 *
 * Example file:
 *   1 2
 *   3 1
 *   2 5
 *
 * Handles:
 *   - Windows (\r\n) and Unix (\n) line endings
 *   - Blank lines (skipped)
 *   - Extra whitespace between numbers
 *   - Both 0-based and 1-based vertex numbering
 */
public class GraphParser {

    /**
     * Parse a graph from the given file path.
     * Uses a two-pass approach: first scan to find the largest vertex label,
     * then build the graph of the correct size.
     *
     * @param filePath path to the input file
     * @return a DirectedGraph built from the file
     * @throws IOException if the file cannot be read or is malformed
     */
    public static DirectedGraph parse(String filePath) throws IOException {
        List<int[]> edges = new ArrayList<>();
        int maxVertex = 0;
        boolean hasVertex0 = false;
        String line;

        // Pass 1: read all edge pairs and find the largest vertex label
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;          // skip blank lines

                String[] parts = line.split("\\s+");
                if (parts.length < 2) continue;        // skip malformed lines

                int from = Integer.parseInt(parts[0]);
                int to   = Integer.parseInt(parts[1]);
                edges.add(new int[]{from, to});

                if (from == 0 || to == 0) hasVertex0 = true;
                maxVertex = Math.max(maxVertex, Math.max(from, to));
            }
        }

        // Pass 2: build graph sized (maxVertex + 1) so every label fits as an index
        DirectedGraph graph = new DirectedGraph(maxVertex + 1);
        for (int[] edge : edges) {
            graph.addEdge(edge[0], edge[1]);
        }

        // If no edge references vertex 0 the file uses 1-based labels.
        // Remove the unused slot so it does not appear as a spurious sink.
        if (!hasVertex0) {
            graph.removeVertex(0);
        }

        return graph;
    }
    
}