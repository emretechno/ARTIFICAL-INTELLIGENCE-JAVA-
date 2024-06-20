import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class Wumpus {

    private final String filename ="wumpus_world3.txt";
    protected String[][] cave;
    protected int X;
    protected int Y;
    private String input_coord;
    private char direction_char;
    int curr_agent_x;
    int curr_agent_y;
    int max_gold;
    private String[][] content;
    private int arrow = 1;
    private Direction face_direction;
    int goal_X;
    int goal_Y;
    protected Map<String, Boolean> knowledge_base;

    Wumpus(){
        goal_X = 0;
        goal_Y = 0;
        max_gold = 0;
        knowledge_base = new HashMap<>();
        System.out.println("Welcome to Wumpus World");
        face_direction = Direction.RIGHT;
        load_from_file(filename);
        input_coord ="1,1";
       

    }

 
    

  protected void throw_arrow(int x, int y){

      if(arrow == 0) return;
      arrow -=1;
    
     if(check_out_of_cave(x,y)) return;
  System.out.printf("Arrow thrown to %d %d\n",x,y);
    for(int i =0; i< X ; i++){
        for(int  j = 0 ; j< Y; j++){
            if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(x+","+y)){
                if(cave[i][j].contains("W")){
                    // There is a Wumpus nearby
                    System.out.println("YOU KILLED THE WUMPUS !. STENCHIES ARE GONE");
                    String temp = cave[i][j].replace("W","");
                    cave[i][j] = temp;
                   if(i+1 <=X){
                    if(cave[i+1][j].contains("S")){
                        temp = cave[i+1][j].replace("S","");
                        cave[i+1][j] = temp;
                    }
                   }
                     if(i-1 >=1){
                      if(cave[i-1][j].contains("S")){
                            temp = cave[i-1][j].replace("S","");
                            cave[i-1][j] = temp;
                      }
                     }
                        if(j+1 <=Y){
                        if(cave[i][j+1].contains("S")){
                                temp = cave[i][j+1].replace("S","");
                                cave[i][j+1] = temp;
                        }
                        }
                        if(j-1 >=1){
                        if(cave[i][j-1].contains("S")){
                                temp = cave[i][j-1].replace("S","");
                                cave[i][j-1] = temp;
                        }
                        }



                }
                else{
                    System.out.println("YOU MISSED");
                }
                break;
            }
        }
    }
}

   


