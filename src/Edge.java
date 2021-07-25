class ROAD_TYPES {
    public static final int PUBLIC =0, PRIVATE = 1, REWARD = -1;
}

public class Edge {
    private Node firstEndPoint, secondEndPoint;
    private int type;

    public Edge(Node u, Node v, int type) {
        this.firstEndPoint = u;
        this.secondEndPoint = v;
        this.type = type; // 0 = public, 1 = private, -1 = reward
    }

    public Node firstEndpoint() {
        return firstEndPoint;
    }

    public Node secondEndpoint() {
        return secondEndPoint;
    }

    public int getType() {
        return type;
    }
}

