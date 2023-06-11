import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

public class TextWHintField extends JTextField implements FocusListener
{
    private final String hint;
    private final Color hintColor;

    public TextWHintField(String hint, Color hintColor)
    {
        super(hint);

        this.hint = hint;
        this.hintColor = hintColor;
        setForeground(hintColor);
        super.addFocusListener(this);
    }

    public String getHint()
    {
        return hint;
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        if (Objects.equals(this.getText(), this.hint))
        {
            super.setText("");
            setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if (getText().isEmpty())
        {
            super.setText(this.hint);
            setForeground(this.hintColor);
        }
    }
}
