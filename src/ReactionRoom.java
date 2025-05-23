import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// ReactionRoom is now a JPanel, designed to be embedded within a JFrame
public class ReactionRoom extends JPanel {

    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 400;
    private static final int GOBLIN_SIZE = 60; // Size of the goblin emoji label
    private static final int GOBLIN_APPEAR_DURATION_MS = 800; // How long the goblin stays visible

    private JLabel goblinLabel;
    private JButton startButton;
    private JLabel messageLabel;
    private JPanel gamePanel;
    private JPanel controlPanel;

    private long startTime;
    private Boolean failed;
    private Timer goblinTimer; // Timer to control goblin visibility
    private Random random;

    public ReactionRoom() {
        failed = false;
        // Set the preferred size for this JPanel
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        // Use BorderLayout for the ReactionRoom itself to arrange its sub-panels
        setLayout(new BorderLayout()); // <--- CRITICAL CHANGE: Set layout for ReactionRoom itself

        random = new Random();

        // Game area where goblin appears
        gamePanel = new JPanel(null); // Use null layout for absolute positioning
        gamePanel.setBackground(new Color(50, 50, 70)); // Dark background
        add(gamePanel, BorderLayout.CENTER); // Add gamePanel to the center of ReactionRoom

        // Goblin label (initially hidden)
        goblinLabel = new JLabel("👹"); // Goblin emoji
        goblinLabel.setFont(new Font("Noto Color Emoji", Font.PLAIN, GOBLIN_SIZE));
        goblinLabel.setSize(GOBLIN_SIZE, GOBLIN_SIZE);
        goblinLabel.setVisible(false); // Hide until game starts
        goblinLabel.setOpaque(true); // Make sure background is transparent
        gamePanel.add(goblinLabel);

        setFocusable(true); // <--- CRITICAL: Make the panel focusable
        // Request focus when the panel is shown in a window.
        // This is important because JPanels don't automatically get focus.
        addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                requestFocusInWindow(); // Request focus when component is added to a window
            }
            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });


        // Add mouse listener to the goblin label
        addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                System.out.println("press key" +  e.getKeyCode());
                if (goblinLabel.isVisible() && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("goblin is vislbe");
                    long reactionTime = System.nanoTime() - startTime;
                    goblinTimer.stop(); // Stop the timer if clicked
                    goblinLabel.setVisible(false);
                    displayResult(reactionTime);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("goblin is not vislbe");
                    goblinTimer.stop(); // Stop the timer if clicked
                    goblinLabel.setVisible(false);
                    gamePanel.remove(goblinLabel);
                    failed = true;
                    displayFailure();
                }
            }
        });

        // Control panel for buttons and messages
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(30, 30, 40));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(controlPanel, BorderLayout.SOUTH); // Add controlPanel to the south of ReactionRoom

        // Start Button
        startButton = new JButton("Start Test");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(70, 150, 70)); // Greenish button
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false); // Remove focus border
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        controlPanel.add(startButton, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(30, 30, 40));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(messagePanel, BorderLayout.NORTH); // Add controlPanel to the south of ReactionRoom

        // Message Label
        messageLabel = new JLabel("Click 'Start Test' to begin!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        messageLabel.setForeground(Color.LIGHT_GRAY);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
//        controlPanel.add(messageLabel, BorderLayout.CENTER);

        // Initialize the goblin timer
        goblinTimer = new Timer(GOBLIN_APPEAR_DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If timer fires, goblin was not clicked in time
                if (!goblinLabel.isVisible()) {
                    return;
                }
                System.out.println("cooked");

                goblinLabel.setVisible(false);
                messageLabel.setText("Too slow! You missed the goblin.");
                goblinTimer.stop(); // Stop the timer if clicked
                goblinLabel.setVisible(false);
                gamePanel.remove(goblinLabel);
                failed = true;
                displayFailure();
            }
        });
        goblinTimer.setRepeats(false); // Only fire once
    }

    /**
     * Starts the reaction test game.
     */
    private void startGame() {
        // Only remove the button if it's currently present
        if (controlPanel.getComponentCount() > 0 && controlPanel.getComponent(0) == startButton) { // Added check for component count
            controlPanel.remove(startButton);
            controlPanel.revalidate(); // Re-layout the panel after removing a component
            controlPanel.repaint(); // Redraw the panel
        }
        startButton.setEnabled(false); // Disable start button during game
        messageLabel.setText("Get ready...");

        // Random delay before goblin appears to prevent anticipating
        int delay = random.nextInt(2000) + 500; // 0.5 to 2.5 seconds
        Timer initialDelayTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("spawning goblin");
                showGoblin();
                ((Timer) e.getSource()).stop(); // Stop this one-shot timer
            }
        });
        initialDelayTimer.setRepeats(false);
        initialDelayTimer.start();
    }

    /**
     * Shows the goblin at a random position and starts the reaction timer.
     */
    private void showGoblin() {
        if(failed) {
            return;
        }

        // Calculate random position within gamePanel bounds
        int x = random.nextInt(gamePanel.getWidth() - GOBLIN_SIZE);
        int y = random.nextInt(gamePanel.getHeight() - GOBLIN_SIZE);

        goblinLabel.setLocation(x, y);
        goblinLabel.setVisible(true);
        messageLabel.setText("Press Space to shoot the goblino!");

        startTime = System.nanoTime(); // Record start time
        goblinTimer.start(); // Start timer for goblin visibility
        System.out.println("goblin timer started");
    }

    /**
     * Displays the reaction time or a "too slow" message.
     * @param reactionTimeNs The reaction time in nanoseconds.
     */
    private void displayResult(long reactionTimeNs) {
        double reactionTimeMs = reactionTimeNs / 1_000_000.0;
        messageLabel.setText(String.format("Reaction Time: %.2f ms", reactionTimeMs));
        startButton.setEnabled(true); // Enable start button again
        // Re-add start button
        if (controlPanel.getComponentCount() == 0 || controlPanel.getComponent(0) != startButton) { // Added check for component count
            controlPanel.add(startButton, BorderLayout.CENTER);
            controlPanel.revalidate();
            controlPanel.repaint();
        }
    }

    private void displayFailure() {
        messageLabel.setText(String.format("YOU FAILED"));
        startButton.setEnabled(true); // Enable start button again
        // Re-add start button
        if (controlPanel.getComponentCount() == 0 || controlPanel.getComponent(0) != startButton) { // Added check for component count
            controlPanel.add(startButton, BorderLayout.CENTER);
            controlPanel.revalidate();
            controlPanel.repaint();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Goblin Reaction Test"); // Create a JFrame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // Center the frame

            ReactionRoom reactionRoom = new ReactionRoom(); // Create an instance of your JPanel
            frame.add(reactionRoom); // Add the JPanel to the JFrame

            frame.pack(); // Size the frame to fit its contents
            frame.setVisible(true); // Make the frame visible
        });
    }
}
