import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import javax.swing.text.*;

public class Theme {
    public HashMap<String, MyStyle> definitions;

    public Theme(String styles) {
        definitions = new HashMap<String, MyStyle>();
        String[] lines = styles.split("\n");
        for(String l: lines) {
            String name = l.split(" ")[0];
            String[] props = l.substring(l.indexOf(" ")).split(";");
            definitions.put(name, new MyStyle(props));
        }
    }

    public SimpleAttributeSet getStyle(String name) {
        return null;
    }

    private class MyStyle {
        public SimpleAttributeSet set;
        public MyStyle(String[] styles) {
            set = new SimpleAttributeSet();
            for(String s : styles) {
                String[] args = s.split(" ");
                switch(args[0]) {
                    case "color":
                        Color c = Color.black;
                        try {
                            c = new Color(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        } finally {
                            StyleConstants.setForeground(set, c);
                        }
                        break;
                    case "italic":
                        StyleConstants.setItalic(set, true);
                        break;
                }
            }
        }
    }

    private class Node {
        private String name;
        private ArrayList<Node> children;
        private Node parent;
        private Color fg;

        public Node(Node _parent, String _name) {
            parent = _parent;
            name = _name;
        }

        public void addChild(Node child) {
            children.add(child);
        }
    }
}