package ru.kpfu.itis.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import ru.kpfu.itis.App;

public class RootLayoutController {

    @FXML
    private MenuItem tableMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem refreshMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    private App app;


    @FXML
    private void initialize() {
        aboutMenuItem.setOnAction(this::handleAboutAction);
        exitMenuItem.setOnAction(this::handleExitAction);
        refreshMenuItem.setOnAction(this::handleRefreshAction);
        tableMenuItem.setOnAction(this::handleShowAction);
    }

    @FXML
    private void handleExitAction(ActionEvent e) {
        app.getPrimaryStage().close();
    }


    @FXML
    private void handleRefreshAction(ActionEvent e) {
        if (app.isViewTable())
            app.showItemsTable();
    }

    @FXML
    private void handleShowAction(ActionEvent e){
        app.showItemsTable();
    }

    @FXML
    private void handleAboutAction(ActionEvent e) {
        app.showAboutView();
    }


    public void setApp(App app) {
        this.app = app;
    }
}
