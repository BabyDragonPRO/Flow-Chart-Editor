import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class ArrowComponent extends JComponent implements Selectable
{
    protected final ArrayList<Point> points;
    private final ArrowComponent handle;

    protected EditorGUI panel;
    protected Point anchor;
    protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    protected boolean selectable = false;
    protected boolean draggable = true;
    protected int xOffset;
    protected int yOffset;

    public ArrowComponent(EditorGUI parent, ArrayList<Point> points)
    {
        this.points = points;
        handle = this;
        panel = parent;

        int x1 = points.get(0).x;
        int y1 = points.get(0).y;
        int x2 = points.get(points.size() - 1).x;
        int y2 = points.get(points.size() - 1).y;

        for (Point p : points)
        {
            x1 = Math.min(x1, p.x);
            y1 = Math.min(y1, p.y);
            x2 = Math.max(x2, p.x);
            y2 = Math.max(y2, p.y);
        }
        setBounds(x1, y1, x2 - x1, y2 - y1);
        parent.add(this);
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

    public void deleteObject()
    {
        panel.remove(this);
    }

    public void addClickListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.unselectObjects();
                panel.setComponentZOrder(handle, 0);
                panel.selectedObjects.add(handle);

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
                for (Point point : points)
                {
                    point.x += position.x - getX();
                    point.y += position.y - getY();
                }
                setLocation(position);
                panel.repaint();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteObject");
        getActionMap().put("deleteObject", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Selectable object : panel.chartObjects)
                    object.setSelectable(true);

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
        for (int i = 1; i < points.size(); i++)
        {
            Line2D line = new Line2D.Double(points.get(i - 1), points.get(i));
            if (line.intersects(rect))
                return true;
        }

        return false;
    }

    private void drawArrowhead(Graphics2D g, int d, int h) {
        int x1 = points.get(points.size() - 2).x;
        int y1 = points.get(points.size() - 2).y;
        int x2 = points.get(points.size() - 1).x;
        int y2 = points.get(points.size() - 1).y;
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xPoints = {x2, (int) xm, (int) xn};
        int[] yPoints = {y2, (int) ym, (int) yn};

        int x3 = getX();
        int y3 = getY();
        int width = getWidth();
        int height = getHeight();

        for (int newW : xPoints)
            width = Math.max(newW - getX(), width);
        for (int newH : yPoints)
            height = Math.max(newH - getY(), height);
        for (int newX : xPoints)
            x3 = Math.min(newX + getX(), x3);
        for (int newY : yPoints)
            y3 = Math.min(newY + getY(), y3);

        xOffset = getX() > x3 ? getX() - x3 : 0;
        yOffset = getY() > y3 ? getY() - y3 : 0;
        setBounds(x3, y3, width, height);

        g.fillPolygon(xPoints, yPoints, 3);
    }

    protected void paintArrow(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));

        drawArrowhead(g2d, 20, 10);

        for (int i = 1; i < points.size(); i++)
            g2d.drawLine(points.get(i - 1).x + xOffset, points.get(i - 1).y + yOffset,
                    points.get(i).x + xOffset, points.get(i).y + yOffset);
    }
}
