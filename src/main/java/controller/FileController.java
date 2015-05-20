package controller;

import controls.ProjectStructure;
import model.ProjectFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class FileController {

    public void openFolder(JPanel panel, ProjectStructure projectStructure) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // open file chooser and check proceed if action was ok
        int returnValue = fileChooser.showOpenDialog(panel);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            changeProjectStructureFromProject(fileChooser.getSelectedFile(), projectStructure);
        }

        /*
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://graph.facebook.com/movilizer", Page.class);
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());
        */
    }

    /**
     * This function changes the structure of the project tree.
     *
     * @param dictionary       The selected dictionary
     * @param projectStructure The project structure which should be updated
     */
    private void changeProjectStructureFromProject(File dictionary, ProjectStructure projectStructure) {

        // set up the new project structure
        projectStructure.setUpNewProjectStructure(dictionary);

        // update model
        listFilesOfFolder(dictionary, projectStructure.getRootNode(),  projectStructure.getTreeModel());

        // reload the projects structure to refresh the UIs
        projectStructure.reload();

        System.out.println("folder: " + dictionary.getName() + " was opened as a new project structure");
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
                        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(new ProjectFile(file.getName(), "", file));
                        treeModel.insertNodeInto(currentNode, rootNode, rootNode.getChildCount());
                        if (file.isDirectory()) listFilesOfFolder(file, currentNode, treeModel);
                    }
                } catch (NullPointerException ex) {
                    System.err.print("file: " + file.getAbsolutePath() + " couldn't read.");
                }
            }
        }
    }

}