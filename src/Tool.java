public enum Tool
{
    SELECTION("selection", "Can select and move objects around"),
    SELECT_AREA("select_area", "Selects objects in an area"),
    TERMINATOR("terminator", "Create terminator"),
    PROCESS("process", "Create process"),
    DECISION("decision", "Create decision"),
    DELAY("delay", "Create delay"),
    DATA("data", "Create data"),
    DOCUMENT("document", "Create document"),
    DOCUMENTS("documents", "Create documents");

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
}
