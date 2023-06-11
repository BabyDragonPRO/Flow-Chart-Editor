import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditColorDialog extends JDialog
{
    private EditorGUI parent;
    private JPanel pane;
    private JLabel lblRed, lblGreen, lblBlue;
    private TextWHintField txtRed, txtGreen, txtBlue;
    private JButton btnOK, btnCancel;

    public EditColorDialog(JFrame frame, EditorGUI parent)
    {
        super(frame, true);
        this.parent = parent;
        pane = new JPanel();
        pane.setBackground(new Color(240, 240, 240));
        pane.setLayout(null);
        setContentPane(pane);
        addLabels();
        addButtons();
        setTitle("Edit Color");
        setSize(300, 200);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void addLabels()
    {
        lblRed = new JLabel("Red", JLabel.CENTER);
        lblRed.setBounds(15, 20, 80, 14);
        pane.add(lblRed);
        lblGreen = new JLabel("Green", JLabel.CENTER);
        lblGreen.setBounds(105, 20, 80, 14);
        pane.add(lblGreen);
        lblBlue = new JLabel("Blue", JLabel.CENTER);
        lblBlue.setBounds(195, 20, 80, 14);
        pane.add(lblBlue);

        txtRed = new TextWHintField("0-255", Color.RED);
        txtRed.setBounds(15, 50, 80, 14);
        pane.add(txtRed);
        txtGreen = new TextWHintField("0-255", Color.RED);
        txtGreen.setBounds(105, 50, 80, 14);
        pane.add(txtGreen);
        txtBlue = new TextWHintField("0-255", Color.RED);
        txtBlue.setBounds(195, 50, 80, 14);
        pane.add(txtBlue);

        addDocumentsListeners();
    }

    private void addButtons()
    {
        btnOK = new JButton("OK");
        btnOK.setBounds(35, 125, 100, 23);
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
        btnCancel.setBounds(150, 125, 100, 23);
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
        txtRed.getDocument().addDocumentListener(new DocumentListener() {
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

        txtBlue.getDocument().addDocumentListener(new DocumentListener() {
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

        txtBlue.getDocument().addDocumentListener(new DocumentListener() {
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
        parseColor();
        setVisible(false);
        dispose();
    }

    private void parseColor()
    {
        int red = Integer.parseInt(txtRed.getText());
        int green = Integer.parseInt(txtGreen.getText());
        int blue = Integer.parseInt(txtBlue.getText());
        ((EditableImage)parent.selectedObjects.get(0)).setColor(new Color(red, green, blue));
        parent.repaint();
    }

    private void update()
    {
        boolean red = checkRed();
        boolean green = checkGreen();
        boolean blue = checkBlue();

        btnOK.setEnabled(red && green && blue);
    }

    private boolean checkRed()
    {
        int red = -1;

        try {
            red = Integer.parseInt(txtRed.getText());
        } catch (Exception ignored) { }

        if (red >= 0 && red <= 255)
        {
            txtRed.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtRed.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkGreen()
    {
        int green = -1;

        try {
            green = Integer.parseInt(txtGreen.getText());
        } catch (Exception ignored) { }

        if (green >= 0 && green <= 255)
        {
            txtGreen.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtGreen.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkBlue()
    {
        int blue = -1;

        try {
            blue = Integer.parseInt(txtBlue.getText());
        } catch (Exception ignored) { }

        if (blue >= 0 && blue <= 255)
        {
            txtBlue.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtBlue.setForeground(Color.RED);
            return false;
        }
    }
}
