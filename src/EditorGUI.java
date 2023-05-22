import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class EditorGUI extends JPanel
{
    private DraggableImage test;
    private ResizeMesh test2;
    private JButton test3;

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

        test = new DraggableImage("./src/res/test.png");
        test.setBounds(500, 300, 200, 100);
        test.setDraggable(false);
        add(test);

        EditorGUI handle = this;

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (MouseListener listener : test.getMouseListeners())
                    test.removeMouseListener(listener);

                test.setDraggable(true);

                test2 = new ResizeMesh(handle, test);

                handle.setComponentZOrder(test, getComponentCount() - 1);
            }
        };

        test.addMouseListener(mouseListener);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                test2.deleteMesh();
                test.setDraggable(false);
                test.addMouseListener(mouseListener);
            }
        });
    }
}
