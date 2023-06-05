import javax.swing.*;

public class EditColorDialog extends JDialog
{
    private EditorGUI parent;
    private JPanel pane;
    private JLabel lblRed, lblGreen, lblBlue;
    private JTextField txtRed, txtGreen, txtBlue;
    private JButton btnOK, btnCancel;

    public EditColorDialog(JFrame frame, EditorGUI parent)
    {
        super(frame, true);
        this.parent = parent;

        setTitle("Edit Color");
        setSize(300, 200);
        setLocationRelativeTo(frame);
        setVisible(true);
    }
}
