import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DraggableComponent extends JComponent
{
    protected Point anchor;
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    protected boolean draggable = true;
    protected boolean xLocked = false;
    protected boolean yLocked = false;

    public DraggableComponent(int x, int y, int width, int height)
    {
        setBounds(x, y, width, height);
        addDragListeners();
    }

    public DraggableComponent(int x, int y, int width, int height, Color color)
    {
        this(x, y, width, height);
        setBackground(color);
        setOpaque(true);
    }

    protected void addDragListeners()
    {
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchor = e.getPoint();
                setCursor(draggingCursor);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point parent = getParent().getLocationOnScreen();
                Point mouse = e.getLocationOnScreen();
                Point position = new Point(xLocked ? getX() : mouse.x - parent.x - anchor.x,
                        yLocked ? getY() : mouse.y - parent.y - anchor.y);
                setLocation(position);
            }
        });
    }

    protected void removeDragListeners()
    {
        for (MouseMotionListener listener : getMouseMotionListeners())
            removeMouseMotionListener(listener);

        setCursor(Cursor.getDefaultCursor());
    }

    public void setDraggable(boolean d)
    {
        if (draggable != d)
        {
            if (d)
                addDragListeners();
            else
                removeDragListeners();
        }

        draggable = d;
    }

    public void setXLocked(boolean locked)
    {
        xLocked = locked;
    }

    public void setYLocked(boolean locked)
    {
        yLocked = locked;
    }

    public void setDraggingCursor(Cursor cursor)
    {
        draggingCursor = cursor;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (isOpaque())
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
