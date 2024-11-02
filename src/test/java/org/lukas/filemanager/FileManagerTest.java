package org.lukas.filemanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {
    private FileManager fileManager;
    private File file;

    @BeforeEach
    public void beforeEach() throws IOException {
        this.file = new File("/home/lukasz/test");
        this.fileManager = new FileManager(file);
    }

    @AfterEach
    public void afterEach() throws IOException {
        fileManager.close();
        this.file.delete();
    }

    @Test
    public void writeTest() throws IOException {
        fileManager.write("a");
        fileManager.write("b");
        fileManager.write("c\n");
        fileManager.write("d    a");

        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(scanner.nextLine());
        }

        assertEquals("abc\nd    a", stringBuilder.toString());
    }

    @Test
    public void clearTest() throws IOException {
        fileManager.write("abc");
        fileManager.clear();
        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(scanner.nextLine());
        }

        assertEquals("", stringBuilder.toString());
    }
}
