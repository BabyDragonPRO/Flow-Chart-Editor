
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToolPanel extends JPanel
{
    private ArrayList<ImageButton> tools;

    public ToolPanel(int x, int y, int width, int height)
    {
        setBounds(50, 50, 500, 500);
        setBackground(Color.BLACK);
        setLayout(null);

        tools = new ArrayList<>();
    }
}
