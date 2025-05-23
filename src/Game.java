import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JFrame {

    private static final int TILE_SIZE = 40;
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static final int MAP_WIDTH = SCREEN_WIDTH / TILE_SIZE;
    private static final int MAP_HEIGHT = (SCREEN_HEIGHT - 70) / TILE_SIZE; // 70 for message label

    private char[][] map;
    private int playerX, playerY;

    private int glowX = 10, glowY = 8;// Glowing tile deeper into the map

    private int typingX = 15, typingY = 6; // New glowing tile for typing room

    private JPanel gamePanel;
    private JLabel messageLabel;

    public Game() {
        setTitle("Dungeon Awakening");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);


        initializeMap();

        messageLabel = new JLabel("💀 Step into the glowing tile to find what lies ahead...", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(Color.DARK_GRAY);

        gamePanel = mainGamePanel();
        add(gamePanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        addKeyListener(new MovementListener());
        setFocusable(true);
        setVisible(true);
    }

    private JPanel mainGamePanel() {
        var gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawScene(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE));
        gamePanel.setBackground(Color.BLACK);
        return gamePanel;
    }

    private JPanel reactionGamePanel() {
        return new ReactionRoom();
    }

    private JPanel typingRoomPanel(){
        return new TypingRoom();
    }

    private void initializeMap() {
        map = new char[MAP_HEIGHT][MAP_WIDTH];

        // Fill with open floor
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = '.';
            }
        }

        // Player spawns at top-left
        playerX = 0;
        playerY = 0;
    }
    private void drawScene(Graphics g) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;

                if ((x == glowX && y == glowY) || (x == typingX && y == typingY)) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(new Color(50, 50, 50));
                }

                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            }
        }

        // Arrows for both rooms
        g.setFont(new Font("SansSerif", Font.PLAIN, TILE_SIZE));
        g.setColor(Color.YELLOW);
        g.drawString("⬇️", glowX * TILE_SIZE + 5, (glowY - 1) * TILE_SIZE + 35);
        g.drawString("⬇️", typingX * TILE_SIZE + 5, (typingY - 1) * TILE_SIZE + 35);

        // Player (💀)
        g.setFont(new Font("Serif", Font.PLAIN, TILE_SIZE));
        g.setColor(Color.RED);
        g.drawString("💀", playerX * TILE_SIZE + 4, playerY * TILE_SIZE + TILE_SIZE - 4);
    }


    private void movePlayer(int newX, int newY) {
        if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT) {
            playerX = newX;
            playerY = newY;
            gamePanel.repaint();

            if (playerX == glowX && playerY == glowY) {
                messageLabel.setText("✨ You feel a strange energy surround you...");
                remove(gamePanel);
                gamePanel = reactionGamePanel();
                add(gamePanel, BorderLayout.CENTER);
            }
            else if(playerX == typingX && playerY == typingY){
                messageLabel.setText("✨ You feel a strange energy surround you...");
                remove(gamePanel);
                gamePanel = typingRoomPanel();
                add(gamePanel, BorderLayout.CENTER);
            }
            else {
                messageLabel.setText("💀 Step into any of the glowing tiles to find what lies ahead...");
            }
        }
    }

    private class MovementListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int newX = playerX;
            int newY = playerY;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: newY--; break;
                case KeyEvent.VK_DOWN: newY++; break;
                case KeyEvent.VK_LEFT: newX--; break;
                case KeyEvent.VK_RIGHT: newX++; break;
            }
            movePlayer(newX, newY);
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
