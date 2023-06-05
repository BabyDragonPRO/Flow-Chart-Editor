import javax.swing.*;

public class TransformDialog extends JDialog
{
    private EditorGUI parent;

    public TransformDialog(JFrame frame, EditorGUI parent)
    {
        super(frame, true);
        this.parent = parent;

        setTitle("Transform");
        setSize(300, 200);
        setLocationRelativeTo(frame);
        setVisible(true);
    }
}
