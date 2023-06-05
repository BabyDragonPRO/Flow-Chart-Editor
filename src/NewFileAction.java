import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class NewFileAction extends AbstractAction
{
    private EditorGUI parent;

    public NewFileAction(EditorGUI parent)
    {
        super("New File", null);
        putValue(SHORT_DESCRIPTION, "Creates new file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        parent.clearWorkspace();
    }
}
