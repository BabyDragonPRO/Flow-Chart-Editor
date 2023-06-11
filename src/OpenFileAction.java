import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenFileAction extends AbstractAction
{
    private EditorGUI parent;

    public OpenFileAction(EditorGUI parent)
    {
        super("Open", null);
        putValue(SHORT_DESCRIPTION, "Opens a flowchart for edit");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        this.parent = parent;
    }

    private void openFile(File file)
    {
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
            parent.clearWorkspace();

            while(scanner.hasNextLine())
            {
                String raw = scanner.nextLine();
                String[] output = (raw).replace(" ", "").split(",", 0);
                if (output[0].equals("SHAPE"))
                    loadShape(output);
                else if (output[0].equals("LABEL"))
                    loadLabel(output);
                else if (output[0].equals("ARROW"))
                    loadArrow(output);
            }

            setLabelsAtTop();
            parent.repaint();
            scanner.close();
        }

        catch (Exception e) {
            if (scanner != null)
                scanner.close();

            e.printStackTrace();
        }
    }

    private void loadShape(String[] output)
    {
        Color color = null;

        if (output.length == 11)
            color = new Color(Integer.parseInt(output[7]), Integer.parseInt(output[8]),
                    Integer.parseInt(output[9]), Integer.parseInt(output[10]));

        EditableImage object = new EditableImage(Integer.parseInt(output[1]), Integer.parseInt(output[2]),
                Integer.parseInt(output[3]), Integer.parseInt(output[4]), parent, output[5],
                Integer.parseInt(output[6]), color);

        parent.chartObjects.add(object);

        if (parent.currentTool == Tool.SELECTION)
            object.setSelectable(true);
    }

    private void loadLabel(String[] output)
    {
        DraggableLabel object = new DraggableLabel(Integer.parseInt(output[1]), Integer.parseInt(output[2]),
                Integer.parseInt(output[3]), output[4], parent, false);

        parent.chartObjects.add(object);

        if (parent.currentTool == Tool.SELECTION)
            object.setSelectable(true);
    }

    private void loadArrow(String[] output)
    {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 1; i < output.length - 1; i += 2)
            points.add(new Point(Integer.parseInt(output[i]),
                                 Integer.parseInt(output[i + 1])));

        ArrowComponent object = new ArrowComponent(parent, points);
        parent.chartObjects.add(object);

        if (parent.currentTool == Tool.SELECTION)
            object.setSelectable(true);
    }

    private void setLabelsAtTop()
    {
        for (Selectable obj : parent.chartObjects)
        {
            if (obj instanceof DraggableLabel object)
                parent.setComponentZOrder(object, 0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FlowchartFilter());
        fc.setAcceptAllFileFilterUsed(false);

        int val = fc.showOpenDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION)
            openFile(fc.getSelectedFile());
    }
}
