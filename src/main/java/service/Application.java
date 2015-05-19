package service;

import controller.FileController;
import view.general.GeneralView;

public class Application {

    public static void main(String args[]) {

        // start the GUI
        new GeneralView(new FileController());
    }

}