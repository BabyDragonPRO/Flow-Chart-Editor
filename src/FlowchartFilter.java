import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FlowchartFilter extends FileFilter
{
    @Override
    public boolean accept(File file)
    {
        return file.isDirectory() || file.getName().endsWith(".flowchart");
    }

    @Override
    public String getDescription()
    {
        return "Flowchart";
    }
}
