package main;

import actions.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditorMenuBar implements ItemListener
{
    private final JMenuBar mb;
    private final JMenu fileMenu, windowMenu, editMenu;
    private final JMenuItem newFile, open, saveAs, export, color, transform, editLabel;
    private final JCheckBoxMenuItem toolBar;

    private final NewFileAction newFileAction;
    private final OpenFileAction openFileAction;
    private final SaveAsAction saveAsAction;
    private final ExportAction exportAction;
    private final ShowPanelAction showToolBarAction;
    private final EditAction editColorAction, transformAction, editLabelAction;

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
        showToolBarAction = new ShowPanelAction(parent.getToolPanel(), "Tool Bar", "Show/Hide the tool bar");
        toolBar = new JCheckBoxMenuItem(showToolBarAction);
        toolBar.setSelected(true);
        parent.getToolPanel().getCloseButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toolBar.setSelected(false);
                parent.getToolPanel().hide();
            }
        });
        editColorAction = new EditAction(frame, parent, 0, "Color", "Change color of the object");
        color = new JMenuItem(editColorAction);
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

    public void disableEditActions(boolean color, boolean transform, boolean text)
    {
        editColorAction.setEnabled(!color);
        transformAction.setEnabled(!transform);
        editLabelAction.setEnabled(!text);
    }

    public void disableEditActions()
    {
        disableEditActions(true, true, true);
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
