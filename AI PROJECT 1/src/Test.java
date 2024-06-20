import javax.swing.SwingUtilities;

public class Test {




    public static void main(String[] args) {
    
        SwingUtilities.invokeLater(() -> {
            connect4 game = new connect4(); // Instantiate your connect4 class
            game.setVisible(true); // Display the frame
        });


    
}
}
