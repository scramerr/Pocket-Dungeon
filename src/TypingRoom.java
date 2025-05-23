import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class TypingRoom extends JPanel {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private final String[] words = {
            "goblin", "dungeon", "sword", "quest", "magic",
            "castle", "dragon", "shield", "rune", "elixir"
    };

    private JLabel wordLabel;
    private JTextField inputField;
    private JLabel popupLabel;

    private Random random;
    private String currentWord;

    public TypingRoom() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(40, 40, 60));
        setLayout(new BorderLayout());

        random = new Random();

        // Popup Label for PASS/FAIL at the top
        popupLabel = new JLabel("", SwingConstants.CENTER);
        popupLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        popupLabel.setOpaque(true);
        popupLabel.setBackground(Color.BLACK);
        popupLabel.setForeground(Color.GREEN);
        popupLabel.setVisible(false);
        add(popupLabel, BorderLayout.NORTH);

        // Center Panel to display word
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(40, 40, 60));
        centerPanel.setLayout(new GridBagLayout());
        wordLabel = new JLabel(getRandomWord());
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        wordLabel.setForeground(Color.WHITE);
        centerPanel.add(wordLabel);
        add(centerPanel, BorderLayout.CENTER);

        // Input field at the bottom
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 28));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBackground(new Color(60, 60, 80));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputField.addActionListener(e -> checkTyping());
        add(inputField, BorderLayout.SOUTH);
    }

    private String getRandomWord() {
        currentWord = words[random.nextInt(words.length)];
        return currentWord;
    }

    private void checkTyping() {
        String typed = inputField.getText().trim();
        inputField.setText("");

        if (typed.equalsIgnoreCase(currentWord)) {
            showPopup("✅ PASS", Color.GREEN);
            wordLabel.setText(getRandomWord());
        } else {
            showPopup("❌ FAIL", Color.RED);
            // Disable input and end game
            inputField.setEnabled(false);
            Timer endTimer = new Timer(1000, e -> {
                JOptionPane.showMessageDialog(this, "Game Over!\nYou failed to type correctly.", "Typing Room", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose(); // Close the window
            });
            endTimer.setRepeats(false);
            endTimer.start();
        }
    }

    private void showPopup(String message, Color color) {
        popupLabel.setText(message);
        popupLabel.setForeground(color);
        popupLabel.setVisible(true);

        // Hide popup after 1 second
        Timer timer = new Timer(1000, e -> popupLabel.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Typing Room Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new TypingRoom());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
