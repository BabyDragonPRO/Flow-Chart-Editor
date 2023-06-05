import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsAction extends AbstractAction
{
    private EditorGUI parent;

    public SaveAsAction(EditorGUI parent)
    {
        super("Save As", null);
        putValue(SHORT_DESCRIPTION, "Saves flowchart for later edit");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        this.parent = parent;
    }

    private void saveToFile(File file, EditorGUI parent)
    {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));

            if (parent.chartObjects.size() > 0)
            {
                for (EditableImage object : parent.chartObjects)
                {
                    String s = "null";
                    Color color = object.getColor();
                    if (color != null)
                        s = String.format("%d, %d, %d, %d", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

                    bw.write(String.format("SHAPE, %d, %d, %d, %d, %s, %s\r\n", object.getX(), object.getY(),
                            object.getWidth(), object.getHeight(), object.getPath(), s));
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                bw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FlowchartFilter());
        fc.setAcceptAllFileFilterUsed(false);

        int val = fc.showSaveDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION)
        {
            File selected = fc .getSelectedFile();

            if (selected.getName().endsWith(".flowchart"))
                saveToFile(selected, parent);
            else
                saveToFile(new File(selected.getPath() + ".flowchart"), parent);
        }
    }
}
