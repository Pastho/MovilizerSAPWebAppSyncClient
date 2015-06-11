package controller;

import controls.ProjectStructure;
import model.ProjectFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class FileController {

    private final String ZIPPATH = "." + File.separator + "resources" + File.separator;
    private final String PROJECTFOLDER = File.separator + "projects" + File.separator;
    private final String ZIPENDING = ".zip";

    public String openFolder(JPanel panel, ProjectStructure projectStructure) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // open file chooser and check proceed if action was ok
        int returnValue = fileChooser.showOpenDialog(panel);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            projectStructure.setProjectFile(new ProjectFile(fileChooser.getSelectedFile().getName(), fileChooser.getSelectedFile()));
            changeProjectStructureFromProject(projectStructure);
        }

        return fileChooser.getSelectedFile().getName();
    }

    /**
     * This function changes the structure of the project tree.
     *
     * @param projectStructure The project structure which should be updated
     */
    private void changeProjectStructureFromProject(ProjectStructure projectStructure) {
        File projectFile = projectStructure.getProjectFile().getOriginalFile();

        // set up the new project structure
        projectStructure.setUpNewProjectStructure(projectFile);

        // update model
        listFilesOfFolder(projectFile, projectStructure.getRootNode(), projectStructure.getTreeModel());

        // reload the projects structure to refresh the UIs
        projectStructure.reload();

        System.out.println("folder: " + projectFile.getName() + " was opened as a new project structure");
    }

    /**
     * This function lists up all files which are nested in the chosen folder by acting recursive.
     *
     * @param dictionary The chosen folder
     * @param rootNode   The root node of the chosen folder
     * @param treeModel  The tree model which should be updated
     */
    private void listFilesOfFolder(File dictionary, DefaultMutableTreeNode rootNode, DefaultTreeModel treeModel) {
        if (dictionary.isDirectory()) {
            for (File file : dictionary.listFiles()) {
                try {
                    if (file.canRead()) {
                        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new ProjectFile(file.getName(), file));
                        treeModel.insertNodeInto(currentNode, rootNode, rootNode.getChildCount());
                        if (file.isDirectory()) listFilesOfFolder(file, currentNode, treeModel);
                    }
                } catch (NullPointerException ex) {
                    System.err.print("file: " + file.getAbsolutePath() + " couldn't read.");
                }
            }
        }
    }

    public File readProjectFile(String username, String projectName) {
        return new File(ZIPPATH + username + PROJECTFOLDER + projectName + ZIPENDING );
    }

}