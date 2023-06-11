import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class DraggableLabel extends JLabel implements Selectable
{
    private final DraggableLabel handle;

    protected Point anchor;
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    protected EditorGUI panel;
    protected boolean selectable = false;
    protected boolean draggable = true;

    public DraggableLabel(int x, int y, int fontSize, String text, EditorGUI panel, boolean centered)
    {
        super(text, JLabel.CENTER);
        this.panel = panel;
        handle = this;

        setFont(new Font("Arial", Font.PLAIN, fontSize));

        int width = getFontMetrics(getFont()).stringWidth(text);
        int height = getFontMetrics(getFont()).getHeight();

        setBounds(centered ? x - width / 2 : x,
                centered ? y - height / 2 : y,
                width, height);
        panel.add(this);
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

    public void setDraggable(boolean d)
    {
        if (draggable != d)
        {
            if (d)
                addDragListeners();
            else
            {
                setBorder(null);
                removeDragListeners();
            }
        }

        draggable = d;
    }

    public void addClickListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.unselectObjects();
                panel.setComponentZOrder(handle, 0);
                panel.selectedObjects.add(handle);
                panel.menuBar.editLabelAction.setEnabled(true);

                setBorder(new LineBorder(Color.BLUE));
                setDraggable(true);
                setCursor(draggingCursor);

                anchor = e.getPoint();
            }
        });
    }

    public void removeClickListeners()
    {
        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);
    }

    public void addDragListeners() {
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
                Point position = new Point(mouse.x - parent.x - anchor.x,
                        mouse.y - parent.y - anchor.y);
                setLocation(position);
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteObject");
        getActionMap().put("deleteObject", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Selectable object : panel.chartObjects)
                    object.setSelectable(true);

                handle.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");
                panel.chartObjects.remove(handle);
                panel.selectedObjects.remove(handle);
                handle.deleteObject();
                panel.repaint();
            }
        });
    }

    public void removeDragListeners() {
        for (MouseMotionListener listener : getMouseMotionListeners())
            removeMouseMotionListener(listener);

        setCursor(Cursor.getDefaultCursor());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");
    }

    public boolean intersects(Rectangle rect)
    {
        return new Rectangle(getX(), getY(), getWidth(), getHeight()).intersects(rect);
    }

    public void deleteObject()
    {
        panel.remove(this);
    }
}
