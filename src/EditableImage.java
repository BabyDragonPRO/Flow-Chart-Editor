import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class EditableImage extends DraggableComponent implements ImageObserver
{
    private final EditableImage handle;

    protected Image image;
    protected Color color;
    protected String path;
    protected EditorGUI panel;
    protected ResizeMesh mesh;
    protected boolean selectable = false;

    public EditableImage(int x, int y, int width, int height, EditorGUI panel, String path, Color color)
    {
        super(x, y, width, height);
        handle = this;
        setDraggable(false);

        try {
            image = ImageIO.read(new File(path));
            this.path = path;

            if (color != null)
            {
                this.color = color;
                dye(color);
            }
        }

        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        this.panel = panel;

        panel.add(this);
    }

    public String getPath()
    {
        return path;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        if (color == null)
            dye(Color.WHITE);

        else
            dye(color);

        this.color = color;
    }

    public void dye(Color color)
    {
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        BufferedImage dyed = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, this);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, w, h);
        g.dispose();
        image = dyed;
    }

    public void setSelectable(boolean s)
    {
        if (selectable != s)
        {
            if (s)
                addClickListeners();

            else
                removeClickListeners();
        }

        selectable = s;
    }

    protected void addClickListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.unselectObjects();
                mesh = new ResizeMesh(panel, handle);
                panel.setComponentZOrder(handle, panel.getComponentCount() - 1);
                panel.selectedObjects.add(handle);
                panel.menuBar.changeColorAction.setEnabled(true);
                panel.menuBar.transformAction.setEnabled(true);

                setBorder(new LineBorder(Color.BLUE));
                setDraggable(true);
                setCursor(draggingCursor);

                anchor = e.getPoint();
            }
        });
    }

    protected void removeClickListeners()
    {
        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);
    }

    @Override
    protected void addDragListeners() {
        super.addDragListeners();

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteObject");
        getActionMap().put("deleteObject", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (EditableImage object : panel.chartObjects)
                    object.setSelectable(true);

                handle.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");
                mesh.deleteMesh();
                panel.remove(handle);
                panel.repaint();
            }
        });
    }

    @Override
    protected void removeDragListeners() {
        super.removeDragListeners();

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");
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
