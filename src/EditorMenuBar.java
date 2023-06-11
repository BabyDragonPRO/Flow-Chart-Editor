import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditorMenuBar implements ItemListener
{
    private JMenuBar mb;
    private JMenu fileMenu, windowMenu, editMenu;
    private JMenuItem newFile, open, saveAs, export, color, transform, editLabel;
    private JCheckBoxMenuItem toolBar;

    private NewFileAction newFileAction;
    private OpenFileAction openFileAction;
    private SaveAsAction saveAsAction;
    private ExportAction exportAction;
    private ShowPanelAction showToolBarAction;
    protected EditAction changeColorAction, transformAction, editLabelAction;

    public EditorMenuBar(EditorGUI parent, JFrame frame)
    {
        mb = new JMenuBar();
        fileMenu = new JMenu("File");
        windowMenu = new JMenu("Window");
        editMenu = new JMenu("Edit");

        newFileAction = new NewFileAction(parent);
        newFile = new JMenuItem(newFileAction);
        openFileAction = new OpenFileAction(parent);
        open = new JMenuItem(openFileAction);
        saveAsAction = new SaveAsAction(parent);
        saveAs = new JMenuItem(saveAsAction);
        exportAction = new ExportAction(frame, parent);
        export = new JMenuItem(exportAction);
        showToolBarAction = new ShowPanelAction(parent.toolPanel, "Tool Bar", "Show/Hide the tool bar");
        toolBar = new JCheckBoxMenuItem(showToolBarAction);
        toolBar.setSelected(true);
        parent.toolPanel.closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toolBar.setSelected(false);
                parent.toolPanel.hide();
            }
        });
        changeColorAction = new EditAction(frame, parent, 0, "Color", "Change color of the object");
        color = new JMenuItem(changeColorAction);
        transformAction = new EditAction(frame, parent, 1, "Transform", "Transform the object");
        transform = new JMenuItem(transformAction);
        editLabelAction = new EditAction(frame, parent, 2, "Edit Label", "Edit selected label");
        editLabel = new JMenuItem(editLabelAction);

        fileMenu.add(newFile);
        fileMenu.add(open);
        fileMenu.add(saveAs);
        fileMenu.add(export);
        windowMenu.add(toolBar);
        editMenu.add(color);
        editMenu.add(transform);
        editMenu.add(editLabel);
        mb.add(fileMenu);
        mb.add(windowMenu);
        mb.add(editMenu);
        frame.setJMenuBar(mb);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem)(e.getSource());
        boolean selected = (e.getStateChange() == ItemEvent.SELECTED);

        if (mi == toolBar)
            showToolBarAction.setEnabled(selected);
    }
}
