package controls;

import model.ProjectFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ProjectStructure extends JTree {

    private final String ZIPPATH = "." + File.separator + "resources" + File.separator;
    private final String PROJECTFOLDER = File.separator + "projects" + File.separator;

    private ProjectFile projectFile;
    private JLabel projectNameValueLabel, projectSizeValueLabel, projectDateValueLabel;
    private String username;

    public ProjectStructure(DefaultMutableTreeNode rootNode, String username) {
        super(rootNode);
        this.username = username;
        this.projectFile = new ProjectFile();
    }

    public JLabel getProjectNameValueLabel() {
        return projectNameValueLabel;
    }

    public void setProjectNameValueLabel(JLabel projectValueLabel) {
        this.projectNameValueLabel = projectValueLabel;
    }

    public JLabel getProjectSizeValueLabel() {
        return projectSizeValueLabel;
    }

    public void setProjectSizeValueLabel(JLabel projectSizeValueLabel) {
        this.projectSizeValueLabel = projectSizeValueLabel;
    }

    public JLabel getProjectDateValueLabel() {
        return projectDateValueLabel;
    }

    public void setProjectDateValueLabel(JLabel projectDateValueLabel) {
        this.projectDateValueLabel = projectDateValueLabel;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Returns the tree model of the product structure.
     *
     * @return Return the tree model of the product structure
     */
    public DefaultTreeModel getTreeModel() {
        return (DefaultTreeModel) this.getModel();
    }

    /**
     * Returns the root node of the product structure.
     *
     * @return Return the root node of the product structure
     */
    public DefaultMutableTreeNode getRootNode() {
        return (DefaultMutableTreeNode) getTreeModel().getRoot();
    }

    /**
     * Reload the product structure to refresh the UI and update the related labels.
     */
    public void reload() {
        // set up project file --> ZIP it and gather information
        setUpProjectFile();

        // reload the tree model to refresh the project structure on the GUI
        getTreeModel().reload();
    }

    /**
     * Create a new product structure based on the dictionary.
     *
     * @param dictionary The dictionary which should be the new root of the project structure.
     */
    public void setUpNewProjectStructure(File dictionary) {
        getRootNode().removeAllChildren();
        getRootNode().setUserObject(new ProjectFile(dictionary.getName(), dictionary));
    }

    public ProjectFile getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(ProjectFile projectFile) {
        this.projectFile = projectFile;
    }

    /**
     * Set up the project file by generating the ZIP file and gather the basic attributes
     */
    private void setUpProjectFile() {
        // create the ZIP file of the selected folder
        List<File> fileList = new ArrayList<>();
        File rootFile = ((ProjectFile) getRootNode().getUserObject()).getOriginalFile();

        // create a ZIP file of the root node
        getAllFiles(rootFile, fileList);
        writeZipFile(rootFile, fileList);

        // gather the basic file attributes and update the JLabels
        BasicFileAttributes basicAttributes = null;
        try {
            basicAttributes = Files.readAttributes(
                    Paths.get(rootFile.getPath()), BasicFileAttributes.class);

            getProjectNameValueLabel().setText(rootFile.getName());
            getProjectSizeValueLabel().setText(calculateProjectSizeToMB(basicAttributes.size()));
            getProjectDateValueLabel().setText(basicAttributes.creationTime().toString());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Calculates the size of the project in megabytes.
     *
     * @param size The size if the project in bytes
     * @return The size of the project in megabytes
     */
    private String calculateProjectSizeToMB(long size) {
        return Double.toString(size / 1000) + " MB";
    }

    /**
     * Select all related files of the selected directory.
     *
     * @param directory The directory in which should be searched
     * @param fileList  The file list which contains the selected files
     */
    public void getAllFiles(File directory, List<File> fileList) {
        for (File file : directory.listFiles()) {
            fileList.add(file);
            if (file.isDirectory()) {
                getAllFiles(file, fileList);
            }
        }
    }

    /**
     * Write the selected files of the fileList into a new ZIP file.
     *
     * @param directoryToZip The name and directory of the new ZIP file
     * @param fileList       The list of selected files which should be integrated into the ZIP file
     */
    public void writeZipFile(File directoryToZip, List<File> fileList) {

        try {
            File projectFolder = new File(ZIPPATH + getUsername() + PROJECTFOLDER);
            File zipFile = new File(ZIPPATH + getUsername() + PROJECTFOLDER + directoryToZip.getName() + ".zip");

            // check if folder is existing
            if(!projectFolder.exists()) {
                projectFolder.mkdir();
            }

            // create a new zip file if its not existing
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : fileList) {
                if (!file.isDirectory()) {
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the selected file to the new ZIP file.
     *
     * @param directoryToZip The path to the new ZIP file
     * @param file           The file which should be added to the new ZIP file
     * @param zos            The ZIP output stream which is used to generate the ZIP file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
            IOException {
        byte[] bytes = new byte[1024];
        int length;

        FileInputStream fis = new FileInputStream(file);

        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());

        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}