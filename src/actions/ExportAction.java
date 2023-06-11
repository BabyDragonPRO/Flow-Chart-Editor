package actions;

import filters.PNGFilter;
import main.EditorGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportAction extends AbstractAction
{
    private final JFrame frame;
    private final EditorGUI parent;

    public ExportAction(JFrame frame, EditorGUI parent)
    {
        super("Export", null);
        putValue(SHORT_DESCRIPTION, "Export the flowchart for presentation");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

        this.frame = frame;
        this.parent = parent;
    }

    private void exportToFile(File file)
    {
        parent.getToolPanel().hide();
        Rectangle rect = parent.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
        parent.paint(bufferedImage.getGraphics());

        try
        {
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
        parent.getToolPanel().show();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new PNGFilter());
        fc.setAcceptAllFileFilterUsed(false);

        int val = fc.showSaveDialog(parent);
        if (val == JFileChooser.APPROVE_OPTION)
        {
            File selected = fc .getSelectedFile();

            if (selected.getName().endsWith(".png"))
                exportToFile(selected);
            else
                exportToFile(new File(selected.getPath() + ".png"));
        }
    }
}
