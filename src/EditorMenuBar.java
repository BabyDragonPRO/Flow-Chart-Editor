import javax.swing.*;

public class EditorMenuBar
{
    private JMenuBar mb;
    private JMenu fileMenu;
    private JMenuItem open, saveAs;

    private OpenFileAction openFileAction;
    private SaveAsAction saveAsAction;

    public EditorMenuBar(EditorGUI parent, JFrame frame)
    {
        mb = new JMenuBar();
        fileMenu = new JMenu("File");

        openFileAction = new OpenFileAction(parent);
        open = new JMenuItem("Open");
        open.setAction(openFileAction);

        saveAsAction = new SaveAsAction(parent);
        saveAs = new JMenuItem();
        saveAs.setAction(saveAsAction);

        fileMenu.add(open);
        fileMenu.add(saveAs);

        mb.add(fileMenu);
        frame.setJMenuBar(mb);
    }
}
