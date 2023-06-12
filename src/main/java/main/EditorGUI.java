package main;

import components.ArrowComponent;
import components.EditableImage;
import components.ToolPanel;
import dialogs.CreateLabelDialog;
import util.Selectable;
import util.Tool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditorGUI extends JPanel
{
    public ArrayList<Selectable> chartObjects;
    public ArrayList<Selectable> selectedObjects;
    public ArrayList<Point> buildingArrow;
    public Tool currentTool;

    private final EditorGUI handle;
    private final JFrame frame;
    private final EditorMenuBar menuBar;
    private final ToolPanel toolPanel;
    private final Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    private Point selectionAnchor;
    private Point selectionStart;
    private Point selectionEnd;

    private final AbstractAction clearSelectionAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            unselectObjects();
        }
    };
    private final AbstractAction clearSelectionAreaAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Selectable object : selectedObjects)
            {
                object.removeDragListeners();
                object.setBorder(null);
            }
        }
    };
    private final AbstractAction deleteArrowAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildingArrow.clear();
            repaint();
        }
    };
    private final AbstractAction finishArrowAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            finishArrow();
        }
    };
    private final AbstractAction deleteObjectsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Selectable object : selectedObjects)
                object.deleteObject();

            selectedObjects.clear();
            repaint();
        }
    };

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    JFrame frame = new JFrame();
                    frame.setTitle("Flow-Chart Editor");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setBounds(100, 100, 1200, 800);

                    EditorGUI contentPane = new EditorGUI(frame);

                    frame.setContentPane(contentPane);
                    frame.setResizable(false);
                    frame.setVisible(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public EditorGUI(JFrame frame)
    {
        handle = this;
        this.frame = frame;
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
        setOpaque(true);

        chartObjects = new ArrayList<>();
        selectedObjects = new ArrayList<>();
        buildingArrow = new ArrayList<>();

        toolPanel = new ToolPanel(10, 10, this);

        add(toolPanel);
        selectTool(Tool.SELECTION);

        menuBar = new EditorMenuBar(this, frame);
    }

    public ToolPanel getToolPanel()
    {
        return toolPanel;
    }

    public EditorMenuBar getMenuBar()
    {
        return menuBar;
    }

    public void selectTool(Tool tool)
    {
        if (currentTool != tool)
        {
            removeAllListeners();

            switch (tool)
            {
                case SELECTION -> {
                    addSelectionListeners();
                }

                case SELECT_AREA -> {
                    addSelectAreaListeners();
                }

                case LABEL -> {
                    addCreateLabelListeners();
                }

                case ARROW -> {
                    addCreateArrowListeners();
                }

                default -> {
                    addCreateObjectListener(tool);
                }
            }

            currentTool = tool;
        }
    }

    private void addSelectionListeners()
    {
        for (Selectable object : chartObjects)
            object.setSelectable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                unselectObjects();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "clearSelection");
        getActionMap().put("clearSelection", clearSelectionAction);
    }

    private void addSelectAreaListeners()
    {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "clearSelectionArea");
        getActionMap().put("clearSelectionArea", clearSelectionAreaAction);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteObjects");
        getActionMap().put("deleteObjects", deleteObjectsAction);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                checkSelectionArea();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                selectionStart = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                selectionEnd = e.getPoint();
                repaint();
            }
        });
    }

    private void addCreateLabelListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                Dialog dialog = new CreateLabelDialog(frame, handle, point.x, point.y);
            }
        });
    }

    private void addCreateArrowListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                buildingArrow.add(e.getPoint());
                repaint();
            }
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteArrow");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "deleteArrow");
        getActionMap().put("deleteArrow", deleteArrowAction);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "finishArrow");
        getActionMap().put("finishArrow", finishArrowAction);
    }

    private void addCreateObjectListener(Tool tool)
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                boolean isHalfWidth = tool.ordinal() > Tool.STORED_DATA.ordinal();
                chartObjects.add(new EditableImage(point.x - (isHalfWidth ? 37 : 74), point.y - 37, isHalfWidth ? 74 : 148,
                        74, handle, tool.getShapePath(), 0, null));
                handle.add((JComponent) chartObjects.get(chartObjects.size() - 1));
                repaint();
            }
        });
    }

    private void removeAllListeners()
    {
        if (currentTool == Tool.SELECTION)
        {
            unselectObjects();
            for (Selectable object : chartObjects)
                object.setSelectable(false);
        }
        if (currentTool == Tool.SELECT_AREA)
        {
            for (Selectable object : selectedObjects)
            {
                object.removeDragListeners();
                object.setBorder(null);
            }
        }
        if (currentTool == Tool.ARROW)
            finishArrow();

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "none");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "none");

        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);

        for (MouseMotionListener listener : getMouseMotionListeners())
            removeMouseMotionListener(listener);
    }

    public void unselectObjects()
    {
        selectedObjects.clear();
        menuBar.disableEditActions();

        for (Selectable object : chartObjects)
        {
            object.setDraggable(false);
            object.setSelectable(true);
            if (object instanceof EditableImage obj && obj.getMesh() != null)
                obj.getMesh().deleteMesh();
        }
    }

    private void checkSelectionArea()
    {
        selectedObjects.clear();

        for (Selectable object : chartObjects)
        {
            object.removeDragListeners();
            object.setBorder(null);
            Rectangle rect = getSelectionArea(selectionStart.x ,selectionStart.y, selectionEnd.x, selectionEnd.y);
            if (object.intersects(rect))
                addToSelectedObjects(object);
        }

        selectionEnd = null;
        repaint();
    }

    private void addToSelectedObjects(Selectable obj)
    {
        if (obj instanceof JComponent object)
        {
            selectedObjects.add(obj);
            object.setBorder(new LineBorder(Color.BLUE));
            object.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    selectionAnchor = e.getPoint();
                    object.setCursor(draggingCursor);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    Point parent = getParent().getLocationOnScreen();
                    Point mouse = e.getLocationOnScreen();
                    int xChange = mouse.x - parent.x - selectionAnchor.x - object.getX();
                    int yChange = mouse.y - parent.y - selectionAnchor.y - object.getY();

                    for (Selectable selected : selectedObjects)
                    {
                        selected.setLocation(selected.getX() + xChange, selected.getY() + yChange);
                        if (selected instanceof ArrowComponent arrow)
                        {
                            for (Point point : arrow.getPoints())
                            {
                                point.x += xChange;
                                point.y += yChange;
                            }
                        }
                    }
                    repaint();
                }
            });
        }
    }

    private Rectangle getSelectionArea(int x1, int y1, int x2, int y2)
    {
        int x = x1;
        int width = x2 - x1;
        int y = y1;
        int height = y2 - y1;

        if (width < 0)
        {
            x += width;
            width = Math.abs(width);
        }

        if (height < 0)
        {
            y += height;
            height = Math.abs(height);
        }

        return new Rectangle(x, y, width, height);
    }

    private void finishArrow()
    {
        if (buildingArrow.size() > 1)
            chartObjects.add(new ArrowComponent(this, (ArrayList<Point>) buildingArrow.clone()));
        buildingArrow.clear();
        repaint();
    }

    private void drawSelectionArea(Graphics g)
    {
        Rectangle selection = getSelectionArea(selectionStart.x ,selectionStart.y, selectionEnd.x, selectionEnd.y);

        float[] dashingPattern = {10f, 4f};
        Stroke stroke = new BasicStroke(1f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 0.0f);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.drawRect(selection.x, selection.y, selection.width, selection.height);
    }

    private void drawArrowhead(Graphics2D g, int d, int h) {
        int x1 = buildingArrow.get(buildingArrow.size() - 2).x;
        int y1 = buildingArrow.get(buildingArrow.size() - 2).y;
        int x2 = buildingArrow.get(buildingArrow.size() - 1).x;
        int y2 = buildingArrow.get(buildingArrow.size() - 1).y;
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

        g.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawArrow(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));
        for (int i = 1; i < buildingArrow.size(); i++)
            g2d.drawLine(buildingArrow.get(i - 1).x, buildingArrow.get(i - 1).y,
                    buildingArrow.get(i).x, buildingArrow.get(i).y);

        drawArrowhead(g2d, 20, 10);
    }

    public void clearWorkspace()
    {
        for (Selectable object : chartObjects)
            object.deleteObject();

        chartObjects.clear();
        selectedObjects.clear();
        buildingArrow.clear();
        menuBar.disableEditActions();
        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for (Selectable s : chartObjects)
            if (s instanceof ArrowComponent arrow)
                arrow.paintArrow(g);

        if (selectionEnd != null)
            drawSelectionArea(g);

        if (buildingArrow.size() > 1)
            drawArrow(g);
    }
}
