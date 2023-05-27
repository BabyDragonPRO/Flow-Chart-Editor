import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class EditableImage extends DraggableComponent implements ImageObserver
{
    protected Image image;
    protected EditorGUI panel;
    protected ResizeMesh mesh;
    protected boolean selectable = false;

    public EditableImage(int x, int y, int width, int height, EditorGUI panel, String path)
    {
        super(x, y, width, height);
        setDraggable(false);

        try {
            image = ImageIO.read(new File(path));
        }

        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        this.panel = panel;

        panel.add(this);
    }

    public void setSelectable(boolean s)
    {
        if (selectable != s)
        {
            if (s)
            {
                addClickListeners();
            }
            else
            {
                removeClickListeners();
            }
        }

        selectable = s;
    }

    protected void addClickListeners()
    {
        final DraggableComponent handle = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (EditableImage object : panel.chartObjects)
                    object.setSelectable(false);

                setDraggable(true);
                setCursor(draggingCursor);

                anchor = e.getPoint();
                mesh = new ResizeMesh(panel, handle);

                panel.setComponentZOrder(handle, panel.getComponentCount() - 1);
            }
        });
    }

    protected void removeClickListeners()
    {
        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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
