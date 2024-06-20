import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Wmp_Heuristic extends Wumpus { // Wumpus class is the parent class.Heuristic class is the child class.


    Wmp_Heuristic() {
        super();
    }

    public static void main(String[] args) {
        Wmp_Heuristic wumpus = new Wmp_Heuristic();
        wumpus.run_A_star();
    }

    private void hlp_w(int x, int y) {
        int temp = x - 1;
        if (ask("Wumpus at " + temp + "," + y)) throw_arrow(temp, y);
        temp = x + 1;
        if (ask("Wumpus at " + temp + "," + y)) throw_arrow(temp, y);
        temp = y - 1;
        if (ask("Wumpus at " + x + "," + temp)) throw_arrow(x, temp);
        temp = y + 1;
        if (ask("Wumpus at " + x + "," + temp)) throw_arrow(x, temp);
    }

    private void run_A_star() {
        find_agent_coord();
        List<int[]> path = aStar(curr_agent_x, curr_agent_y);

        if (path == null) {
            if (ask("Pit at " + goal_X + "," + goal_Y)) {
                System.err.println("There is a pit on the goal cell. Sure there is no way to reach the goal!");
                System.exit(0);
            } else {
                System.err.println("NO PATH FOUND!!");
                System.exit(0);
            }
        } else {
            System.out.println("\n\nThe result path is : \n");
            for (int[] p : path) {
                System.out.printf("-----------------------------------------------------------\n");
                System.out.printf("Move to field ( %d , %d )\n", p[0], p[1]);

                get_info(p[0], p[1]);
               
                make_movement_ai(p[0], p[1]);

                curr_agent_x = p[0];
                curr_agent_y = p[1];
                hlp_w(p[0], p[1]);
                System.out.printf("-----------------------------------------------------------\n");
                    check_smell();

                if (ask("Gold at " + p[0] + "," + p[1])) {

                    System.out.println("You found the gold! Congratulations");
                    
                }

                if (ask("Pit at " + p[0] + "," + p[1])) {
                    System.out.println("You fell into a pit");
                    System.exit(0);
                }
            }
            System.out.printf("-------------------------\n");
            System.out.printf("YOU HAVE REACHED THE GOAL!! CONGRATULATIONS\n");
        }
    }

    private void make_movement_ai(int x , int y ){

        for(int i = 0 ; i< X ; i++){
            for(int j = 0 ; j< Y; j++){
    //ekle
                if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(x+","+y)){
                  if(!cave[i][j].contains("A")) {

                    String temp = "A,";
                    temp += cave[i][j];
                    cave[i][j] = temp;

                  }
                  if(cave[i][j].contains("G") && !cave[i][j].contains("GO")) {
                    String temp = cave[i][j].replace("G","");
                    cave[i][j] = temp;
                  }
                }
     //silme
                else if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(curr_agent_x+","+curr_agent_y)){
                 if(cave[i][j].contains("A")) {
                        String temp;
                       if(curr_agent_x==1 && curr_agent_y == 1) temp = cave[i][j].replace("A","");
    
                       else  temp = cave[i][j].replace("A,","");
    
                        cave[i][j] = temp;
                 }


                 
                }
            }

    }
    print_cave();
}
    private void check_adjacent_cell(int x, int y) {
        
        if (check_out_of_cave(x, y)) return;

        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                if (cave[i][j].contains(x + "," + y)) {
                    if (cave[i][j].contains("W")) {
                        tell("Wumpus at " + x + "," + y, true);
                    }
                    if (cave[i][j].contains("P")) {
                        tell("Pit at " + x + "," + y, true);
                    }
                    if (cave[i][j].contains("G") && !cave[i][j].contains("GO")) {
                        tell("Gold at " + x + "," + y, true);
                    }
                    if (cave[i][j].contains("S")) {
                        tell("Stench at " + x + "," + y, true);
                    }
                }
            }
        }
    }

    private int heuristic(int x, int y) {
        int h = Math.abs(x - goal_X) + Math.abs(y - goal_Y);

        if (ask("Gold at " + x + "," + y) && !ask("Pit at " + x + "," + y) && !ask("Wumpus at " + x + "," + y)) {
            h -= 2000;
        }

        if (ask("Wumpus at " + x + "," + y)) {
            h += 1000;
        }

        if (ask("Pit at " + x + "," + y)) {
            h += 500;
        }

        return h;
    }

    private List<int[]> aStar(int startX, int startY) {
        // Priority queue to hold nodes to explore, sorted by fScore
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(a -> a.fScore));
        // Set to keep track of explored nodes
        Set<String> closedSet = new HashSet<>();
        // Map to keep track of nodes by their coordinates
        Map<String, Node> nodeMap = new HashMap<>();
    
        // Initialize the start node
        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY));
        openSet.add(startNode);
        nodeMap.put(startNode.key, startNode);
    
        while (!openSet.isEmpty()) {
            Node current = openSet.poll(); // Get node with lowest fScore
    
            // Check if goal is reached
            if (current.x == goal_X && current.y == goal_Y) {
                return reconstructPath(current); // Return path if goal is reached
            }
    
            closedSet.add(current.key); // Mark current node as explored
            get_info(current.x, current.y); // Retrieve information for current node
    
            // Explore neighbors
            for (int[] neighborCoords : getNeighbors(current.x, current.y)) {
                Node neighbor = new Node(neighborCoords[0], neighborCoords[1], current.gScore + moveCost(neighborCoords[0], neighborCoords[1]), heuristic(neighborCoords[0], neighborCoords[1]));
    
                // Skip if neighbor is already explored
                if (closedSet.contains(neighbor.key)) continue;
    
                // If neighbor is not in nodeMap or has a lower gScore, update it
                if (!nodeMap.containsKey(neighbor.key) || neighbor.gScore < nodeMap.get(neighbor.key).gScore) {
                    neighbor.cameFrom = current; // Set path from current to neighbor
                    openSet.add(neighbor); // Add neighbor to open set
                    nodeMap.put(neighbor.key, neighbor); // Update nodeMap
                }
            }
        }
    
        return null; // Return null if no path is found
    }
    
    // Class to represent a node in the grid

    
    // Reconstruct the path from start to goal
    private List<int[]> reconstructPath(Node current) {
        List<int[]> path = new ArrayList<>();
        while (current != null) {
            path.add(new int[]{current.x, current.y});
            current = current.cameFrom;
        }
        Collections.reverse(path); // Reverse path to start from the beginning
        return path;
    }
    

    private int moveCost(int x, int y) {
        int cost = 1;
        if (ask("Pit at " + x + "," + y)) {
            cost += 1000;
        }
        if (ask("Wumpus at " + x + "," + y)) {
            cost += 1000;
        }
        if (ask("Gold at " + x + "," + y)) {
            cost -= 1000;
        }
        return cost;
    }

    private void get_info(int x, int y) {
        check_adjacent_cell(x - 1, y);
        check_adjacent_cell(x + 1, y);
        check_adjacent_cell(x, y - 1);
        check_adjacent_cell(x, y + 1);
    }

    private void tell(String perception, boolean is_true) {
        knowledge_base.put(perception, is_true);
    }

    private boolean ask(String perception) {
        return knowledge_base.getOrDefault(perception, false);
    }

    private List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();

        if (x < X && !ask("Wumpus at " + (x + 1) + "," + y) && !ask("Pit at " + (x + 1) + "," + y)) {
            neighbors.add(new int[]{x + 1, y});
        }
        if (x > 1 && !ask("Wumpus at " + (x - 1) + "," + y) && !ask("Pit at " + (x - 1) + "," + y)) {
            neighbors.add(new int[]{x - 1, y});
        }
        if (y < Y && !ask("Wumpus at " + x + "," + (y + 1)) && !ask("Pit at " + x + "," + (y + 1))) {
            neighbors.add(new int[]{x, y + 1});
        }
        if (y > 1 && !ask("Wumpus at " + x + "," + (y - 1)) && !ask("Pit at " + x + "," + (y - 1))) {
            neighbors.add(new int[]{x, y - 1});
        }

        return neighbors;
    }
}
