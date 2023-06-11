package util;

public enum Tool
{
    SELECTION("selection", "Can select and move objects around"),
    SELECT_AREA("select_area", "Selects objects in an area"),
    LABEL("label", "Add a label"),
    ARROW("arrow", "Add an arrow"),
    TERMINATOR("terminator", "Indicates the beginning or end of a program flow in the flowchart"),
    PROCESS("process", "Indicates any processing function"),
    DECISION("decision", "Indicates a decision point between two or more paths in a flowchart"),
    DELAY("delay", "Indicates a delay in the process"),
    DATA("data", "Can represent any type of data in a flowchart"),
    DOCUMENT("document", "Indicates data that can be read by people, such as printed output"),
    DOCUMENTS("documents", "Indicates multiple documents"),
    SUBROUTINE("subroutine", "Indicates a predefined (named) process, such as a subroutine or a module"),
    PREPARATION("preparation", "Indicates a modification to a process, such as setting a switch or initializing a routine"),
    DISPLAY("display", "Indicates data that is displayed for people to read, such as data on a monitor or projector screen"),
    MANUAL_INPUT("manual_input", "Indicates any operation that is performed manually (by a person)"),
    MANUAL_LOOP("manual_loop", "Indicates a sequence of commands to repeat until stopped manually"),
    LOOP_LIMIT("loop_limit", "Indicates the start of a loop, rotate the shape vertically to indicate the end of a loop"),
    STORED_DATA("stored_data", "Indicates any type of stored data"),
    CONNECTOR("connector", "Indicates an inspection point"),
    OFF_PAGE_CONNECTOR("off_page_connector", "Use this shape to create a cross-reference and hyperlink from a process on one page to a process on another page"),
    OR("or", "Logical OR"),
    AND("and", "Logical AND"),
    COLLATE("collate", "Indicates a step that organizes data into a standard format"),
    SORT("sort", "Indicates a step that organizes items list sequentially"),
    MERGE("merge", "Indicates a step that combines multiple sets into one"),
    DATABASE("database", "Indicates a list of information with a standard structure that allows for searching and sorting"),
    INTERNAL_STORAGE("internal_storage", "Indicates an internal storage device");

    private String iconPath;
    private String iconToolTip;

    Tool(String path, String toolTip)
    {
        iconPath = path;
        iconToolTip = toolTip;
    }

    public String getIconPath()
    {
        return "./src/res/" + iconPath + "_tool_icon.png";
    }

    public String getIconPressedPath()
    {
        return "./src/res/" + iconPath + "_tool_icon_pressed.png";
    }

    public String getIconToolTip()
    {
        return iconToolTip;
    }

    public String getShapePath()
    {
        return "./src/res/" + iconPath + ".png";
    }
}
