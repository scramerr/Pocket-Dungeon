import javax.swing.*;
import java.awt.*;

public class Room extends JPanel {
    public int px;
    public int py;
    private JPanel panel;
    private Callback cb;

    public Room(int x, int y, JPanel panel, Callback cb) {
        px = x * Game.TILE_SIZE;
        py = y * Game.TILE_SIZE;
        this.panel = panel;
        this.cb = cb;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(px, py, Game.TILE_SIZE, Game.TILE_SIZE);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(px, py, Game.TILE_SIZE, Game.TILE_SIZE);

        // Arrows for both rooms
        g.setFont(new Font("SansSerif", Font.PLAIN, Game.TILE_SIZE));
        g.setColor(Color.YELLOW);
        g.drawString("⬇️", px + 5, (py - Game.TILE_SIZE) + 35);
    }

    public void start(JFrame container, JPanel current) {
        container.remove(current);
        container.add(panel, BorderLayout.CENTER);
        panel.requestFocusInWindow();
    }
}
