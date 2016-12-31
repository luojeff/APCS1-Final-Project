import javax.swing.JEditorPane;

public class Main {
    public static void main(String[] args){
        setup();
	    Window w = new Window();
	    w.setVisible(true);
    }

    /**
    * Method to load plugins, register EditorKits, etc
    * before window creation.
    */
    public static void setup() {
        JEditorPane.registerEditorKitForContentType("x-code/html", "HtmlCodeKit");
    }
}
