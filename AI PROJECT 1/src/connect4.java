
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

public class connect4 extends JFrame {
    private final JPanel gameBoardPanel;
    int flag_ran=0;

    private JComboBox<String> player1ComboBox;
    private JComboBox<String> player2ComboBox;
   
    private final JButton startButton;
    private JButton helpButton; // New button for help
    private JButton QuitButton;

    private int player1Moves;
    private int player2Moves;
    private int totalMoves;
    private int curr_col =6;
    private int userType;
    private final int[] isComputer = new int[2];
    private final int [] button_count;
    private static final int widthTable = 1024;
    private static final int heightTable = 768;
    private static final int ROW = 6;
    private static final int COL =7;
    private static final int EMPTY = 0;
    private int[][] board;

    private boolean gameStarted;

 
    public connect4() {
        initialize_board();
        userType = 1;
        player1Moves = 0;
        player2Moves = 0;
        totalMoves = 0;
        isComputer[0] = 0;// Initialize the player types
        isComputer[1] = 0;// Initialize the player types
        gameStarted = false;
        button_count = new int[COL];
        for(int i= 0 ; i<COL ; i++) button_count[i] = 0;
        setTitle("MOVEMENTS OF PLAYER 1   ╔═══   "+ player1Moves + "   ═══╗   |       MOVEMENTS OF PLAYER 2   ╔═══   " + player2Moves +"   ═══╗   |     TOTAL MOVEMENTS╔══   " + totalMoves+"   ══╗");
        setSize(widthTable, heightTable);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Create a JPanel with BoxLayout to hold labels centered horizontally
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        // Add glue to center labels horizontally
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(Box.createHorizontalGlue());

        // Add the topPanel to the top of the BorderLayout
        add(topPanel, BorderLayout.NORTH);

        gameBoardPanel = new GameBoardPanel(board);
        gameBoardPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0)); // Adjust top padding as needed
        add(gameBoardPanel, BorderLayout.CENTER);

        // Other initialization...


        add(gameBoardPanel, BorderLayout.CENTER); // Add the game board panel to the center


        // Rest of the layout, for example, a game board or other components
        // ...
         // Add player type selection and start button
         JPanel controlPanel = new JPanel(new FlowLayout());
         player1ComboBox = new JComboBox<>(new String[]{"Human", "Computer"});
         player2ComboBox = new JComboBox<>(new String[]{"Human", "Computer"});
         startButton = new JButton("Start Game");
         helpButton = new JButton("Help"); // New button for help
         QuitButton = new JButton("Quit");

         controlPanel.add(new JLabel("Player 1 Type: "));
         controlPanel.add(player1ComboBox);
         controlPanel.add(new JLabel("Player 2 Type: "));
         
         controlPanel.add(player2ComboBox);
         controlPanel.add(startButton);
         controlPanel.add(helpButton); // Add the help button
         controlPanel.add(QuitButton);

         add(controlPanel, BorderLayout.SOUTH);
 
         // Add action listener for the start button
         startButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Handle the start button click
                 if(!gameStarted)
                 startGame();
                 gameStarted = true;
                 startButton.setEnabled(false);
                 player1ComboBox.setEnabled(false);
                 player2ComboBox.setEnabled(false);

                 // Repaint the JFrame to reflect the changes
                 revalidate();
                 repaint();
             }
         });
         startButton.setForeground(Color.BLUE);
             // Add action listener for the help button
             helpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle the help button click
                    connect4.this.showHelp(); // Use the class name to reference the showHelp method
                }
            });

            helpButton.setForeground(Color.RED);
        
            QuitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle the help button click
                    connect4.this.Quit(); // Use the class name to reference the showHelp method
                }
            });
        setVisible(true);
    }

    private void add_buttons(){
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 14, 1, 1)); // Adjust the number of columns and horizontal gap as per your requirement

        for (int i = 1; i <= 7; i++) {
            JButton button = new JButton("Button " + String.valueOf(i));
            button.addActionListener(createActionListener());
            panel.add(button);
            if (i < 7) { // Add spaces between buttons except for the last one
                panel.add(new JPanel()); // Add an empty JPanel acting as a space
            }
        }
        add(panel, BorderLayout.NORTH);
    

    }
    public void just_play_computer(){
        while(!is_end() && !is_draw()){
            computer_move();
            switchTurn();
            computer_move();    
        }
    }
    private ActionListener createActionListener() {
            
      if(isComputer[userType-1] == 1 ){
          computer_move();    
          check_game_result();
      }
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the source of the action event
                JButton clickedButton = (JButton) e.getSource();
                String buttonText = clickedButton.getText();
                
                // Extract the button number from the text
                String buttonNumberString = buttonText.substring(buttonText.lastIndexOf(" ") + 1);
                int buttonNumber = Integer.parseInt(buttonNumberString);
               
                switch (buttonNumber) {
                    case 1:
                        curr_col = 0;
                        break;
                    case 2:
                        curr_col = 1;
                        break;
                    case 3:
                        curr_col = 2;
                        break;
                    case 4:
                        curr_col = 3;
                        break;
                    case 5:
                        curr_col = 4;
                        break;
                    case 6:
                        curr_col = 5;
                        break;
                    case 7:
                        curr_col = 6;
                        break;
                    default:
                        break;
                }
                 
                try {
                    fill_board(get_curr_row(curr_col), curr_col);
                } catch (InvalidParameterException err) {
                    System.err.println("Invalid index: " + err.getMessage());
                }
                increase_move();
                check_if_col_full();
                button_count[curr_col]++;
                check_game_result();
                switchTurn();
                // Repaint the GameBoardPanel to reflect the changes
                gameBoardPanel.repaint();
                printMovementTable();
              
                add_buttons();
              
            }
        };
    }
    private void check_game_result(){

        if(is_end()){
            String endMessage = "";
          if(isComputer[userType-1] == 0)   endMessage = "Player " + userType + " wins the game!\n" + "Congratulations!\n";
          else  endMessage = "Computer" + userType + " wins the game!\n" + "Better luck next time!\n";

            JOptionPane.showMessageDialog(this, endMessage, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if(is_draw()){
            String drawMessage = "The game is a draw!\n" + "Better luck next time!\n";
            JOptionPane.showMessageDialog(this, drawMessage, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    
    }
private void reverse_turn(){

    if(userType ==2 ) userType = 1;
    else userType = 2;
   
}

private void check_if_col_full(){

    if(button_count[curr_col] >= ROW){
        JOptionPane.showMessageDialog(this, "This column is full! Please choose another column.", "Column Full", JOptionPane.ERROR_MESSAGE);
        decrease_move();
        reverse_turn();
    }
}


    private boolean is_end(){
        
        for(int i = 0 ; i < ROW ; i++){
            for(int j = 0 ; j < COL ; j++){
                if(board[i][j] == EMPTY) continue;
                if(j+3 < COL && board[i][j] == userType && board[i][j+1] == userType && board[i][j+2] == userType && board[i][j+3] == userType) return true;
                if(i+3 < ROW && board[i][j] == userType && board[i+1][j] == userType && board[i+2][j] == userType && board[i+3][j] == userType) return true;
                if(i+3 < ROW && j+3 < COL && board[i][j] == userType && board[i+1][j+1] == userType && board[i+2][j+2] == userType && board[i+3][j+3] == userType) return true;
                if(i-3 >= 0 && j+3 < COL && board[i][j] == userType && board[i-1][j+1] == userType && board[i-2][j+2] == userType && board[i-3][j+3] == userType) return true;
            }
        }
        return false;
    }
  private boolean is_draw(){
      
        for(int i = 0 ; i < COL ; i++){
            if(button_count[i] < ROW) return false;
        }
        return true;
  }

    private void increase_move(){
        if(userType == 1)
            player1Moves++;
        
        else
            player2Moves++;
        
    }
    private void decrease_move(){

        if(userType == 1)
            player1Moves--;
        
        else
            player2Moves--;
        
    }
    
    private int get_curr_row(int col){

        for(int i = ROW - 1 ; i >= 0 ; i--){

            if(board[i][col]==EMPTY){
    
                  return i;
            }
        }

        return -1; 
    }

    private void switchTurn() {

        if(userType == 1  )     userType = 2;
   
        else      userType = 1;

    }
  private void Quit(){
     String QuitMessage = "You quit the game !\n" + "Hope to see you again !\n";

     JOptionPane.showMessageDialog(this, QuitMessage,"See You Again!", JOptionPane.INFORMATION_MESSAGE);

     System.exit(0);
  }
  
    private void printMovementTable() {

        totalMoves = player1Moves + player2Moves;
        setTitle("MOVEMENTS OF PLAYER 1   ╔═══   "+ player1Moves + "   ═══╗   |       MOVEMENTS OF PLAYER 2   ╔═══   " + player2Moves +"   ═══╗   |     TOTAL MOVEMENTS╔══   " + totalMoves+"   ══╗");

    }
    private void showHelp() {
        
        String helpMessage = "Connect4 is a two-player connection game in which the\n"
                + "players first choose a color and then take turns dropping\n"
                + "one colored disc from the top into a seven-column,\n"
                + "six-row vertically suspended grid. The pieces fall\n"
                + "straight down, occupying the lowest available space\n"
                + "within the column. The object of the game is to connect\n"
                + "four of one's own discs of the same color next to each\n"
                + "other vertically, horizontally, or diagonally before\n"
                + "your opponent. Good luck and have fun playing!";
        
        JOptionPane.showMessageDialog(this, helpMessage, "Connect4 Game Help", JOptionPane.INFORMATION_MESSAGE);
    }
    private void initialize_board(){

          board = new int[ROW][COL];
        for(int i = 0; i< ROW ; i++){
            for(int j= 0 ; j < COL ; j++){
                board[i][j] = EMPTY;
            }
        }
        
     }

     private void fill_board(int row , int col){
        if(row == -1) return;

        if(row <0 || row >=ROW || col <0 || col >=COL) throw new InvalidParameterException("Invalid index");
        board[row][col] = userType;
     }
    private void startGame() {
        String selectedPlayer1Type = (String) player1ComboBox.getSelectedItem();
        String selectedPlayer2Type = (String) player2ComboBox.getSelectedItem();
    
        // Check if both player types are selected
        if (selectedPlayer1Type == null || selectedPlayer2Type == null) {
            JOptionPane.showMessageDialog(this, "You must select player types for both Player 1 and Player 2.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if not all player types are selected
        }
    
        // Output the selected player types (for demonstration purposes)
        String startMessageText = "The Game has started!\n"
                + "Player 1 Type : " + selectedPlayer1Type + "\n"
                + "Player 2 Type : " + selectedPlayer2Type + "\n"
                + "Have fun !\n";
    
        JTextArea startMessage = new JTextArea(startMessageText);
        startMessage.setForeground(Color.BLUE);
        startMessage.setEditable(false);
        startMessage.setOpaque(false);
    
        // Draw the game board before displaying the start message
        // Disable the start button after the game has started
        startButton.setEnabled(false);
        // Disable the player type selection after the game has started
        player1ComboBox.setEnabled(false);
        player2ComboBox.setEnabled(false);

                             JOptionPane.showMessageDialog(this, startMessage, "LET'S START!", JOptionPane.INFORMATION_MESSAGE);

                             add_buttons();
    
        // Implement the logic to start the game based on the selected player types
        if ("Human".equals(selectedPlayer1Type)) {
            // Start game with human player 1
        } else if ("Computer".equals(selectedPlayer1Type)) {
            // Start game with Computer-playing computer for player 1
          isComputer[0] = 1;
          computer_move();
            
        }
    
        if ("Human".equals(selectedPlayer2Type)) {
            // Start game with human player 2
        } else if ("Computer".equals(selectedPlayer2Type)) {
            // Start game with Computer-playing computer for player 2
            isComputer[1] = 1;
          
        }

        if(isComputer[0] == 1 && isComputer[1] == 1){
            just_play_computer();
        }
    }

    private boolean is_temp_valid(int[][] temp_board , int col){

        for(int i = ROW - 1 ; i>=0 ; i--){
            if(temp_board[i][col] == EMPTY) return true;
        }
    
        return false;
    }

private int getOpponent() {
    return userType == 1 ? 2 : 1;
}

private int[][] copyBoard() {

    int[][] copy = new int[ROW][COL];
    for (int i = 0; i < ROW; i++) {
        System.arraycopy(board[i], 0, copy[i], 0, COL);
    }
    return copy;
}


  private void computer_move(){


    int best_score = Integer.MIN_VALUE;
    int best_col = 0;

    int[][] temp_board = copyBoard();

  
    for (int i = 0; i < COL; i++) {
        // Make a move for the current player
        // Skip full columns    

        if (button_count[i] >= ROW) continue;
        if(temp_board[get_curr_row(i)][i] != EMPTY) continue;
        temp_board[get_curr_row(i)][i] = userType;
        


    int score = minimax(5, false, temp_board); 
   // int score = alphabetapruning(5, Integer.MIN_VALUE, Integer.MAX_VALUE, false, temp_board);
        // Undo the move
        temp_board[get_curr_row(i)][i] = EMPTY;
     
        if (score > best_score) {
            best_score = score;
            best_col = i;
        }
        
    

     if(totalMoves ==0 || totalMoves == 1) best_col =3; // First move of the game should be in the center
    
 
    curr_col = best_col;
}

    fill_board(get_curr_row(curr_col), curr_col);
    check_game_result(); // Check if the game has ended
    button_count[curr_col]++;
    increase_move();
    switchTurn(); // It's now the opponent's turn in real gameplay

    // GUI operations to reflect the move
    printMovementTable(); // Optional: For debugging/visual purposes
    gameBoardPanel.repaint(); // Update the game board GUI
    

}


private int minimax(int depth, boolean isMaximizing, int[][] temp_board) {

    if (depth == 0 || is_end() || is_draw()) {
        return evaluateBoard(temp_board);
    }


    if (isMaximizing) {
        
        int best_score = Integer.MIN_VALUE;

        for (int i = 0; i < COL; i++) {
            // Make a move for the current player
            // Skip full columns
            if(!is_temp_valid(temp_board,i)) continue; // Skip full columns
            if(temp_board[get_curr_row(i)][i] != EMPTY) continue; 
            temp_board[get_curr_row(i)][i] = userType;

            // Recursively call minimax with the opposite turn
            int score = minimax(depth - 1, false, temp_board);
            // Undo the move
            temp_board[get_curr_row(i)][i] = EMPTY;
         
            best_score = Math.max(score, best_score);
        }
        return best_score;
    } else {
        int best_score = Integer.MAX_VALUE;
        for (int i = 0; i < COL; i++) {
            // Make a move for the current player
            // Skip full columns
           if(!is_temp_valid(temp_board,i)) continue;  // Skip full columns
            if(temp_board[get_curr_row(i)][i] != EMPTY) continue;
            temp_board[get_curr_row(i)][i] = getOpponent();
           
            // Recursively call minimax with the opposite turn
            int score = minimax(depth - 1, true, temp_board);
            // Undo the move
            temp_board[get_curr_row(i)][i] = EMPTY;
       
            best_score = Math.min(score, best_score);
        }
        return best_score;
    }

}

//alfa beta pruning

private int alphabetapruning(int depth, int alpha, int beta, boolean isMaximizing, int[][] temp_board) {

    //minimax for evaluateBoard function to best  movement
    if (depth == 0 || is_end() || is_draw()) {
        return evaluateBoard(temp_board);
    }


    // Determine which player's turn it is

    if (isMaximizing) {
        int best_score = Integer.MIN_VALUE;

        for (int i = 0; i < COL; i++) {
            // Make a move for the current player
            // Skip full columns
            if(!is_temp_valid(temp_board,i)) continue;
            if(temp_board[get_curr_row(i)][i] != EMPTY) continue;
            temp_board[get_curr_row(i)][i] = userType;
         
            // Recursively call minimax with the opposite turn
            int score = alphabetapruning(depth - 1, alpha, beta, false, temp_board);
            // Undo the move
            temp_board[get_curr_row(i)][i] = EMPTY;
            alpha = Math.max(alpha, score);
            if (beta <= alpha) { // beta cut-off
                break;
            }
            best_score = Math.max(score, best_score);
        }
        return best_score;
    } else {
        int best_score = Integer.MAX_VALUE;
        for (int i = 0; i < COL; i++) {
            // Make a move for the current player
            // Skip full columns
           if(!is_temp_valid(temp_board,i)) continue;
            if(temp_board[get_curr_row(i)][i] != EMPTY) continue;
            temp_board[get_curr_row(i)][i] = getOpponent();
           
            // Recursively call minimax with the opposite turn
            int score = alphabetapruning(depth - 1, alpha, beta, true, temp_board);
            // Undo the move
            temp_board[get_curr_row(i)][i] = EMPTY;
            beta = Math.min(beta, score); // alfa cut-off
            if (beta <= alpha) {
                break;
            }
            best_score = Math.min(score, best_score);
        }
        return best_score;
    }      
}
private int evaluateBoard(int[][] curr_board) {


    int score = 0;
    // Score center column
    int center_col = curr_board[0][3];
    int center_count = 0;
    for (int i = 0; i < ROW; i++) {
        if (curr_board[i][3] == center_col) {
            center_count++;
        }
    }
    score += center_count * 3;

    // Score Horizontal
    for (int i = 0; i < ROW; i++) {
        for (int j = 0; j < COL - 3; j++) {
            int[] window = {curr_board[i][j], curr_board[i][j + 1], curr_board[i][j + 2], curr_board[i][j + 3]};
            score += evaluateLine(window);
        }
    }

    // Score Vertical
    for (int i = 0; i < ROW - 3; i++) {
        for (int j = 0; j < COL; j++) {
            int[] window = {curr_board[i][j], curr_board[i + 1][j], curr_board[i + 2][j], curr_board[i + 3][j]};
            score += evaluateLine(window);
        }
    }

    // Score Diagonal
    for (int i = 0; i < ROW - 3; i++) {
        for (int j = 0; j < COL - 3; j++) {
            int[] window = {curr_board[i][j], curr_board[i + 1][j + 1], curr_board[i + 2][j + 2], curr_board[i + 3][j + 3]};
            score += evaluateLine(window);
        }
    }

    for (int i = 0; i < ROW - 3; i++) {
        for (int j = 3; j < COL; j++) {
            int[] window = {curr_board[i][j], curr_board[i + 1][j - 1], curr_board[i + 2][j - 2], curr_board[i + 3][j - 3]};
            score += evaluateLine(window);
        }
    }

    return score;

}
private int evaluateLine(int[] window) {

    int score = 0;
    int user_points = 0;
    int opponent_points = 0;
    for (int i = 0; i < 4; i++) {
        if (window[i] == userType) {
            user_points++;
        } else if (window[i] == getOpponent()) {
            opponent_points++;
        }
    }

    if (user_points == 4) {
        score += 1000;
    } else if (user_points == 3 && opponent_points == 0) {
        score += 5;
    } else if (user_points == 2 && opponent_points == 0) {
        score += 2;
    }

    if (opponent_points == 3 && user_points == 0) {
        score -= 4;
    }

    if(opponent_points == 4){
        score -= 100;
    }

    return score;
}


private int random_move(){

    int col = (int)(Math.random() * 7);
    while(button_count[col] >= ROW){
        col = (int)(Math.random() * 7);
    }
    return col;
}

  
}

// Path: src/GameBoardPanel.java