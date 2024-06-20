public class Node {
    int x, y, gScore, fScore;
    String key;
    Node cameFrom;

    Node(int x, int y, int gScore, int hScore) {
        this.x = x;
        this.y = y;
        this.gScore = gScore;
        this.fScore = gScore + hScore;
        this.key = x + "," + y; // Unique key for each node
    }
}