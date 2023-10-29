import java.io.*;
import java.util.*;

//Prøvde å lage en graf med en hashmap fra alle noder til alle kantene, men ble for mye å gå gjennom
//Endte med å bare ha et par hashmaps som jeg referer til
public class Graph {
    File mvs, actrs;
    HashMap<String, Actors.Node> actorIdToNodes; // Alle node ider til noder
    static HashMap<String, Movies.Edge> movieToEdge; // Alle filmid til film objekter
    HashMap<String, Set<Actors.Node>> movieToNodes; // Film id til alle skuespillerene i filmen.
    static HashMap<Actors.Node, Set<String>> nodesToEdge; // Alle node objekter til deres filmer
    // Kunne sikkert vært letter å bruke nøstede hashmaps

    public Graph(File mvs, File actrs) {
        long startTime = System.nanoTime();

        this.mvs = mvs;
        this.actrs = actrs;
        Actors actorsFile = new Actors(actrs);
        Movies movieFile = new Movies(mvs);
        movieToEdge = movieFile.getMovieMap();
        actorIdToNodes = actorsFile.getNodeMap();
        nodesToEdge = actorsFile.getNodeToEdge();
        // HashMap<Actors.Node, Set<Movies.Edge>> graph = new HashMap<>();
        movieToNodes = actorsFile.getGraph();
        long endTime = System.nanoTime();
        double elapsedTimeSeconds = (endTime - startTime) / 1e9; // tid i sekunder
        System.out.println(elapsedTimeSeconds + " sekunder tid brukt på laging av grafen");

    }

    public HashMap<Actors.Node, Actors.Node> findShortestPath(Actors.Node actor1, Actors.Node actor2,
            Set<Actors.Node> visited) {

        HashMap<Actors.Node, Actors.Node> pathToSix = new HashMap<>();
        if (nodesToEdge.containsKey(actor1) && nodesToEdge.containsKey(actor2)) {
            visited.add(actor1);
            Queue<Actors.Node> queue = new LinkedList<>();
            queue.add(actor1);
            while (!queue.isEmpty()) {
                Actors.Node current = queue.poll();
                if (current == actor2) {
                    return pathToSix; // Funnet korteste veien
                }
                Set<String> mviLst = nodesToEdge.get(current);
                for (String movies : mviLst) {
                    Set<Actors.Node> actorLst = movieToNodes.get(movies);
                    for (Actors.Node actors : actorLst) {
                        if (!visited.contains(actors)) {
                            visited.add(actors);
                            pathToSix.put(actors, current);
                            queue.add(actors);
                        }
                    }
                }
            }

        } else {
            System.err.println("Fant ikke skuespilleren: ");
        }
        return pathToSix;
    }

    public void BFS(String id1, String id2) {
        Set<Actors.Node> visited = new HashSet<>();
        HashMap<Actors.Node, Actors.Node> path = null;
        Actors.Node actor1 = actorIdToNodes.get(id1);
        Actors.Node current = actorIdToNodes.get(id2);
        path = findShortestPath(actor1, current, visited);

        backTrackPath(false, path, actor1, current);

    }

