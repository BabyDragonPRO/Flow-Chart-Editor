package util;

import javax.swing.border.Border;
import java.awt.*;

public interface Selectable
{
    void setSelectable(boolean s);

    void setDraggable(boolean d);

    void setLocation(int x, int y);

    void deleteObject();

    void addDragListeners();

    void removeDragListeners();

    void addClickListeners();

    void removeClickListeners();

    void setBorder(Border border);

    int getX();

    int getY();

    boolean intersects(Rectangle rect);
}
