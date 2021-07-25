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

    // TOOD: INCOMPELETE
    public Iterator<Node> findPath(int start, int destination, int initialMoney) throws GraphException {
        Graph g = getGraph();
        DfsUtil(g, g.getNode( start ), initialMoney);

        /*// x = nodeName, y = cost to travel to that node
        Stack<Point> stack = new Stack<>();
        Stack<Point> path = new Stack<>();
        stack.push(new Point(start, 0));


        while (!stack.isEmpty()) {
            Point _currentPoint = stack.pop();
            Node current = g.getNode(_currentPoint.x);
            if ((initialMoney + _currentPoint.y) >= 0) {
                if (!current.isVisited()) {
                    initialMoney += _currentPoint.y;
                    path.push(_currentPoint);
                    Iterator<Edge> incidentEdges = g.incidentEdges(current);

                    while (incidentEdges.hasNext()) {
                        Edge e = incidentEdges.next();
                        Node n = e.secondEndpoint();
                        if (!n.isVisited() && initialMoney >= 0) {
                            int cost = switch (e.getType()) {
                                case ROAD_TYPES.PRIVATE -> -1 * TOLL;
                                case ROAD_TYPES.REWARD -> GAIN;
                                default -> 0;
                            };

                            stack.push(new Point(n.getName(), cost));
                        }
                    }
                }
                // Mark Visited
                current.setMark(true);
            }
        }

        return null;*/
        return null;
    }

    // TOOD: INCOMPELETE
    private boolean DfsUtil(Graph g, Node node, int money) throws GraphException {
        if ( node.isVisited() ) return false;
        Iterator<Edge> incidentEdges = g.incidentEdges(node);

        while (incidentEdges.hasNext()) {
            Edge e = incidentEdges.next();
            Node n = e.secondEndpoint();
            boolean isPath = DfsUtil(g, n, money);
        }
        return false;
    }
}
