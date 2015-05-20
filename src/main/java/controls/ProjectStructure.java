package controls;

import model.ProjectFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class ProjectStructure extends JTree {

    private JLabel projectValueLabel, projectSizeValueLabel, projectDateValueLabel;

    public ProjectStructure(DefaultMutableTreeNode rootNode) {
        super(rootNode);
    }

    public JLabel getProjectValueLabel() {
        return projectValueLabel;
    }

    public void setProjectValueLabel(JLabel projectValueLabel) {
        this.projectValueLabel = projectValueLabel;
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
        ProjectFile projectFile = (ProjectFile) getRootNode().getUserObject();

        getProjectValueLabel().setText(projectFile.getName());

        getTreeModel().reload();
    }

    /**
     * Create a new product structure based on the dictionary.
     *
     * @param dictionary The dictionary which should be the new root of the project structure.
     */
    public void setUpNewProjectStructure(File dictionary) {
        getRootNode().removeAllChildren();
        ((ProjectFile) getRootNode().getUserObject()).setName(dictionary.getName());
    }
}