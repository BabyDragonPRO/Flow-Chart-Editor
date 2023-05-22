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
    protected boolean overbearing = false;
    protected boolean xLocked = false;
    protected boolean yLocked = false;

    public DraggableComponent()
    {
        addDragListeners();
        setOpaque(false);
    }

    public DraggableComponent(Color color)
    {
        addDragListeners();
        setOpaque(true);
        setBackground(color);
    }

    private void addDragListeners()
    {
        final DraggableComponent handle = this;
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

                if (overbearing)
                {
                    getParent().setComponentZOrder(handle, 0);
                    repaint();
                }
            }
        });
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

    private void removeDragListeners()
    {
        for (MouseMotionListener listener : this.getMouseMotionListeners())
            removeMouseMotionListener(listener);

        setCursor(Cursor.getDefaultCursor());
    }
}
