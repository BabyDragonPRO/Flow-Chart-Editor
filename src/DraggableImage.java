import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class DraggableImage extends DraggableComponent implements ImageObserver
{
    protected Image image;

    public DraggableImage(String path)
    {
        super();
        setLayout(null);

        try {
            image = ImageIO.read(new File(path));
        }

        catch (IOException e)
        {
            System.out.println(e.toString());
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        if (image != null)
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        else
        {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public boolean imageUpdate(Image image, int infoFlags, int x, int y, int w, int h)
    {
        if (infoFlags == ALLBITS)
        {
            repaint();
            return false;
        }

        return true;
    }
}
