import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

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
        setLayout(null); // We'll manually place all components
        setBackground(new Color(40, 40, 60));

        random = new Random();

        // Center word label
        wordLabel = new JLabel(getRandomWord(), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setBounds(WIDTH / 2 - 200, HEIGHT / 2 - 100, 400, 60);
        add(wordLabel);

        // Input field
        inputField = new JTextField();
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 28));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBackground(new Color(60, 60, 80));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputField.setBounds(WIDTH / 2 - 300, HEIGHT - 100, 600, 50);
        inputField.addActionListener(e -> checkTyping());
        add(inputField);
        inputField.setFocusable(true);
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());


        // Overlay popup label
        popupLabel = new JLabel("", SwingConstants.CENTER);
        popupLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        popupLabel.setOpaque(true);
        popupLabel.setBackground(Color.BLACK);
        popupLabel.setForeground(Color.GREEN);
        popupLabel.setVisible(false);
        popupLabel.setBounds(WIDTH / 2 - 150, 50, 300, 60);
        add(popupLabel);


        focusInputField();
    }

    private String getRandomWord() {
        currentWord = words[random.nextInt(words.length)];
        return currentWord;
    }

    @Override
    public boolean requestFocusInWindow() {
        return inputField.requestFocusInWindow();
    }

    private void checkTyping() {
        String typed = inputField.getText().trim();
        inputField.setText("");

        if (typed.equalsIgnoreCase(currentWord)) {
            showPopup("✅ PASS", Color.GREEN);
            wordLabel.setText(getRandomWord());
        } else {
            showPopup("❌ FAIL", Color.RED);
            inputField.setEnabled(false);

            new Timer(1000, e -> {
                JOptionPane.showMessageDialog(this, "Game Over!\nYou failed to type correctly.", "Typing Room", JOptionPane.ERROR_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose();
            }).start();
        }
    }

    private void showPopup(String message, Color color) {
        popupLabel.setText(message);
        popupLabel.setForeground(color);
        popupLabel.setVisible(true);

        new Timer(1000, e -> popupLabel.setVisible(false)).start();
    }

    public void focusInputField() {
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Typing Room Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new TypingRoom());
            frame.setSize(WIDTH, HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
