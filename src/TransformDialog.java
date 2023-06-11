import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransformDialog extends JDialog
{
    private EditorGUI parent;
    private JPanel pane;
    private JLabel lblX, lblY, lblWidth, lblHeight, lblRotation;
    private TextWHintField txtX, txtY, txtWidth, txtHeight, txtRotation;
    private JButton btnOK, btnCancel;

    public TransformDialog(JFrame frame, EditorGUI parent)
    {
        super(frame, true);
        this.parent = parent;
        pane = new JPanel();
        pane.setBackground(new Color(240, 240, 240));
        pane.setLayout(null);
        setContentPane(pane);
        addLabels();
        addButtons();
        setTitle("Transform");
        setSize(300, 200);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private void addLabels()
    {
        lblX = new JLabel("X", JLabel.CENTER);
        lblX.setBounds(15, 15, 80, 14);
        pane.add(lblX);
        lblY = new JLabel("Y", JLabel.CENTER);
        lblY.setBounds(105, 15, 80, 14);
        pane.add(lblY);
        lblWidth = new JLabel("Width", JLabel.CENTER);
        lblWidth.setBounds(15, 55, 80, 14);
        pane.add(lblWidth);
        lblHeight = new JLabel("Height", JLabel.CENTER);
        lblHeight.setBounds(105, 55, 80, 14);
        pane.add(lblHeight);
        lblRotation = new JLabel("Rotation", JLabel.CENTER);
        lblRotation.setBounds(195, 15, 80, 14);
        pane.add(lblRotation);
        txtX = new TextWHintField("0-1200", Color.RED);
        txtX.setBounds(15, 35, 80, 14);
        pane.add(txtX);
        txtY = new TextWHintField("0-800", Color.RED);
        txtY.setBounds(105, 35, 80, 14);
        pane.add(txtY);
        txtWidth = new TextWHintField("0-1200", Color.RED);
        txtWidth.setBounds(15, 75, 80, 14);
        pane.add(txtWidth);
        txtHeight = new TextWHintField("0-800", Color.RED);
        txtHeight.setBounds(105, 75, 80, 14);
        pane.add(txtHeight);
        txtRotation = new TextWHintField("0-360", Color.RED);
        txtRotation.setBounds(195, 35, 80, 14);
        pane.add(txtRotation);
        setControls();
        addDocumentsListeners();
    }

    private void setControls()
    {
        EditableImage obj = (EditableImage) parent.selectedObjects.get(0);
        txtX.setText(String.valueOf(obj.getX()));
        txtY.setText(String.valueOf(obj.getY()));
        txtWidth.setText(String.valueOf(obj.getWidth()));
        txtHeight.setText(String.valueOf(obj.getHeight()));
        txtRotation.setText(String.valueOf(obj.getRotation()));
        txtX.setForeground(Color.BLACK);
        txtY.setForeground(Color.BLACK);
        txtWidth.setForeground(Color.BLACK);
        txtHeight.setForeground(Color.BLACK);
        txtRotation.setForeground(Color.BLACK);
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
        txtX.getDocument().addDocumentListener(new DocumentListener() {
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

        txtY.getDocument().addDocumentListener(new DocumentListener() {
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

        txtWidth.getDocument().addDocumentListener(new DocumentListener() {
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

        txtHeight.getDocument().addDocumentListener(new DocumentListener() {
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

        txtRotation.getDocument().addDocumentListener(new DocumentListener() {
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
        parseTransformation();
        setVisible(false);
        dispose();
    }

    private void parseTransformation()
    {
        int x = Integer.parseInt(txtX.getText());
        int y = Integer.parseInt(txtY.getText());
        int width = Integer.parseInt(txtWidth.getText());
        int height = Integer.parseInt(txtHeight.getText());
        int rotation = Integer.parseInt(txtRotation.getText());
        EditableImage obj = (EditableImage)parent.selectedObjects.get(0);
        obj.setBounds(x, y, width, height);
        obj.setRotation(rotation);
        obj.mesh.update(-1);
        parent.repaint();
    }

    private void update()
    {
        boolean x = checkX();
        boolean y = checkY();
        boolean width = checkWidth();
        boolean height = checkHeight();
        boolean rotation = checkRotation();

        btnOK.setEnabled(x && y && width && height && rotation);
    }

    private boolean checkX()
    {
        int x = -1;

        try {
            x = Integer.parseInt(txtX.getText());
        } catch (Exception ignored) { }

        if (x >= 0 && x <= 1200)
        {
            txtX.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtX.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkY()
    {
        int y = -1;

        try {
            y = Integer.parseInt(txtY.getText());
        } catch (Exception ignored) { }

        if (y >= 0 && y <= 800)
        {
            txtY.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtY.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkWidth()
    {
        int w = -1;

        try {
            w = Integer.parseInt(txtWidth.getText());
        } catch (Exception ignored) { }

        if (w >= 0 && w <= 1200)
        {
            txtWidth.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtWidth.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkHeight()
    {
        int h = -1;

        try {
            h = Integer.parseInt(txtHeight.getText());
        } catch (Exception ignored) { }

        if (h >= 0 && h <= 800)
        {
            txtHeight.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtHeight.setForeground(Color.RED);
            return false;
        }
    }

    private boolean checkRotation()
    {
        int r = -1;

        try {
            r = Integer.parseInt(txtRotation.getText());
        } catch (Exception ignored) { }

        if (r >= 0 && r <= 360)
        {
            txtRotation.setForeground(Color.BLACK);
            return true;
        }
        else
        {
            txtRotation.setForeground(Color.RED);
            return false;
        }
    }
}
