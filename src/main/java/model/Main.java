package model;

public class Main {

    public static void main(String[] args) throws Exception {

        // Générer un graph aléatoire
        Graph.generateGraph(7, 0.4, "nameGraphToSave.txt");

        // Le récupérer et le print
        Graph graph = Graph.loadGraph("nameGraphToSave.txt");
        System.out.println(graph);
    }
}
