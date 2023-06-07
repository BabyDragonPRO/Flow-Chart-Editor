import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class EditableImage extends DraggableComponent implements ImageObserver
{
    private final EditableImage handle;

    protected Image image;
    protected Image baseImage;
    protected Color color;
    protected String path;
    protected EditorGUI panel;
    protected ResizeMesh mesh;
    protected boolean selectable = false;
    protected int rotation;

    public EditableImage(int x, int y, int width, int height, EditorGUI panel, String path, int rotation, Color color)
    {
        super(x, y, width, height);
        handle = this;
        setDraggable(false);

        try {
            image = ImageIO.read(new File(path));
            baseImage = image;
            this.path = path;

            if (color != null)
            {
                this.color = color;
                dye(color);
            }

            setRotation(rotation);
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

    public int getRotation()
    {
        return rotation;
    }

    public void setColor(Color color)
    {
        if (color == null)
            dye(Color.WHITE);

        else if (this.color != color)
            dye(color);

        this.color = color;
    }

    public void setRotation(int rotation)
    {
        if (this.rotation != rotation)
            rotate(rotation);

        this.rotation = rotation;
    }

    private void rotate(int rotation)
    {
        image = baseImage;
        dye(color);
        double sin = Math.abs(Math.sin(Math.toRadians(rotation)));
        double cos = Math.abs(Math.cos(Math.toRadians(rotation)));
        int w = image.getWidth(this);
        int h = image.getHeight(this);
        int newW = (int) Math.floor(w * cos + h * sin);
        int newH = (int) Math.floor(h * cos + w * sin);
        BufferedImage result = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.translate((newW - w) / 2, (newH - h) / 2);
        g.rotate(Math.toRadians(rotation), w / 2, h / 2);
        g.drawRenderedImage((RenderedImage) image, null);
        g.dispose();
        image = result;
        setSize((int) (Math.abs(getWidth() * cos) + Math.abs(getHeight() * sin)),
                (int) (Math.abs(getWidth() * sin) + Math.abs(getHeight() * cos)));
    }

    private void dye(Color color)
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
