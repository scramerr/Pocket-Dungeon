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
    private Random random;

    public TypingRoom() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(40, 40, 60));
        setLayout(new BorderLayout());

        random = new Random();

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
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // You can add logic here to check if input matches the word
                String typed = inputField.getText().trim();
                System.out.println("Typed: " + typed);
                inputField.setText("");
                wordLabel.setText(getRandomWord());
            }
        });

        add(inputField, BorderLayout.SOUTH);
    }

    private String getRandomWord() {
        return words[random.nextInt(words.length)];
    }
}
