package android.com.models;

public class DeleteProgress {

    private String fileSize;
    private boolean isCompleted;
    private String fileToUploadPath;
    private String filename;
    private int progress;


    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    public String getFileToUploadPath() {
        return fileToUploadPath;
    }

    public void setFileToUploadPath(String fileToUploadPath) {
        this.fileToUploadPath = fileToUploadPath;
    }


    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


}
