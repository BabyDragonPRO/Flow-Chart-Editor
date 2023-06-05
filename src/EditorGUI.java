import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditorGUI extends JPanel
{
    private Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

    private final EditorGUI handle;
    protected EditorMenuBar menuBar;
    protected ToolPanel toolPanel;
    protected LayerPanel layerPanel;
    public ArrayList<EditableImage> chartObjects;
    public ArrayList<EditableImage> selectedObjects;
    public ArrayList<ArrowComponent> arrows;
    public Tool currentTool;

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
            for (EditableImage object : selectedObjects)
            {
                object.removeDragListeners();
                object.setBorder(null);
            }
        }
    };
    private final AbstractAction deleteObjectsAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (EditableImage object : selectedObjects)
                handle.remove(object);

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
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
        setOpaque(true);

        chartObjects = new ArrayList<>();
        selectedObjects = new ArrayList<>();

        toolPanel = new ToolPanel(10, 10, this);

        add(toolPanel);
        selectTool(Tool.SELECTION);

        menuBar = new EditorMenuBar(this, frame);
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

                default -> {
                    addCreateObjectListener(tool);
                }
            }

            currentTool = tool;
        }
    }

    private void addSelectionListeners()
    {
        for (EditableImage object : chartObjects)
            object.setSelectable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("SSS");
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

    private void addCreateObjectListener(Tool tool)
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                boolean isHalfWidth = tool.ordinal() > Tool.STORED_DATA.ordinal();
                chartObjects.add(new EditableImage(point.x - (isHalfWidth ? 37 : 74), point.y - 37, isHalfWidth ? 74 : 148,
                        74, handle, tool.getShapePath(), null));
                handle.add(chartObjects.get(chartObjects.size() - 1));
                repaint();
            }
        });
    }

    private void removeAllListeners()
    {
        if (currentTool == Tool.SELECTION)
        {
            unselectObjects();
            for (EditableImage object : chartObjects)
                object.setSelectable(false);
        }

        if (currentTool == Tool.SELECT_AREA)
        {
            for (EditableImage object : selectedObjects)
            {
                object.removeDragListeners();
                object.setBorder(null);
            }
        }

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "none");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "none");

        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);

        for (MouseMotionListener listener : getMouseMotionListeners())
            removeMouseMotionListener(listener);
    }

    public void unselectObjects()
    {
        selectedObjects.clear();
        menuBar.changeColorAction.setEnabled(false);
        menuBar.transformAction.setEnabled(false);

        for (EditableImage object : chartObjects)
        {
            object.setDraggable(false);
            object.setSelectable(true);
            if (object.mesh != null)
                object.mesh.deleteMesh();
        }
    }

    private void checkSelectionArea()
    {
        selectedObjects.clear();

        for (EditableImage object : chartObjects)
        {
            object.setBorder(null);
            object.removeDragListeners();

            Rectangle rect1 = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight());
            Rectangle rect2 = getSelectionArea(selectionStart.x ,selectionStart.y, selectionEnd.x, selectionEnd.y);
            if (rect1.intersects(rect2))
                addToSelectedObjects(object);
        }

        selectionEnd = null;
        repaint();
    }

    private void addToSelectedObjects(EditableImage object)
    {
        selectedObjects.add(object);
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

                for (EditableImage obj : selectedObjects)
                    obj.setLocation(obj.getX() + xChange, obj.getY() + yChange);
            }
        });
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

    public void clearWorkspace()
    {
        for (EditableImage object : chartObjects)
        {
            if (object.mesh != null)
                object.mesh.deleteMesh();

            remove(object);
        }

        chartObjects.clear();
        selectedObjects.clear();

        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (selectionEnd != null)
            drawSelectionArea(g);
    }
}
