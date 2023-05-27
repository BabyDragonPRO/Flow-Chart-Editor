import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ImageButton extends JComponent
{
    protected Image image;
    protected Image imagePressed;
    protected boolean pressed;

    public ImageButton(int x, int y, int width, int height, String path, String pathPressed)
    {
        try {
            image = ImageIO.read(new File(path));
            imagePressed = ImageIO.read(new File(pathPressed));
        }

        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        setBounds(x, y, width, height);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (image != null)
            g2d.drawImage(pressed ? imagePressed : image, 0, 0, getWidth(), getHeight(), this);

        else
        {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
