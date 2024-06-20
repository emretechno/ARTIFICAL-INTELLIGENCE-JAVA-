import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameBoardPanel extends JPanel {

    private static final int ROW = 6;
    private static final int COL = 7;
    private static final int CELL_SIZE = 83; // Size of each cell
    private static final int GAP_SIZE = 35; // Size of the gap between cells
    private int[][] board;

    public GameBoardPanel(int[][] board) {
        this.board = board;

        // Add a component listener to the panel to update its size when the frame is resized
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                repaint(); // Repaint the panel when its size changes
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        // Return the preferred size based on the size of the parent container (frame)
        return getParent().getSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate the dimensions to center the board
        int boardWidth = COL * (CELL_SIZE + GAP_SIZE) - GAP_SIZE; // Adjust the width as needed
        int boardHeight = ROW * (CELL_SIZE + GAP_SIZE) - GAP_SIZE; // Adjust the height as needed
        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        // Draw the black background of the board
        g.setColor(Color.BLACK);
        g.fillRect(xOffset, yOffset, boardWidth, boardHeight);

        // Draw the circles representing the game board
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                int x = xOffset + col * (CELL_SIZE + GAP_SIZE); // Adjust the spacing as needed
                int y = yOffset + row * (CELL_SIZE + GAP_SIZE); // Adjust the spacing as needed
                if (board[row][col] == 1) {
                    g.setColor(Color.RED); // Set the color for Player 1
                } else if (board[row][col] == 2) {
                    g.setColor(Color.YELLOW); // Set the color for Player 2
                } else {
                    g.setColor(Color.WHITE); // Set the default color for empty cells
                }
                g.fillOval(x, y, CELL_SIZE, CELL_SIZE); // Draw the circle
            }
        }

      
    }

    public void repaintBoard() {
        repaint();
    }

    
    
}
