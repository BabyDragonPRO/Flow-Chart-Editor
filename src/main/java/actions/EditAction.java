package actions;

import components.DraggableLabel;
import components.EditableImage;
import dialogs.EditColorDialog;
import dialogs.EditLabelDialog;
import dialogs.TransformDialog;
import main.EditorGUI;
import util.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditAction extends AbstractAction
{
    private final JFrame frame;
    private final EditorGUI parent;
    private final int mode;
    private JDialog actionDialog;

    public EditAction(JFrame frame, EditorGUI parent, int mode, String name, String description)
    {
        super(name, null);
        putValue(SHORT_DESCRIPTION, description);

        this.frame = frame;
        this.parent = parent;
        this.mode = mode;
    }

    @Override
    public boolean isEnabled()
    {
        if (mode == 0 || mode == 1)
            return parent.currentTool == Tool.SELECTION && parent.selectedObjects.size() == 1
                    && parent.selectedObjects.get(0) instanceof EditableImage;
        else if (mode == 2)
            return parent.currentTool == Tool.SELECTION && parent.selectedObjects.size() == 1
                    && parent.selectedObjects.get(0) instanceof DraggableLabel;

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (mode == 0)
            actionDialog = new EditColorDialog(frame, parent);

        else if (mode == 1)
            actionDialog = new TransformDialog(frame, parent);

        else if (mode == 2)
            actionDialog = new EditLabelDialog(frame, parent);
    }
}
