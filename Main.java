// Student ID: 20240856
// Name: Siddiha Rimzan
// Module: 5SENG003W Algorithms Coursework 2025/26

import java.io.IOException;
import java.util.List;

/**
 * Main entry point for the Acyclicity Checker project.
 *
 * Usage: Compile : javac DirectedGraph.java GraphParser.java
 * AcyclicityChecker.java Main.java Run demo: java Main Run file: java Main
 * <input_file>
 *
 * Examples: java Main a_40_0.txt java Main c_40_0.txt
 */
public class Main {

    public static void main(String[] args) {

        if (args.length == 1) {
            // Run on the file provided as argument
            runOnFile(args[0]);
        } else {
            // No argument: run built-in demo on two small examples
            runDemos();
        }
    }

    // -------------------------------------------------------------------
    // Demo mode: two small hand-crafted examples
    // -------------------------------------------------------------------
    private static void runDemos() {
        System.out.println("No file argument given. Running built-in demos.\n");
        System.out.println("To run on a benchmark file: java Main <filename>\n");

        // ----- Demo 1: Acyclic -----
        // Graph: 0->1, 1->2, 0->2
        // Expected: YES (acyclic), sinks removed in order: 2, 1, 0
        System.out.println("============================================================");
        System.out.println("DEMO 1: Acyclic graph  |  Edges: 0->1, 1->2, 0->2");
        System.out.println("============================================================");

        DirectedGraph g1 = new DirectedGraph(3);
        g1.addEdge(0, 1);
        g1.addEdge(1, 2);
        g1.addEdge(0, 2);

        boolean acyclic1 = AcyclicityChecker.isAcyclic(g1);
        System.out.println("Final answer: " + (acyclic1 ? "YES (acyclic)" : "NO (not acyclic)"));
        System.out.println();

        // ----- Demo 2: Cyclic -----
        // Graph: 0->1, 1->2, 2->0
        // Expected: NO (cycle: 0->1->2->0)
        System.out.println("============================================================");
        System.out.println("DEMO 2: Cyclic graph   |  Edges: 0->1, 1->2, 2->0");
        System.out.println("============================================================");

        DirectedGraph g2 = new DirectedGraph(3);
        DirectedGraph g2copy = new DirectedGraph(3); // kept intact for findCycle()
        g2.addEdge(0, 1);
        g2copy.addEdge(0, 1);
        g2.addEdge(1, 2);
        g2copy.addEdge(1, 2);
        g2.addEdge(2, 0);
        g2copy.addEdge(2, 0);

        boolean acyclic2 = AcyclicityChecker.isAcyclic(g2);
        System.out.println("Final answer: " + (acyclic2 ? "YES (acyclic)" : "NO (not acyclic)"));

        if (!acyclic2) {
            // Task 5: find and print the actual cycle
            List<Integer> cycle = AcyclicityChecker.findCycle(g2copy);
            AcyclicityChecker.printCycle(cycle);
        }
    }

    // -------------------------------------------------------------------
    // File mode: parse and check a benchmark input file
    // -------------------------------------------------------------------
    private static void runOnFile(String filePath) {
        System.out.println("============================================================");
        System.out.println("Input file : " + filePath);
        System.out.println("============================================================");

        DirectedGraph graph;
        DirectedGraph graphCopy; // isAcyclic() modifies the graph, so keep a copy for findCycle()

        try {
            graph = GraphParser.parse(filePath);
            graphCopy = GraphParser.parse(filePath);
        } catch (IOException e) {
            System.err.println("ERROR: Could not read file: " + e.getMessage());
            return;
        }

        System.out.println("Graph loaded: " + graph.getNumVertices() + " vertices\n");

        // Task 4: run sink elimination
        boolean acyclic = AcyclicityChecker.isAcyclic(graph);
        System.out.println("\nFinal answer: "
                + (acyclic ? "YES - graph is acyclic" : "NO - graph is NOT acyclic"));

        // Task 5: if not acyclic, find and print the cycle
        if (!acyclic) {
            System.out.println();
            List<Integer> cycle = AcyclicityChecker.findCycle(graphCopy);
            AcyclicityChecker.printCycle(cycle);
        }
    }
}
