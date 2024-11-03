package org.lukas.filemanager;

import java.io.*;
import java.nio.file.Path;

public class FileManager {
    private PrintWriter printWriter;
    private final File file;

    public FileManager(File file) throws IOException {
        this.file = file;
        printWriter = new PrintWriter(file);
    }

    public FileManager(Path filePath) throws IOException {
        this.file = new File(String.valueOf(filePath));
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
