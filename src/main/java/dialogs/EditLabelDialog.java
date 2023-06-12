package dialogs;

import components.DraggableLabel;
import components.TextWHintField;
import main.EditorGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditLabelDialog extends JDialog
{
    private final EditorGUI parent;
    private final JPanel pane;
    private final DraggableLabel label;
    private JLabel lblFontSize, lblText;
    private TextWHintField txtFontSize;
    private TextArea txtText;
    private JButton btnOK, btnCancel;

    public EditLabelDialog(JFrame frame, EditorGUI parent)
    {
        super(frame, true);
        this.parent = parent;
        this.label = (DraggableLabel) parent.selectedObjects.get(0);
        pane = new JPanel();
        pane.setBackground(new Color(240, 240, 240));
        pane.setLayout(null);
        setContentPane(pane);
        addLabels();
        addButtons();
        setTitle("Edit Label");
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
        setControls();
        addDocumentsListeners();
    }

    private void setControls()
    {
        txtFontSize.setText(String.valueOf(label.getFont().getSize()));
        txtText.setText(String.valueOf(label.getText()));

        txtFontSize.setForeground(Color.BLACK);
        txtText.setForeground(Color.BLACK);
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
        label.setText(text);
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));

        int width = getFontMetrics(label.getFont()).stringWidth(text);
        int height = getFontMetrics(label.getFont()).getHeight();
        label.setSize(width, height);

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
