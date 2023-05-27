
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ToolPanel extends JPanel
{
    private ArrayList<Tool> tools;
    private ArrayList<ImageButton> toolButtons;
    private ImageButton closeButton;

    private EditorGUI parent;
    private Image image;
    private Point anchor;
    private Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

    private boolean draggable;

    public ToolPanel(int x, int y, EditorGUI parent)
    {
        try {
            image = ImageIO.read(new File("./src/res/tool_panel.png"));
        }

        catch (IOException e)
        {
            System.out.println(e.toString());
        }

        this.parent = parent;

        setBounds(x, y, 75, 550);
        setLayout(null);

        ToolPanel handle = this;
        closeButton = new ImageButton(45, 12, 20, 20, "./src/res/close_icon.png", "./src/res/close_icon.png");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                handle.hide();
            }
        });

        add(closeButton);

        tools = new ArrayList<>();
        toolButtons = new ArrayList<>();

        tools.add(Tool.SELECTION);
        tools.add(Tool.SELECT_AREA);
        tools.add(Tool.TERMINATOR);
        tools.add(Tool.PROCESS);
        tools.add(Tool.DECISION);
        tools.add(Tool.DELAY);
        tools.add(Tool.DATA);
        tools.add(Tool.DOCUMENT);
        tools.add(Tool.DOCUMENTS);

        for (int i = 0; i < tools.size(); i++)
        {
            toolButtons.add(new ImageButton(i % 2 == 1 ? 40 : 5,
                    i / 2 * 35 + 48,
                    30,
                    30,
                    tools.get(i).getIconPath(),
                    tools.get(i).getIconPressedPath()));

            toolButtons.get(i).setToolTipText(tools.get(i).getIconToolTip());

            int finalI = i;
            toolButtons.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    parent.selectTool(tools.get(finalI));
                }
            });

            add(toolButtons.get(i));
        }

        addDragListeners();

        parent.add(this);
    }

    protected void addDragListeners()
    {
        final ToolPanel handle = this;

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                anchor = e.getPoint();
                if (anchor.y < 42 & !(anchor.y <= 37 & anchor.y >= 7 & anchor.x >= 40 & anchor.x <= 70))
                {
                    draggable = true;
                    setCursor(draggingCursor);
                }
                else
                {
                    draggable = false;
                    setCursor(Cursor.getDefaultCursor());
                }

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggable)
                {
                    Point parent = getParent().getLocationOnScreen();
                    Point mouse = e.getLocationOnScreen();
                    Point position = new Point(mouse.x - parent.x - anchor.x,
                            mouse.y - parent.y - anchor.y);
                    setLocation(position);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (image != null)
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

        else
        {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public boolean imageUpdate(Image image, int infoFlags, int x, int y, int w, int h)
    {
        if (infoFlags == ALLBITS)
        {
            repaint();
            return false;
        }

        return true;
    }
}