protected void run(){
     
       Scanner sc= new Scanner(System.in);    //System.in is a standard input stream
       while(!is_dead()){
        get_current_direction();

            System.out.println("Enter the direction you want to move the agent or change the direction (W = UP , A = LEFT , S =DOWN ,D = RIGHT) :");
                
                input_coord = sc.nextLine(); 
                
               if(input_coord.equals("q")) System.exit(0);
               
                   
                while(!is_valid_movement()){

                    input_coord= sc.nextLine();

                } 
                  
                     make_movement();
                     print_cave();   
                     check_smell();
                    
                }
                sc.close();
           
            


}
private boolean is_chancing_direction(){

    if((input_coord.equals("W") || input_coord.equals("w") ||  input_coord.equals("D") || input_coord.equals("d") || input_coord.equals("A") || input_coord.equals("a") || input_coord.equals("S") || input_coord.equals("s"))){
        direction_char = Character.toUpperCase(input_coord.charAt(0));
        return true;
       }
       return false;

}
protected void  find_agent_coord(){


    for(int i = 0; i< X; i++){
        for(int j = 0; j< Y; j++){
            if(cave[i][j].contains("A")){

               curr_agent_x = Integer.parseInt(cave[i][j].substring(cave[i][j].length()-3,cave[i][j].length()-2));
               curr_agent_y = Integer.parseInt(cave[i][j].substring(cave[i][j].length()-1,cave[i][j].length()));
        
            }
        }
    }
    
}




 protected void check_smell(){

       for(int i =0 ; i< X ; i++){

        for(int j = 0;  j< Y ; j++){

          if(cave[i][j].length() >=3){
         if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(curr_agent_x+","+curr_agent_y)){

             if(cave[i][j].contains("B")){
                    System.out.printf("Breeze sensed \n");
             }
         }
       
        
         if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(curr_agent_x+","+curr_agent_y)){
             if(cave[i][j].contains("S")){
                    System.out.printf("Stenchy sensed \n");
             }
         }
           
        }
    }
       }
   }
 private void load_from_file(String filename){
    
    try {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        List<String> lines = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
      
        for (String l : lines) {

            if (l.startsWith("M")) {
                X = Integer.parseInt(l.substring(1, 2));
                Y = Integer.parseInt(l.substring(2, 3));
                break;
            }
        }
    
       init_cave();
        for (String l : lines) {//problem on parsing

            if (l.startsWith("A")) {
                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"A");
            } else if (l.startsWith("B")) {
                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"B");
            } else if (l.startsWith("G" ) && !l.startsWith("GO")) {

                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"G");
            } else if (l.startsWith("GO")) {
                int a = Integer.parseInt(l.substring(2, 3));
                int b = Integer.parseInt(l.substring(3, 4));
                goal_X = a;
                goal_Y = b;
                get_valid_index(a-1, b-1,"GO");


            } else if (l.startsWith("P")) {

                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"P");
            } else if (l.startsWith("S")) {
                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"S");
            } else if (l.startsWith("W")) {
                int a = Integer.parseInt(l.substring(1, 2));
                int b = Integer.parseInt(l.substring(2, 3));
                get_valid_index(a-1, b-1,"W");
            }
           
           
        }

        synchron_content();
        System.out.println("\n\n  INITIAL CAVE IS : \n");
        print_cave();
    
    } catch (IOException e) {
        e.printStackTrace();
    }

}
private boolean is_dead(){

        
    for(int i = 0; i< X; i++){
        for(int j = 0; j< Y; j++){
            if(cave[i][j].contains("W") && cave[i][j].contains("A") || (cave[i][j].contains("P") && cave[i][j].contains("A"))){
                System.out.println("YOU ARE DEAD\n");
                return true;
            }
        }
    }
    return false;
}
private void get_current_direction(){

    System.out.printf("Agent's current direction is : %s\n",face_direction);
    System.out.printf("-------------------------\n");
}


protected boolean check_out_of_cave(int x , int y){

    if(x <=0 | x > X | y <=0 | y > Y){
        return true;
    }
    return false;
}

