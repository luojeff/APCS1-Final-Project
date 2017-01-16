import java.io.File;

public class FileUpdateChecker {
	private String originalContents;
	private String newContents;
	private File file = null;

	/**
	 * Checks for file changes. Required for auto-overwrite in save functions.
	 */
	public FileUpdateChecker() {
	}

	public void init(File file, String originalContents) {
		this.file = file;
		this.originalContents = originalContents;
	}

	public boolean isFileSet() {
		return (this.file != null);
	}

	public boolean hasContents() {
		return (this.originalContents != null);
	}

	/**
	 * Compares old contents with input
	 */
	public boolean isTextChanged(String newContents) {
		this.newContents = newContents;
		return !(originalContents.equals(newContents));
	}

	public void setContents(String newContents) {
		this.newContents = newContents;
	}

	public void updateContents() {
		originalContents = newContents;
	}

	public File returnFile() {
		return file;
	}

	public String returnFileName() {
		return file.getAbsolutePath();
	}
}
