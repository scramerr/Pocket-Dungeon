import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JFrame {

    public static final int TILE_SIZE = 40;
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static final int MAP_WIDTH = SCREEN_WIDTH / TILE_SIZE;
    private static final int MAP_HEIGHT = (SCREEN_HEIGHT - 70) / TILE_SIZE;

    private int playerX, playerY;
    private boolean inRoom;

    private char[][] map;
    private JPanel gamePanel;
    private JLabel messageLabel;

    private ArrayList<Room> rooms;

    public Game() {
        setTitle("Dungeon Awakening");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        inRoom = false;
        rooms = new ArrayList<>();

        initializeMap();

        messageLabel = new JLabel("💀 Use arrow keys to move. Enter glowing tiles to explore levels.", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(Color.DARK_GRAY);

        gamePanel = createMainGamePanel();
        add(gamePanel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        addKeyListener(new MovementListener());
        setFocusable(true);
        setVisible(true);
    }

    private void initializeMap() {
        map = new char[MAP_HEIGHT][MAP_WIDTH];

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                map[y][x] = '.';
            }
        }

        playerX = 2;
        playerY = MAP_HEIGHT - 2;

        int startX = 6;
        int spacing = 6;

        Callback cb = new Callback() {
            @Override
            public void onSuccess() {
                initializeMap();
            }
        };

        rooms.clear();
        rooms.add(new Room(startX, MAP_HEIGHT - 2, new TypingRoom(), cb));
        rooms.add(new Room(startX + spacing, MAP_HEIGHT - 2, new ReactionRoom(), cb));
    }

    private JPanel createMainGamePanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawScene(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
            }
        };
    }

    private void drawScene(Graphics g) {
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                int px = x * TILE_SIZE;
                int py = y * TILE_SIZE;

                g.setColor(new Color(50, 50, 50));
                g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(px, py, TILE_SIZE, TILE_SIZE);
            }
        }

        for (Room room : rooms) {
            room.draw(g);
        }

        g.setFont(new Font("Serif", Font.PLAIN, TILE_SIZE));
        g.setColor(Color.RED);
        g.drawString("💀", playerX * TILE_SIZE + 4, playerY * TILE_SIZE + TILE_SIZE - 4);
    }

    private void movePlayer(int newX, int newY) {
        if (inRoom) return;

        if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT) {
            playerX = newX;
            playerY = newY;
            gamePanel.repaint();

            messageLabel.setText("💀 Use arrow keys to move. Enter glowing tiles to explore levels.");

            for (Room room : rooms) {
                if (playerX * TILE_SIZE == room.px && playerY * TILE_SIZE == room.py) {
                    messageLabel.setText("✨ Entering a mysterious room...");
                    room.start(this, gamePanel);
                    inRoom = true;
                }
            }
        }
    }

    private class MovementListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int newX = playerX, newY = playerY;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> newY--;
                case KeyEvent.VK_DOWN -> newY++;
                case KeyEvent.VK_LEFT -> newX--;
                case KeyEvent.VK_RIGHT -> newX++;
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
