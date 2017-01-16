import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		setup();

		FileUpdateChecker checker = new FileUpdateChecker();
		Window w = new Window(checker);
		w.setVisible(true);
	}

	/**
	 * Method to load plugins, register EditorKits, etc before window creation.
	 */
	public static void setup() {
		JEditorPane.registerEditorKitForContentType("text/html_code", "HtmlCodeKit");
	}
}