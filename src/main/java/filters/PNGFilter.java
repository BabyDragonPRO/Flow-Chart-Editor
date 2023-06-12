package filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PNGFilter extends FileFilter
{
    @Override
    public boolean accept(File file)
    {
        return file.isDirectory() || file.getName().endsWith(".png");
    }

    @Override
    public String getDescription()
    {
        return "PNG";
    }
}
