import java.io.File;

public class FileUpdateChecker {
    private String originalContents;
    private String newContents; 
    private File file;

    /**
     * Checks for file changes. Required for auto-overwrite
     * in save functions.
     */
    public FileUpdateChecker(File file, String originalContents){
	this.file = file;
        this.originalContents = originalContents;
    }

    /**
     * Compares old contents with input
     */
    public boolean isTextChanged(String newContents){
	this.newContents = newContents;
	return !(originalContents.equals(newContents));
    }

    public void updateFile(){
	originalContents = newContents;
    }

    public File returnFile(){
	return file;
    }

    public String returnFileName(){
	return file.getAbsolutePath();
    }    
}
