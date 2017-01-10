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
            l = l.trim();
            if(l.startsWith("#")) {continue;}
            String name = l.split(" ")[0];
            String[] props = l.substring(l.indexOf(" ") + 1).split(";");
            definitions.put(name, new MyStyle(props));
        }
    }

    /**
     * Gets the style that covers this context/state. Include an
     * asterik after dash to include all subcontexts of a context.
     * If a style definition does not exist, goes to parent context
     * and adds an asterik to see if that matches anything. If it
     * still cant find anything, it will go to the parent again.
     */
    public SimpleAttributeSet getStyle(String name) {
        String search = name;
	    if(definitions.containsKey(search)) {
	        return definitions.get(search).set;
	    }
    	String[] contexts = search.split("-");
    	contexts[contexts.length - 1] = "*";
    	search = join(contexts, "-");
    	while(search.length() > 0) {
    	    if(definitions.containsKey(search)) {
    		    return definitions.get(search).set;
    	    } else {
    		    contexts = search.split("-");
    		    contexts = subarray(contexts, 0, contexts.length - 1);
        		if(contexts.length > 0) {
                    contexts[contexts.length - 1] = "*";
        		    search = join(contexts, "-");
                } else {
                    search = ""; //stops loop
                }
    	    }
    	}
    	return new MyStyle(new String[] {"color 0 0 0"}).set;
    }

    private String join(String[] array, String glue) {
	    String str = "";
	    for(int i = 0; i < array.length; i++) {
	       str += array[i];
	       if(i != array.length - 1) {str += glue;}
	    }
	    return str;
    }

    private String[] subarray(String[] array, int begin, int end) {
	    String[] arr = new String[end - begin];
	    for(int i = begin; i < end; i++) {
	        arr[i] = array[i];
	    }
	    return arr;
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
