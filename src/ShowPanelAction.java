import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowPanelAction extends AbstractAction
{
    private JPanel panel;

    public ShowPanelAction(JPanel panel, String name, String description)
    {
        super(name, null);
        putValue(SHORT_DESCRIPTION, description);

        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (panel.isVisible())
            panel.hide();

        else
            panel.show();
    }
}
