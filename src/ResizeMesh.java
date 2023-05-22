import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ResizeMesh
{
    private JPanel panel;
    private JComponent parent;

    private ArrayList<DraggableComponent> buttons;

    public ResizeMesh(JPanel panel, JComponent parent)
    {
        this.panel = panel;
        this.parent = parent;

        parent.setBorder(new LineBorder(Color.BLUE));

        buttons = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            buttons.add(new DraggableComponent(Color.BLUE));

        buttons.get(0).setBounds(parent.getX() - 5, parent.getY() - 5, 10, 10);
        buttons.get(1).setBounds(parent.getX() + parent.getWidth() / 2 - 5, parent.getY() - 5, 10, 10);
        buttons.get(2).setBounds(parent.getX() + parent.getWidth() - 5, parent.getY() - 5, 10, 10);
        buttons.get(3).setBounds(parent.getX() + parent.getWidth() - 5, parent.getY() + parent.getHeight() / 2 - 5, 10, 10);
        buttons.get(4).setBounds(parent.getX() + parent.getWidth() - 5, parent.getY() + parent.getHeight() - 5, 10, 10);
        buttons.get(5).setBounds(parent.getX() + parent.getWidth() / 2 - 5, parent.getY() + parent.getHeight() - 5, 10, 10);
        buttons.get(6).setBounds(parent.getX() - 5, parent.getY() + parent.getHeight() - 5, 10, 10);
        buttons.get(7).setBounds(parent.getX() - 5, parent.getY() + parent.getHeight() / 2 - 5, 10, 10);

        buttons.get(0).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        buttons.get(1).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        buttons.get(2).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        buttons.get(3).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        buttons.get(4).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        buttons.get(5).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        buttons.get(6).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        buttons.get(7).setDraggingCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));

        buttons.get(1).setXLocked(true);
        buttons.get(3).setYLocked(true);
        buttons.get(5).setXLocked(true);
        buttons.get(7).setYLocked(true);

        for (int i = 0; i < 8; i++)
        {
            int finalI = i; // cannot use the i variable for some reason, says it needs to be final or something
            buttons.get(i).addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    update(finalI);
                }
            });

            panel.add(buttons.get(i), 0);
        }

        parent.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                update(-1);
            }
        });
    }

    private void update(int index)
    {
        int x1 = parent.getX();
        int y1 = parent.getY();
        int x2 = x1 + parent.getWidth();
        int y2 = y1 + parent.getHeight();

        switch (index) {
            case 0 -> {
                x1 = buttons.get(0).getX() + 5;
                y1 = buttons.get(0).getY() + 5;
            }

            case 1 -> y1 = buttons.get(1).getY() + 5;

            case 2 -> {
                x2 = buttons.get(2).getX() + 5;
                y1 = buttons.get(2).getY() + 5;
            }

            case 3 -> x2 = buttons.get(3).getX() + 5;

            case 4 -> {
                x2 = buttons.get(4).getX() + 5;
                y2 = buttons.get(4).getY() + 5;
            }

            case 5 -> y2 = buttons.get(5).getY() + 5;

            case 6 -> {
                x1 = buttons.get(6).getX() + 5;
                y2 = buttons.get(6).getY() + 5;
            }
            case 7 -> x1 = buttons.get(7).getX() + 5;
        }

        redrawMesh(x1, y1, x2 - x1, y2 - y1);

        parent.setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    private void redrawMesh(int x, int y, int width, int height)
    {
        buttons.get(0).setLocation(x - 5, y - 5);
        buttons.get(1).setLocation(x + width / 2 - 5, y - 5);
        buttons.get(2).setLocation(x + width - 5, y - 5);
        buttons.get(3).setLocation(x + width - 5, y + height / 2 - 5);
        buttons.get(4).setLocation(x + width - 5, y + height - 5);
        buttons.get(5).setLocation(x + width / 2 - 5, y + height - 5);
        buttons.get(6).setLocation(x - 5, y + height - 5);
        buttons.get(7).setLocation(x - 5, y + height / 2 - 5);
    }

    public void deleteMesh()
    {
        parent.setBorder(null);

        for (DraggableComponent button : buttons)
        {
            button.hide();
            panel.remove(button);
        }
    }
}
