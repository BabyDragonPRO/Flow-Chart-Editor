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

            for (EditableImage img : parent.chartObjects)
                parent.remove(img);

            parent.chartObjects.clear();
            parent.selectedObjects.clear();

            boolean checkingShapes = false;

            while(scanner.hasNextLine())
            {
                String raw = scanner.nextLine();

                if (checkingShapes)
                {
                    String output[] = (raw).replace(" ", "").split(",", 0);
                    Color color = null;

                    if (output.length == 9)
                        color = new Color(Integer.parseInt(output[5]), Integer.parseInt(output[6]),
                                Integer.parseInt(output[7]), Integer.parseInt(output[8]));

                    EditableImage object = new EditableImage(Integer.parseInt(output[0]), Integer.parseInt(output[1]),
                            Integer.parseInt(output[2]), Integer.parseInt(output[3]), parent, output[4], color);

                    parent.chartObjects.add(object);
                    parent.add(object);

                    if (parent.currentTool == Tool.SELECTION)
                        object.setSelectable(true);
                }

                if (!checkingShapes && raw.equals("SHAPES"))
                    checkingShapes = true;
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();

        int val = fc.showOpenDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION)
            openFile(fc.getSelectedFile(), parent);
    }
}
