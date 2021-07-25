import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class RoadMap {
    private Scanner reader;
    private Graph graph;

    private int SCALE, START, END, WIDTH, LENGTH, INITIAL_BUDGET, TOLL, GAIN;
    private ArrayList<ArrayList<Character>> map = new ArrayList<>();
    private Stack<Node> path;
    public RoadMap(String fileName) throws MapException, GraphException {
        openFileInScanner(fileName);
        readValuesAndCreateGraph();
        readTokensIntoMap();
        createGraphFromMap();
    }

    private void openFileInScanner(String fileName) throws MapException {
        try {
            reader = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            throw new MapException("No such file found");
        }
    }

    private void readValuesAndCreateGraph() {
        SCALE = Integer.parseInt(reader.nextLine());
        START = Integer.parseInt(reader.nextLine());
        END = Integer.parseInt(reader.nextLine());
        WIDTH = Integer.parseInt(reader.nextLine());
        LENGTH = Integer.parseInt(reader.nextLine());
        INITIAL_BUDGET = Integer.parseInt(reader.nextLine());
        TOLL = Integer.parseInt(reader.nextLine());
        GAIN = Integer.parseInt(reader.nextLine());
        graph = new Graph(WIDTH * LENGTH);
    }

    private void readTokensIntoMap() {
        // Populate tokens
        while (reader.hasNext()) {
            String line = reader.nextLine();
            ArrayList<Character> charArray = line.chars().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
            map.add(charArray);
        }
    }

    private void createGraphFromMap() throws GraphException {
        // Create Map from tokens
        for (int i = 0; i < LENGTH; i++) {
            // X-Direction
            for (int j = 0; j < WIDTH; j++) {
                // Skip first vertex
                if (j == 0) continue;


                int _currNode = (WIDTH * i) + j;
                int _prevNode = _currNode - 1;

                // Get nodes from their names
                Node currNode = graph.getNode(_currNode);
                Node prevNode = graph.getNode(_prevNode);

                // Get Link between these nodes
                char link = map.get(i * 2).get((j * 2) - 1);

                // Create Edge between nodes
                switch (link) {
                    case 'F' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.PUBLIC);
                    case 'C' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.REWARD);
                    case 'T' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.PRIVATE);
                }
            }
        }

        // Create Map from tokens
        for (int j = 0; j < WIDTH; j++) {
            // Y-Direction
            for (int i = 0; i < LENGTH; i++) {
                // Skip first vertex
                if (i == 0) continue;


                int _currNode = (WIDTH * i) + j;
                int _prevNode = _currNode - WIDTH;

                // Get nodes from their names
                Node currNode = graph.getNode(_currNode);
                Node prevNode = graph.getNode(_prevNode);

                // Get Link between these nodes
                char link = map.get((i * 2) - 1).get(j * 2);

                // Create Edge between nodes
                switch (link) {
                    case 'F' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.PUBLIC);
                    case 'C' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.REWARD);
                    case 'T' -> graph.insertEdge(currNode, prevNode, ROAD_TYPES.PRIVATE);
                }
            }
        }
    }

    public Graph getGraph() {
        return this.graph;
    }

    public int getStartingNode() {
        return this.START;
    }

    public int getDestinationNode() {
        return this.END;
    }

    public int getInitialMoney() {
        return this.INITIAL_BUDGET;
    }

    public Iterator<Node> findPath(int start, int destination, int initialMoney) throws GraphException {

        // Fetch starting node
        Node startingNode = graph.getNode( start );

        // Global scope to avoid passing on every c
        path = new Stack<>(); // Always initialize a new stack before finding solution

        // Push starting node to path
        path.push(startingNode);

        // Recursive DFS call
        boolean isPath = DfsUtil(startingNode, initialMoney, destination);

        // If path was not found return empty iterator
        return isPath ? path.iterator() : Collections.emptyIterator();
    }


    private boolean DfsUtil(Node node, int money, int destination) throws GraphException {


        // If node is already visited return false ( false = path not complete/found )
        if ( node.isVisited() ) return false;

        // If node is destination, return true ( true = valid path is found )
        if ( node.getName() == destination ) return true;

        // Mark node as visited
        node.setMark( true );

        // Get list of edges on this node
        Iterator<Edge> incidentEdges = graph.incidentEdges(node);

        // Iterate through edges
        while (incidentEdges.hasNext()) {
            // get edge
            Edge e = incidentEdges.next();

            // Get destination node of edge, potential node to hop to ( can also be destination )
            Node n = e.secondEndpoint();

            // Get cost depending on edge type, see Edge:Road_TYPES class
            int cost = switch (e.getType()) {
                case ROAD_TYPES.PRIVATE -> -1 * TOLL;
                case ROAD_TYPES.REWARD -> GAIN;
                default -> 0;
            };

            // If node is not visited or does have money to pay the cost ( if any ), then jump to this node;
            if ( !n.isVisited() && money + cost >= 0 ) {

                // Add it to path (global)
                path.push(n);

                // Recursive call of next node
                boolean isPath = DfsUtil(n, money + cost, destination);

                // If path is found return true and keep returning till end of this recursive stack.
                if ( isPath ) return true;

                // If path is not found then pop that from from path.
                else path.pop();
            }
        }

        // If all edges of nodes are exhausted and no path is found then return false
        return false;
    }
}