    public HashMap<Actors.Node, Float> Dijkstra(String first, String last) {
        Actors.Node start = actorIdToNodes.get(first), end = actorIdToNodes.get(last);
        Set<Actors.Node> visited = new HashSet<>();
        Queue<Actors.Node> queue = new PriorityQueue<>();
        HashMap<Actors.Node, Float> distance = new HashMap<>();
        HashMap<Actors.Node, Actors.Node> path = new HashMap<>();
        distance.put(start, (float) 0);
        queue.add(start);
        Graph.Actors.Node.distance = distance;
        while (!queue.isEmpty()) {
            Actors.Node current = queue.poll();
            if (current == end) {
                backTrackPath(true, path, start, end);
                return distance;
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            Set<String> mviLst = nodesToEdge.get(current);
            for (String movie : mviLst) {
                Set<Actors.Node> actrs = movieToNodes.get(movie);
                for (Actors.Node node : actrs) {
                    if (!visited.contains(node)) {
                        float weight = distance.get(current) + findWeight(current, node, movie);
                        if (weight < distance.getOrDefault(node, Float.MAX_VALUE)) {
                            path.put(node, current);
                            distance.put(node, weight);
                            queue.add(node);
                            // visited.add(current);
                        }
                    }
                }
            }
        }
        return distance;
    }

    private void backTrackPath(boolean weight,
            HashMap<Graph.Actors.Node, Graph.Actors.Node> path, Graph.Actors.Node start, Graph.Actors.Node end) {
        float movieRating = 0;
        Stack<String> output = new Stack<>();
        while (start != end) { // Backtracker til orginale skuespilleren
            Set<String> curMovies = nodesToEdge.get(end);
            Actors.Node parent = path.get(end);
            Set<String> parentMovie = nodesToEdge.get(parent);
            for (String string : parentMovie) {
                if (curMovies.contains(string)) { // Tror jeg egt burde sjekke filem sin høyeste rating i tilfelle de
                                                  // har flere filmer sammen?
                    Movies.Edge mvs = movieToEdge.get(string);
                    output.push("===[ " + mvs.name + " (" + mvs.rating + ") ] ===> " +
                            end.name);
                    movieRating += mvs.rating;
                    end = parent;

                    break;
                }
            }
        }
        System.out.println(end.name);
        int size = output.size();
        while (!output.isEmpty()) {
            System.out.println(output.pop());
        }
        if (weight) {
            System.out.print("Total weight: ");
            System.out.println((size * 10) - movieRating);
        }
        System.out.println("\n");
    }

    private float findWeight(Actors.Node current, Actors.Node parent, String movie) {
        float weight = Integer.MAX_VALUE;
        if (movieToEdge.containsKey(movie))
            if (movieToEdge.get(movie).rating < weight) {
                weight = 10 - movieToEdge.get(movie).rating;
            }
        return weight;
    }

    public void findComponents() {
        HashMap<Integer, Integer> components = new HashMap<>();

        Set<Actors.Node> visted = new HashSet<>();
        for (Actors.Node node : nodesToEdge.keySet()) {
            if (!visted.contains(node)) {
                int count = DFS(node, visted);
                if (components.containsKey(count)) {
                    components.put(count, components.get(count) + 1);
                } else {
                    components.put(count, 1);
                }

            }

        }
        Stack<String> print = new Stack<>();
        for (Map.Entry<Integer, Integer> entry : components.entrySet()) {
            print.add("There are " + entry.getValue() + " components of size " + entry.getKey());
        }
        while (!print.isEmpty()) {
            System.out.println(print.pop());
        }
        System.out.println();

    }

    private Integer DFS(Graph.Actors.Node node, Set<Graph.Actors.Node> visted) {
        Stack<Actors.Node> stack = new Stack<>();
        stack.add(node);
        int count = 0;

        while (!stack.isEmpty()) {
            Actors.Node current = stack.pop();
            if (visted.contains(current)) {
                continue;
            }
            count++;
            visted.add(current);
            Set<String> curMovie = nodesToEdge.get(current);
            for (String movie : curMovie) {
                if (movieToEdge.containsKey(movie)) {
                    Set<Actors.Node> actrs = movieToNodes.get(movie);
                    for (Actors.Node parent : actrs) {
                        if (!visted.contains(parent)) {
                            stack.push(parent);
                        }
                    }
                }
            }
        }
        return count;
    }

    public static class Actors {
        File ac;
        HashMap<String, Node> actorMap = new HashMap<>();
        HashMap<Node, Set<String>> actorToEdgeMap = new HashMap<>();
        HashMap<String, Set<Node>> movies = new HashMap<>();

        public Actors(File ac) {
            this.ac = ac;
            try {
                Scanner sc = new Scanner(ac);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] splitString = line.split("\t");
                    Node node = new Node(splitString[0], splitString[1]);
                    int len = splitString.length;
                    Set<String> indivEdge = new HashSet<>();
                    for (int i = 2; i < len; i++) {
                        if (!movies.containsKey(splitString[i])) {
                            Set<Node> set = new HashSet<>();
                            set.add(node);
                            movies.put(splitString[i], set);
                        } else {
                            Set<Node> k = movies.get(splitString[i]);
                            k.add(node);
                            movies.put(splitString[i], k);
                        }
                        indivEdge.add(splitString[i]);
                    }
                    actorToEdgeMap.put(node, indivEdge);
                    actorMap.put(splitString[0], node);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public HashMap<String, Node> getNodeMap() {
            return actorMap;
        }

        public HashMap<Node, Set<String>> getNodeToEdge() {
            return actorToEdgeMap;
        }

        public HashMap<String, Set<Node>> getGraph() {
            return movies;
        }

        public static class Node implements Comparable<Node> {
            String name, id;
            public static HashMap<Actors.Node, Float> distance;
                                                                

            public Node(String id, String name) {
                this.name = name;
            }

            @Override
            public int compareTo(Node o) {
                float myDistance = distance.get(this);
                float otherDistance = distance.get(o);
                return Float.compare(myDistance, otherDistance);

            }

        }
    }

    public static class Movies {
        File fil;
        HashMap<String, Edge> map = new HashMap<>();

        public Movies(File fil) {
            this.fil = fil;
            try {
                Scanner sc = new Scanner(fil);
                sc.nextLine();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] splitString = line.split("\t");
                    Edge movie = new Edge(splitString[1], Float.parseFloat(splitString[2]),
                            Integer.parseInt(splitString[3]));
                    map.put(splitString[0], movie);
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public HashMap<String, Edge> getMovieMap() {
            return map;
        }

        public static class Edge {
            public String name;
            public float rating;
            public int votes;
            public Actors.Node startReference = null, endReference = null;
            ArrayList<Actors.Node> actorsList = new ArrayList<>();

            public Edge(String name, float rating, int votes) {
                this.name = name;
                this.rating = rating;
                this.votes = votes;
            }
        }

    }

    public static void main(String[] args) {
        long wholeStartTime = System.nanoTime();
        Graph g = new Graph(new File("imdb/movies.tsv"), new File("imdb/actors.tsv"));
        long startTime = System.nanoTime();
        g.BFS("nm2255973", "nm0000460");
        g.BFS("nm0424060", "nm8076281");
        g.BFS("nm4689420", "nm0000365");
        g.BFS("nm0000288", "nm2143282");
        g.BFS("nm0637259", "nm0931324");
        long endTime = System.nanoTime();
        double elapsedTimeSeconds = (endTime - startTime) / 1e9; 
        System.out.println(elapsedTimeSeconds + " sekunder tid brukt på å finne korteste vei\n\n");
        long dijkstraStart = System.nanoTime();
        g.Dijkstra("nm2255973", "nm0000460");
        g.Dijkstra("nm0424060", "nm8076281");
        g.Dijkstra("nm4689420", "nm0000365");
        g.Dijkstra("nm0000288", "nm2143282");
        g.Dijkstra("nm0637259", "nm0931324");
        long dijkstraEnd = System.nanoTime();
        double dijkstraElapsed = (dijkstraEnd - dijkstraStart) / 1e9;
        System.out.println(dijkstraElapsed + " sekunder tid brukt på å finne chilleste vei\n\n");
        g.findComponents();
        long wholeEndTime = System.nanoTime();
        double wholeTime = (wholeEndTime - wholeStartTime) / 1e9;
        System.out.println(wholeTime + "Sekunder på hele oppgaven");
    }

}
