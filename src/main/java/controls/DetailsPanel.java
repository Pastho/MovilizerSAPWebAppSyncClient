package controls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DetailsPanel extends JPanel {

    private JPanel detailsNotesPanel, detailsActionsPanel;
    private ActionListener parentActionListener;
    private ProjectStructure projectStructure;

    public DetailsPanel(ActionListener parentActionListener, LayoutManager layoutManager, ProjectStructure projectStructure) {
        super(layoutManager);
        this.parentActionListener = parentActionListener;
        this.projectStructure = projectStructure;

        setUpDetailsNotePanel();
        this.add(detailsNotesPanel);
        setUpDetailsNoteActionsPanel();
        this.add(detailsActionsPanel);
    }

    public ActionListener getParentActionListener() {
        return parentActionListener;
    }

    public ProjectStructure getProjectStructure() {
        return projectStructure;
    }

    /**
     * Set up the details note panel
     */
    private void setUpDetailsNotePanel() {
        detailsNotesPanel = new JPanel();

        // define group layout for details notes
        GroupLayout layout = new GroupLayout(detailsNotesPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // project name
        JLabel projectNameTitle = new JLabel("Project:");
        JLabel projectNameValue = new JLabel("-");
        getProjectStructure().setProjectNameValueLabel(projectNameValue);
        projectNameValue.setFont(projectNameValue.getFont().deriveFont(Font.PLAIN));

        // project size
        JLabel projectSizeTitle = new JLabel("Size:");
        JLabel projectSizeValue = new JLabel("-");
        getProjectStructure().setProjectSizeValueLabel(projectSizeValue);
        projectSizeValue.setFont(projectSizeValue.getFont().deriveFont(Font.PLAIN));

        // project date
        JLabel projectDateTitle = new JLabel("Date of Change:");
        JLabel projectDateValue = new JLabel("-");
        getProjectStructure().setProjectDateValueLabel(projectDateValue);
        projectDateValue.setFont(projectDateValue.getFont().deriveFont(Font.PLAIN));

        // create the group layout
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(projectNameTitle)
                                        .addComponent(projectSizeTitle)
                                        .addComponent(projectDateTitle)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(projectNameValue)
                                        .addComponent(projectSizeValue)
                                        .addComponent(projectDateValue)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectNameTitle)
                                        .addComponent(projectNameValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectSizeTitle)
                                        .addComponent(projectSizeValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(projectDateTitle)
                                        .addComponent(projectDateValue)
                        )
        );

        detailsNotesPanel.setLayout(layout);
    }

    /**
     * Set up the details action panel
     */
    private void setUpDetailsNoteActionsPanel() {
        detailsActionsPanel = new JPanel(new GridLayout(1, 1));

        JButton sendButton = new JButton("Send WebApp");
        sendButton.setActionCommand("button-send-webapp");
        sendButton.addActionListener(getParentActionListener());
        detailsActionsPanel.add(sendButton);
    }
}
