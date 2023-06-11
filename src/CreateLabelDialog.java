import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateLabelDialog extends JDialog
{
    private EditorGUI parent;
    private JPanel pane;
    private JLabel lblFontSize, lblText;
    private TextWHintField txtFontSize;
    private TextArea txtText;
    private JButton btnOK, btnCancel;
    private int x, y;

    public CreateLabelDialog(JFrame frame, EditorGUI parent, int x, int y)
    {
        super(frame, true);
        this.parent = parent;
        this.x = x;
        this.y = y;
        pane = new JPanel();
        pane.setBackground(new Color(240, 240, 240));
        pane.setLayout(null);
        setContentPane(pane);
        addLabels();
        addButtons();
        setTitle("Create Label");
        setSize(300, 300);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void addLabels()
    {
        lblFontSize = new JLabel("Font Size", JLabel.CENTER);
        lblFontSize.setBounds(15, 15, 80, 14);
        pane.add(lblFontSize);
        lblText = new JLabel("Label Text", JLabel.CENTER);
        lblText.setBounds(15, 55, 80, 14);
        pane.add(lblText);
        txtFontSize = new TextWHintField("Enter size", Color.RED);
        txtFontSize.setBounds(15, 35, 80, 14);
        pane.add(txtFontSize);
        txtText = new TextArea();
        txtText.setBounds(15, 75, 240, 130);
        pane.add(txtText);
        addDocumentsListeners();
    }

    private void addButtons()
    {
        btnOK = new JButton("OK");
        btnOK.setBounds(35, 225, 100, 23);
        btnOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (btnOK.isEnabled())
                    btnOKClicked();
            }
        });
        pane.add(btnOK);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(150, 225, 100, 23);
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setVisible(false);
                dispose();
            }
        });
        pane.add(btnCancel);
    }

    private void addDocumentsListeners()
    {
        txtFontSize.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                update();
            }
            public void removeUpdate(DocumentEvent e) {
                update();
            }
            public void insertUpdate(DocumentEvent e) {
                update();
            }
        });
    }

    private void btnOKClicked()
    {
        parseLabel();
        setVisible(false);
        dispose();
    }

    private void parseLabel()
    {
        int fontSize = Integer.parseInt(txtFontSize.getText());
        String text = txtText.getText();
        DraggableLabel label = new DraggableLabel(x, y, fontSize, text, parent, true);
        parent.chartObjects.add(label);
        parent.setComponentZOrder(label, 0);
        parent.repaint();
    }

    private void update()
    {
        btnOK.setEnabled(checkFontSize());
    }

    private boolean checkFontSize()
    {
        int fontSize = -1;

        try {
            fontSize = Integer.parseInt(txtFontSize.getText());
        } catch (Exception ignored) { }

        if (fontSize >= 0)
        {
            txtFontSize.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtFontSize.setForeground(Color.RED);
            return false;
        }
    }
}
