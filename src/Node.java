public class Node {
    private int name;
    private boolean visited;

    public Node(int name) {
        this.name = name;
        this.visited = false;
    }

    public void setMark(boolean mark) {
        this.visited = mark;
    }

    public boolean getMark() {
        return this.visited;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public int getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name + "";
    }
}
