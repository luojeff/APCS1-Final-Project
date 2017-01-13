import javax.swing.*;

public class Main {
    public static void main(String[] args){
        setup();
	Window w = new Window();
	w.setVisible(true);
	
	try {
	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (UnsupportedLookAndFeelException e) {
	} catch (ClassNotFoundException e) {
	} catch (InstantiationException e) {
	} catch (IllegalAccessException e) {
	}
    }

    /**
     * Method to load plugins, register EditorKits, etc
     * before window creation.
     */
    public static void setup() {
        JEditorPane.registerEditorKitForContentType("text/html_code", "HtmlCodeKit");
    }
}
