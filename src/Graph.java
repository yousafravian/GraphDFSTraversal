import java.util.ArrayList;
import java.util.Iterator;

public class Graph implements GraphADT {

    private ArrayList<ArrayList<Edge>> graphList = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();

    public Graph(int n) {
        for (int i = 0; i < n; i++) {
            // Create its list in adjacencyList
            graphList.add(new ArrayList<>());

            // Create node
            nodes.add(new Node(i));
        }
    }

    public Node getNode(int name) throws GraphException {

        // return false
        if (name >= nodes.size()) throw new GraphException("No such Node exists");

        // Index of the node in nodesList is also the name of node
        return this.nodes.get(name);
    }

    public void insertEdge(Node u, Node v, int edgeType) throws GraphException {
        // Null check
        if (u == null || v == null) return;
        if (u.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");
        if (v.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");

        // Create edge from u to v
        graphList.get(u.getName()).add(new Edge(u, v, edgeType));

        // Create edge from v to u
        graphList.get(v.getName()).add(new Edge(v, u, edgeType));
    }

    public Iterator<Edge> incidentEdges(Node u) throws GraphException {
        // Null check
        if (u == null) return null;
        if (u.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");

        // Iterator of edge-list
        return graphList.get(u.getName()).iterator();
    }

    public Edge getEdge(Node u, Node v) throws GraphException {
        // Null check
        if (u == null || v == null) throw new GraphException("Null Node Provided");
        if (u.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");
        if (v.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");

        for (Edge e : graphList.get(u.getName())) {
            if (e.secondEndpoint().getName() == v.getName()) return e;
        }

        throw new GraphException("No edge exists between u and v");
    }

    public boolean areAdjacent(Node u, Node v) throws GraphException {
        // Null check
        if (u == null || v == null) throw new GraphException("Null Node Provided");
        if (u.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");
        if (v.getName() >= nodes.size()) throw new GraphException("Not Nodes of this graph");

        for (Edge e : graphList.get(u.getName())) {
            // Check with connected nodes
            if (e.secondEndpoint().getName() == v.getName()) return true;
            // If not then check with if it has two edge connection, which is also considered as adjacent node
            else
                for (Edge _e : graphList.get(e.secondEndpoint().getName()))
                    if (e.secondEndpoint().getName() == v.getName()) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        for (Node n: nodes) {
            builder.append("Node-").append(n.getName()).append(": ");

            for (Edge _n: graphList.get(n.getName())) {
                builder.append(_n.secondEndpoint().getName()).append("-").append(_n.getType()).append(" => ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
