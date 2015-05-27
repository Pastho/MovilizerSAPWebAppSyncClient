package controls;

import javax.swing.*;
import java.awt.event.ActionListener;

public class CustomMenuBar extends JMenuBar {

    private JMenu filesMenu, settingsMenu;
    private ActionListener parentActionListener;

    public CustomMenuBar(ActionListener parentActionListener) {
        this.parentActionListener = parentActionListener;

        setUpFileMenu();
        setUpSettingsMenu();
    }

    public ActionListener getParentActionListener() {
        return parentActionListener;
    }

    /**
     * Set up the file menu.
     */
    private void setUpFileMenu() {
        // initialize the basic components
        JMenuItem menuItem;

        // create the file menu
        filesMenu = new JMenu("File");

        // build the file menu items
        menuItem = new JMenuItem("Open Folder");
        menuItem.setActionCommand("menuItem-file-openFolder");
        menuItem.addActionListener(getParentActionListener());
        filesMenu.add(menuItem);

        menuItem = new JMenuItem("Show Project");
        menuItem.setActionCommand("menuItem-file-showProject");
        menuItem.addActionListener(getParentActionListener());
        filesMenu.add(menuItem);

        this.add(filesMenu);
    }

    /**
     * Set up the settings menu.
     */
    private void setUpSettingsMenu() {
        // initialize the basic components
        JMenuItem menuItem;

        // create the settings menu
        settingsMenu = new JMenu("Settings");

        // build the settings menu
        menuItem = new JMenuItem("SAP Connection");
        menuItem.setActionCommand("menuItem-settings-SAPConnection");
        menuItem.addActionListener(getParentActionListener());
        settingsMenu.add(menuItem);

        menuItem = new JMenuItem("Transport Request");
        menuItem.setActionCommand("menuItem-settings-Transportrequest");
        menuItem.addActionListener(getParentActionListener());
        settingsMenu.add(menuItem);

        this.add(settingsMenu);
    }
}
