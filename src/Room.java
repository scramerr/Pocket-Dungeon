import javax.swing.*;
import java.awt.*;

public class Room extends JPanel {
    public int px;
    public int py;
    private JPanel panel;

    public Room(int x, int y, JPanel panel) {
        px = x * Game.TILE_SIZE;
        py = y * Game.TILE_SIZE;
        this.panel = panel;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(px, py, Game.TILE_SIZE, Game.TILE_SIZE);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(px, py, Game.TILE_SIZE, Game.TILE_SIZE);
    }

    public void start(JFrame container, JPanel current) {
        container.remove(current);
        container.add(panel, BorderLayout.CENTER);
    }
}
