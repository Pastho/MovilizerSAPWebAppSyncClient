package model;

import java.io.File;

public class ProjectFile {

    private File originalFile;
    private String name;

    /**
     * Default constructor
     */
    public ProjectFile() {
        this.originalFile = null;
        this.name = "-";
    }

    /**
     * Constructor for complete project files
     *
     * @param name The name of the project file
     * @param file The project file
     */
    public ProjectFile(String name, File file) {
        this.name = name;
        this.originalFile = file;
    }

    /**
     * Constructor for simple project files
     *
     * @param name The name of the project file
     */
    public ProjectFile(String name) {
        this.name = name;
    }

    public File getOriginalFile() {
        return originalFile;
    }

    @Override
    public String toString() {
        return name;
    }

}