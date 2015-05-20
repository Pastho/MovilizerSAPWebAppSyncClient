package model;

import java.io.File;

public class ProjectFile {

    private File projectFile;
    private String name;
    private String projectRoot;
    private String projectPath;

    /**
     * Default constructor
     */
    public ProjectFile() {
    }

    /**
     * Constructor for complete project files
     *
     * @param name        The name of the project file
     * @param projectRoot The root of the project
     * @param projectFile The project file
     */
    public ProjectFile(String name, String projectRoot, File projectFile) {
        this.name = name;
        this.projectRoot = projectRoot;
        this.projectFile = projectFile;
    }

    /**
     * Constructor for simple project files
     *
     * @param name The name of the project file
     */
    public ProjectFile(String name) {
        this.name = name;
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    private void generateProjectPath() {

    }
}