private void make_movement(){

   
     find_agent_coord();

     if(is_throwing()){

         if(face_direction == Direction.RIGHT){
             throw_arrow(curr_agent_x+1,curr_agent_y);
         }
         else if(face_direction == Direction.LEFT){
             throw_arrow(curr_agent_x-1,curr_agent_y);
         }
         else if(face_direction == Direction.UP){
             throw_arrow(curr_agent_x,curr_agent_y+1);
         }
         else{
             throw_arrow(curr_agent_x,curr_agent_y-1);
         }

         return;
     }

     if(is_chancing_direction()) {
         change_face_direction();
         return;
     }

    int x = Integer.parseInt(input_coord.substring(0,1));
    int y = Integer.parseInt(input_coord.substring(2,3));
  
       for(int i = 0 ; i< X ; i++){
        for(int j = 0 ; j< Y; j++){
//adding agent
            if(cave[i][j].substring(cave[i][j].length()-3 , cave[i][j].length()).equals(x+","+y)){
       
                String temp = "A,";
                temp += cave[i][j];
                cave[i][j] = temp;
//deleting gold 
                if(cave[i][j].contains("G") && !cave[i][j].contains("GO")) {
                    temp  = cave[i][j].replace("G","");
                    cave[i][j] = temp;
                 }
            }
 //dleeting agent
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
       System.out.printf("Move to field ( %d , %d )\n",x,y);
        
       System.out.printf("-------------------------\n");

}
  private void change_face_direction(){

        if(direction_char == 'W'){
            face_direction = Direction.UP;
        }
        if(direction_char == 'D'){
            face_direction = Direction.RIGHT;
        }
        if(direction_char == 'A'){
            face_direction = Direction.LEFT;
        }
        if(direction_char == 'S'){
            face_direction = Direction.DOWN;
        }
        System.out.println("Agent's new direction is : " + face_direction);
        check_smell();
        System.out.printf("-------------------------\n");
  }
 
private boolean is_throwing(){

    if(input_coord.equals("T") || input_coord.equals("t")) return true;

    return false;

}
private boolean is_valid_movement(){


         if(is_throwing() || is_chancing_direction()) return true;
         find_agent_coord();
                    

       if(input_coord.length() != 3 || !input_coord.contains(",")){

         if(!is_chancing_direction() && !is_throwing() )  System.out.println("Invalid input");

           return false;
       } 
       
      int x = Integer.parseInt(input_coord.substring(0,1));
      int y = Integer.parseInt(input_coord.substring(2,3));
 System.out.printf("Agen it at %d %d\n",curr_agent_x,curr_agent_y);
 System.out.printf("You want to move to %d %d\n",x,y);
                 
      if( (face_direction == Direction.RIGHT) && (x < curr_agent_x || y != curr_agent_y)){
          System.out.println("You can only move right.Your direction is right");
          return false;
      }

      else if( (face_direction==Direction.UP) && (y < curr_agent_y || x != curr_agent_x )){
          System.out.println("You can only move up.Your direction is up");
          return false;
      }
       else if((face_direction==Direction.LEFT) && (curr_agent_x < x || y != curr_agent_y)){
            System.out.println("You can only move left.Your direction is left");
            return false;
        }
        else if((face_direction==Direction.DOWN)&& (y> curr_agent_y || x != curr_agent_x)){
            System.out.println("You can only move down.Your direction is down");
            return false;
        }
      if(check_out_of_cave(x, y)) {
        return false;
      }

      if(x == curr_agent_x && y == curr_agent_y){

        System.err.printf("You are already at %d %d\n",x,y);
         return false;

      }


     if(Math.abs(curr_agent_x - x) >1 || Math.abs(curr_agent_y - y) >1){

        System.out.println("You can only move one step at a time");
        return false;
    
    }

      return true;
  
}

private void init_cave(){

cave = new String[X][Y];
content = new String[X][Y];

for(int i =0 ; i< X ; i++){

    for(int j = 0; j< Y ;j++){
       //conver integer to string.
       cave[i][j] = j+ "," + (X-i-1);
    }
}

}


private void get_valid_index(int x, int y,String inf){


for(int i=0 ; i< X; i++){

    for(int j = 0; j< Y; j++){

        if(cave[i][j].equals(x + "," + y)){
            content[i][j] += ","+inf;
            
        }
    }
}
}

private void synchron_content(){

for(int i = 0 ; i< X ; i++){
    for(int j = 0; j< Y; j++){
        if(content[i][j]==null){
          String temp ="";
          temp +=j+1 + "," + (X-i);
        cave[i][j] = temp;
        }
        else {
          cave[i][j] = content[i][j].substring(5,content[i][j].length());
          cave[i][j] += j+1 + "," + (X-i); 
        }
    }
}
}

public void print_cave(){

System.out.printf("\n|-------------------------------------------------------|\n|             |             |             |             |\n");

for (int i = 0; i <X; i++) {
for (int j = 0; j < Y; j++) {
    
   // System.out.printf("| %9s   ",cave[i][j].substring(0,cave[i][j].length()-3));
      System.out.printf("| %9s   ",cave[i][j]);

}
System.out.printf("|\n|             |             |             |             |\n|-------------------------------------------------------|\n");
if(i !=X -1 )System.out.printf("|             |             |             |             |\n");
        
    
}
System.out.println();

}


 

}

