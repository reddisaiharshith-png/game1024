import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Game1024 extends JPanel {
    private static final int TILE_SIZE = 100;
    private static final int MARGIN = 16;
    private final Board board = new Board();

    public Game1024() {
        setPreferredSize(new Dimension(4 * TILE_SIZE + 5 * MARGIN, 4 * TILE_SIZE + 5 * MARGIN + 60));
        setBackground(new Color(0x0A0F1D));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> board.moveLeft();
                    case KeyEvent.VK_RIGHT -> board.moveRight();
                    case KeyEvent.VK_UP -> board.moveUp();
                    case KeyEvent.VK_DOWN -> board.moveDown();
                    case KeyEvent.VK_R -> board.reset();
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.drawString("1024", MARGIN, 40);
        g2.drawString("Score: " + board.getScore(), 200, 40);

        int[][] grid = board.getGrid();
        int yOffset = 60;

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                int x = MARGIN + c * (TILE_SIZE + MARGIN);
                int y = yOffset + MARGIN + r * (TILE_SIZE + MARGIN);
                drawTile(g2, grid[r][c], x, y);
            }
        }

        if (board.isGameOver()) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(0x776E65));
            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.drawString("Game Over!", getWidth() / 2 - 100, getHeight() / 2);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.drawString("Press 'R' to Restart", getWidth() / 2 - 80, getHeight() / 2 + 40);
        }
    }

   private void drawTile(Graphics2D g2, int val, int x, int y) {
        g2.setColor(getTileColor(val));
        g2.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 14, 14);

        if (val > 0) {
    g2.setColor(val >= 1024 ? Color.BLACK : Color.WHITE);
            
            // Dynamically scale font size as numbers grow larger
            int fontSize = 36;
            if (val >= 10000) {
                fontSize = 22;
            } else if (val >= 1000) {
                fontSize = 26;
            } else if (val >= 100) {
                fontSize = 32;
            }
            
            g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
            String text = String.valueOf(val);
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (TILE_SIZE - fm.stringWidth(text)) / 2;
            int ty = y + ((TILE_SIZE - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(text, tx, ty);
        }
    }

    private Color getTileColor(int val) {
    return switch (val) {
        case 0     -> new Color(0x131B2E); // Dark empty slot (near black-blue)
        case 1     -> new Color(0x1E3A8A); // Deep navy blue
        case 2     -> new Color(0x1D4ED8); // Royal blue
        case 4     -> new Color(0x2563EB); // Cobalt blue
        case 8     -> new Color(0x3B82F6); // Vibrant blue
        case 16    -> new Color(0x60A5FA); // Sky blue
        case 32    -> new Color(0x93C5FD); // Light pastel blue
        case 64    -> new Color(0x38BDF8); // Electric cyan blue
        case 128   -> new Color(0x0EA5E9); // Deep ocean cyan
        case 256   -> new Color(0x0284C7); // Sapphire
        case 512   -> new Color(0x0369A1); // Deep steel blue
        case 1024  -> new Color(0xE0F2FE); // Ice white-blue
        case 2048  -> new Color(0xFFFFFF); // Pure white highlight tile
        default    -> new Color(0x0F172A); // Midnight fallback
    };
}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("1024 Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new Game1024());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}