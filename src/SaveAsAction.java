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

    private void saveToFile(File file)
    {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));

            if (parent.chartObjects.size() > 0)
            {
                for (Selectable object : parent.chartObjects)
                {
                    if (object instanceof EditableImage image)
                        saveShape(image, bw);
                    else if (object instanceof DraggableLabel label)
                        saveLabel(label, bw);
                    else if (object instanceof ArrowComponent arrow)
                        saveArrow(arrow, bw);
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

    private void saveShape(EditableImage object, BufferedWriter bw) throws IOException
    {
        String s = "null";
        Color color = object.getColor();
        if (color != null)
            s = String.format("%d, %d, %d, %d", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bw.write(String.format("SHAPE, %d, %d, %d, %d, %s, %d, %s\r\n", object.getX(), object.getY(),
                object.getWidth(), object.getHeight(), object.getPath(), object.getRotation(), s));
    }

    private void saveArrow(ArrowComponent object, BufferedWriter bw) throws IOException
    {
        StringBuilder s = new StringBuilder("ARROW");
        for (Point p : object.points)
        {
            s.append(", ");
            s.append(p.x);
            s.append(", ");
            s.append(p.y);
        }

        s.append("\r\n");
        bw.write(s.toString());
    }

    private void saveLabel(DraggableLabel object, BufferedWriter bw) throws IOException
    {
        bw.write(String.format("LABEL, %d, %d, %d, %s\r\n", object.getX(), object.getY(),
                object.getFont().getSize(), object.getText()));
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
                saveToFile(selected);
            else
                saveToFile(new File(selected.getPath() + ".flowchart"));
        }
    }
}
