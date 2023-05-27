import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class EditorGUI extends JPanel
{
    private ToolPanel toolPanel;
    private LayerPanel layerPanel;

    public ArrayList<EditableImage> chartObjects;

    private Tool currentTool;

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
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);
        setOpaque(true);

        chartObjects = new ArrayList<>();

        chartObjects.add(new EditableImage(500, 300, 200, 100, this, "./src/res/test.png"));
        chartObjects.add(new EditableImage(100, 300, 200, 100, this, "./src/res/test.png"));
        chartObjects.add(new EditableImage(400, 600, 200, 100, this, "./src/res/test.png"));

        toolPanel = new ToolPanel(10, 10, this);

        add(toolPanel);

        addChartObjects();
    }

    public void unselectAll()
    {
        for (EditableImage object : chartObjects)
        {
            object.setDraggable(false);
            object.setSelectable(true);
            if (object.mesh != null)
                object.mesh.deleteMesh();
        }
    }

    public void selectTool(Tool tool)
    {
        if (currentTool != tool)
        {
            removeAllListeners();

            if (currentTool == Tool.SELECTION)
            {
                unselectAll();

                for (EditableImage object : chartObjects)
                    object.setSelectable(false);
            }

            switch (tool)
            {
                case SELECTION -> {
                    for (EditableImage object : chartObjects)
                        object.setSelectable(true);

                    addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            unselectAll();
                        }
                    });
                }

                case TERMINATOR, PROCESS, DECISION, DELAY, DATA, DOCUMENT, DOCUMENTS -> {
                    EditorGUI handle = this;

                    handle.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Point point = e.getPoint();

                            chartObjects.add(new EditableImage(point.x - 50, point.y - 50, 100, 100, handle, "./src/res/test.png"));
                            handle.add(chartObjects.get(chartObjects.size() - 1));
                            repaint();
                        }
                    });
                }
            }

            currentTool = tool;
        }
    }

    private void removeAllListeners()
    {
        for (MouseListener listener : getMouseListeners())
            removeMouseListener(listener);
    }

    public void addChartObjects()
    {
        for (EditableImage object : chartObjects)
            add(object);
    }
}
