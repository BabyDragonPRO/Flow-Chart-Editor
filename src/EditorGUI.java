import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class EditorGUI extends JPanel
{
    private EditableImage test;
    private ResizeMesh test2;
    private JButton test3;

    public ArrayList<EditableImage> chartObjects;

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

        addChartObjects();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                unselectAll();
            }
        });
    }

    public void addChartObjects()
    {
        for (EditableImage object : chartObjects)
        {
            add(object);
        }
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
}
