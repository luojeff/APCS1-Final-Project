import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.awt.Color;
import javax.swing.text.*;

public class Theme {
    public HashMap<String, MyStyle> definitions;
    public HashMap<String, String> variables;

    public Theme(String styles) {
        definitions = new HashMap<String, MyStyle>();
        variables = new HashMap<String, String>();
        String[] lines = styles.split("\n");
        for(String l: lines) {
            l = l.trim();
            if(l.startsWith("#")) {continue;}
            else if(l.startsWith("$")) {
                String name = l.split(" ")[0].substring(1); //from the dollar sign till space
                String val = l.substring(l.indexOf(" ") + 1); //after space till newline
                variables.put(name, val);
            }
            else if(l.length() < 1) {continue;}
            else {
                String name = l.split(" ")[0];
                String[] props = l.substring(l.indexOf(" ") + 1).split(";");
                definitions.put(name, new MyStyle(props));
            }
        }
    }

    public static Theme fromFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new File(filename));
        in.useDelimiter("\n");
        String str = "";
        while(in.hasNext()) {str += in.next() + "\n";}
        return new Theme(str.substring(0, str.length() - 1));
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
	        arr[i - begin] = array[i];
	    }
	    return arr;
    }

    private String[] subarray(String[] array, int begin) {
        return subarray(array, begin, array.length);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public String getVariable(String name) {
        if(!hasVariable(name)) {return null;}
        return variables.get(name);
    }

    public Color toColor(String[] params) {
        if(params[0].startsWith("$")) {
            return toColor(getVariable(params[0].substring(1)).split(" "));
        }
        if(params.length == 1) {
            int c = Integer.parseInt(params[0]);
            return new Color(c, c, c);
        }
        return new Color(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
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
                            c = toColor(subarray(args, 1));
                        } catch(Exception e) {
                            //nothing
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
