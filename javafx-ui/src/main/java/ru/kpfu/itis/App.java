package ru.kpfu.itis;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kpfu.itis.controller.ItemEditDialogController;
import ru.kpfu.itis.controller.ItemInfoDialogController;
import ru.kpfu.itis.controller.ItemsTableViewController;
import ru.kpfu.itis.controller.RootLayoutController;
import ru.kpfu.itis.model.Item;
import ru.kpfu.itis.service.ItemRestService;

import java.io.IOException;

public class App extends Application {

    private ItemRestService restService;

    private boolean isViewTable;
    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        restService = new ItemRestService();
        this.primaryStage = primaryStage;
        initRootLayout();
        showItemsTable();
    }

    public void initRootLayout() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/RootLayout.fxml"));
        try {
            rootLayout = fxmlLoader.load();
            primaryStage.setResizable(false);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            RootLayoutController controller = fxmlLoader.getController();
            controller.setApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAboutView() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/About.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            rootLayout.setCenter(anchorPane);
            isViewTable = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Table of items
    public void showItemsTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ItemsTableView.fxml"));
        try {
            AnchorPane itemPane = fxmlLoader.load();
            rootLayout.setCenter(itemPane);

            ItemsTableViewController controller = fxmlLoader.getController();
            controller.setApp(this);
            isViewTable = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showItemInfoDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/ItemInfoDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Information");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemInfoDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(item);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showItemEditDialog(Item item, ObservableList<Item> items) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/ItemEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(item, items);
            controller.setApp(this);


            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAlertWarningDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText(message);
        alert.initOwner(primaryStage);
        alert.showAndWait();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public ItemRestService getRestService() {
        return restService;
    }

    public boolean isViewTable() {
        return isViewTable;
    }
}
