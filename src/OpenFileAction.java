import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
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

    private void openFile(File file, EditorGUI parent)
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
            }

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

        if (output.length == 10)
            color = new Color(Integer.parseInt(output[6]), Integer.parseInt(output[7]),
                    Integer.parseInt(output[8]), Integer.parseInt(output[9]));

        EditableImage object = new EditableImage(Integer.parseInt(output[1]), Integer.parseInt(output[2]),
                Integer.parseInt(output[3]), Integer.parseInt(output[4]), parent, output[5], color);

        parent.chartObjects.add(object);
        parent.add(object);

        if (parent.currentTool == Tool.SELECTION)
            object.setSelectable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FlowchartFilter());
        fc.setAcceptAllFileFilterUsed(false);

        int val = fc.showOpenDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION)
            openFile(fc.getSelectedFile(), parent);
    }
}
