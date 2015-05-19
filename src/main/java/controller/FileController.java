package controller;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class FileController {

    public void openFolder(JPanel panel, JTree projectStructure) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showOpenDialog(panel);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            changeProjectStructureFromProject(fileChooser.getSelectedFile(), projectStructure);
        } else {
            System.out.println("Open file Pop Up canceled by user");
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
    private void changeProjectStructureFromProject(File dictionary, JTree projectStructure) {

        // get the tree model
        DefaultTreeModel treeModel = (DefaultTreeModel) projectStructure.getModel();

        // get the root node
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        rootNode.removeAllChildren();
        rootNode.setUserObject("Project " + dictionary.getName());

        // update model
        treeModel.nodeChanged(rootNode);

        treeModel = listFilesOfFolder(dictionary, rootNode, treeModel);


        treeModel.reload();

        System.out.println("Open file: " + dictionary.getName());
    }

    /**
     * This function lists up all files which are nested in the chosen folder by acting recursive.
     *
     * @param dictionary The chosen folder
     * @param rootNode   The root node of the chosen folder
     * @param treeModel  The tree model which should be updated
     * @return Return the tree model for recursion
     */
    private DefaultTreeModel listFilesOfFolder(File dictionary, DefaultMutableTreeNode rootNode, DefaultTreeModel treeModel) {
        if (dictionary.isDirectory()) {
            for (File file : dictionary.listFiles()) {
                try {
                    if (file.canRead()) {
                        DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(file.getName());
                        treeModel.insertNodeInto(currentNode, rootNode, rootNode.getChildCount());
                        if (file.isDirectory()) {
                            listFilesOfFolder(file, currentNode, treeModel);
                        }
                    }
                } catch (NullPointerException ex) {
                    System.err.print("File " + file.getAbsolutePath() + " couldn't read");
                }
            }
        }
        return treeModel;
    }

}
