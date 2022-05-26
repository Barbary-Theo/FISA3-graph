package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
    Développer par :
        - Barbary Théo
        - Thibaut Martin

    Le 25 mai 2022
*/

public class Graph {

    /*
        0 -> non-orienté
        1 -> orienté
    */
    private int type;

    /*
        Nombre de sommet
    */
    private int n;

    /*
        Nombre de connection
    */
    private int m;

    /*
        Liste d'adjacence
    */
    private Map<Integer, List<Integer>> tableau;

    public Graph() {}

    public Graph(int type, int n) {

        // Check si bien type non-orienté ou orienté
        if(type == 0 || type == 1) {
            this.type = type;
            this.n = n;

            this.tableau = new HashMap<>();

            // Identifiant des sommets par défaut : 1 à n compris
            for (int i = 1 ; i <= n ; i++) {
                this.tableau.put(i, new ArrayList<>());
            }

            this.m = 0;
        }
        else {
            System.err.println("Error type has to be 0 -> non-orienté or 1 -> orienté");
        }
    }


    public void freeMemorySpace() {
        this.type = 0;
        this.n = 0;
        this.m = 0;
        this.tableau = null;
    }


    public void addConnection(int sommet1, int sommet2) {

        // Interdire les clés négatives
        if(sommet1 >= 0 && sommet2 >= 0) {

            // Si non-orienté
            if(this.type == 0) {

                // Check si la connection n'existe pas déjà
                if(!this.tableau.get(sommet1).contains(sommet2)) {
                    this.tableau.get(sommet1).add(sommet2);
                    this.tableau.get(sommet2).add(sommet1);
                    this.m += 1;
                }
            }
            // Type orienté
            else if(this.type == 1) {

                // Check si la connection n'existe pas déjà
                if(!this.tableau.get(sommet1).contains(sommet2)) {
                    this.tableau.get(sommet1).add(sommet2);
                    this.m += 1;
                }
            }
        }

    }


    public void removeConnection(int sommet1, int sommet2) {

        // Interdire les clés négatives
        if(sommet1 >= 0 && sommet2 >= 0) {

            // Type non-orienté
            if(this.type == 0) {

                // Check si la connection existe
                if(this.tableau.get(sommet1).contains(sommet2)) {
                    this.tableau.get(sommet1).remove((Integer)sommet2);
                    this.tableau.get(sommet2).remove((Integer)sommet1);
                    this.m -= 1;
                }
                else {
                    System.err.println("Not in");
                }
            }
            // Type orienté
            else if(this.type == 1) {

                // Check si la connection existe
                if(this.tableau.get(sommet1).contains(sommet2)) {
                    this.tableau.get(sommet1).remove((Integer) sommet2);
                    this.m -= 1;
                }
                else {
                    System.err.println("Not in");
                }
            }
        }
        else {
            System.err.println("Impossible id sommet out of range");
        }

    }


    public void addSommet(int newID) {

        // Vérif si l'id du sommet n'existe pas déjà
        if(!this.tableau.containsKey(newID)) {
            this.tableau.put(newID, new ArrayList<>());
            this.n += 1;
        }
    }


    public boolean isAdjacent(int sommetSearch, int sommetIn) {

        // Interdire les clés négatives
        if(sommetSearch >= 0 && sommetIn >= 0) {

            // Check que les clés existe
            if(this.tableau.containsKey(sommetSearch) && this.tableau.containsKey(sommetIn)) {
                // Check si le sommet à rechercher est dans le sommet demandé
                return this.tableau.get(sommetIn).contains(sommetSearch);
            }

        }
        else {
            System.err.println("Sommet out of range");
        }

        return false;

    }


    @Override
    public String toString() {
        String type = this.type == 0 ? "Non orienté" : "Orienté";
        return "\n-- Graphe-- \nType : " + type + "\nNombre de sommets : " + this.n + "\nListe d'adjacence\t~~~~~~> " + this.tableau;
    }


    public static Graph loadGraph(String fileName) throws Exception {

        try {
            //Récupérer les lignes du fichier
            List<String> allLines = Files.readAllLines(Paths.get("src/main/resources/" + fileName));

            Graph graph = null;
            int nbConnection = 0;
            int nbSommet = 0;

            for(int i = 0 ; i < allLines.size() ; i ++) {

                // Si première ligne
                if(i == 0) {
                    // Récupérer les infos de base du graphe
                    String[] infos = allLines.get(i).split(" ");
                    nbConnection = Integer.parseInt(infos[2]);
                    nbSommet = Integer.parseInt(infos[1]);
                    graph = new Graph(Integer.parseInt(infos[0]), 0);
                }
                // Si les lignes représentant les connections
                else {
                    // Récupérer les infos de la connection
                    String[] connectionValue = allLines.get(i).split(" ");

                    // Ajouter les deux sommets (check à l'intérieur si le sommet existe déjà)
                    graph.addSommet(Integer.parseInt(connectionValue[0]));
                    graph.addSommet(Integer.parseInt(connectionValue[1]));

                    // Ajouter la connection entre les deux sommets précédemment créés
                    graph.addConnection(Integer.parseInt(connectionValue[0]), Integer.parseInt(connectionValue[1]));

                }

            }

            if(graph != null && graph.n != nbSommet) {
                System.err.println("Il n'y a pas le même nombre de sommet entre le nombre indiqué dans la première ligne et le nombre de ligne suivante");
                return null;
            }

            return graph;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("File not nicely implemented or path is not good");
            return null;
        }

    }

    public void save(String fileName) throws FileNotFoundException {

        // Création du fichier
        PrintWriter pt = new PrintWriter("src/main/resources/" + fileName);

        // Écrire les infos de base
        pt.println(this.type + " " + this.n + " " + this.m);

        // Pour toutes les clés - valeur du graphe
        for(var ele : this.tableau.entrySet()) {

            // Pour toutes les valeurs par sommet (id)
            for (Integer value : ele.getValue()) {
                // Ajouter une ligne sommet source / destination
                pt.println(ele.getKey() + " " + value);
            }
        }

        pt.close();

    }


    public static void generateGraph(int n, double p, String NameGraphToSave) throws FileNotFoundException {

        var rand = new Random();
        // Type aléatoire entre 0 et 1
        int type = rand.nextInt(2);

        Graph graph = new Graph(type, n);

        // Pour tous les sommets
        for(int i = 0 ; i < n ; i ++) {
            // Pour tous les sommets bis
            for(int j = 0 ; j < n ; j ++) {

                // Si pas le même sommet (pas de boucle)
                if(i != j) {
                    double random = Math.random();

                    // Check la probabilité
                    if(random <= p) {
                        // Ajouter le sommet
                        graph.addConnection(i + 1, j + 1);
                    }

                }

            }
        }

        System.out.println(graph);

        // Sauvegarder le graph dans un fichier au nom indiqué
        graph.save(NameGraphToSave);

    }



    /*
            ~~~~~~~~~~~~     Getter And Setter        ~~~~~~~~~~~~
    */

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public Map<Integer, List<Integer>> getTableau() {
        return tableau;
    }

    public void setTableau(Map<Integer, List<Integer>> tableau) {
        this.tableau = tableau;
    }


}
