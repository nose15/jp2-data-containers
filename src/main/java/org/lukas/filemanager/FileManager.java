package org.lukas.filemanager;

import java.io.*;

public class FileManager {
    private PrintWriter printWriter;
    private final File file;

    public FileManager(File file) throws IOException {
        this.file = file;
        printWriter = new PrintWriter(file);
    }

    public FileManager(String filePath) throws IOException {
        this.file = new File(filePath);
        printWriter = new PrintWriter(file);
    }

    public void write(String content) throws IOException {
        printWriter.print(content);
        printWriter.flush();
    }

    public void clear() throws IOException {
        printWriter = new PrintWriter(this.file);
    }

    /// This method needs to be called after finishing interaction with the FileManager
    public void close() throws IOException {
        printWriter.close();
    }
}
