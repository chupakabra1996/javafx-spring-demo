package ru.kpfu.itis.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.App;
import ru.kpfu.itis.exception.ServerException;
import ru.kpfu.itis.model.Item;

import java.util.List;
import java.util.Objects;

public class ItemsTableViewController {

    // Items Table
    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, Integer> idColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, Double> priceColumn;

    @FXML
    private TableColumn<Item, Item> infoColumn;

    @FXML
    private TableColumn<Item, Item> deleteColumn;

    @FXML
    private TableColumn<Item, Item> updateColumn;

    private ObservableList<Item> observableList;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    public Button saveButton;

    private App app;

    @FXML
    private void initialize() {

        observableList = FXCollections.observableArrayList();

        saveButton.setOnAction(this::handleSaveButtonAction);

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        infoColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        deleteColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        updateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));

        // Item info

        infoColumn.setCellFactory(param -> new TableCell<Item, Item>() {

            final ImageView buttonGraphic = new ImageView("info.png");
            final Button button = new Button("info", buttonGraphic);

            {
                button.setMinSize(20, 20);
            }

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setGraphic(button);
                    button.setOnAction(e -> app.showItemInfoDialog(item));
                } else {
                    setGraphic(null);
                }
            }
        });


        // Delete item

        deleteColumn.setCellFactory(param -> new TableCell<Item, Item>() {

            final ImageView buttonGraphic = new ImageView("delete.png");
            final Button button = new Button("delete", buttonGraphic);

            {
                button.setMinSize(20, 20);
            }

            @Override
            protected void updateItem(Item item, boolean empty) {

                super.updateItem(item, empty);

                if (!Objects.isNull(item)) {
                    setGraphic(button);
                    button.setOnAction(e -> {
                        try {
                            app.getRestService().delete(item.getId());
                            List<Item> refreshItems = app.getRestService().getAll();
                            observableList.clear();
                            observableList.addAll(refreshItems);
                        } catch (ServerException e1) {
                            app.showAlertWarningDialog("Can't delete an item!");
                            e1.printStackTrace();
                        }
                    });
                } else {
                    setGraphic(null);
                }
            }
        });

        // Edit item

        updateColumn.setCellFactory(param -> new TableCell<Item, Item>() {

            final ImageView buttonGraphic = new ImageView("edit.png");
            final Button button = new Button("", buttonGraphic);

            {
                button.setMinSize(20, 20);
            }

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setGraphic(button);
                    button.setOnAction(e -> {
                        app.showItemEditDialog(item, observableList);
                    });
                } else {
                    setGraphic(null);
                }
            }
        });


        itemTable.setItems(observableList);
    }


    @FXML
    private void handleSaveButtonAction(ActionEvent e) {

        try {
            checkItem(nameField.getText(), priceField.getText());
            Item newItem = new Item(nameField.getText(), Double.parseDouble(priceField.getText()));
            observableList.add(app.getRestService().create(newItem));
            nameField.clear();
            priceField.clear();
        } catch (ServerException | RuntimeException e1) {
            app.showAlertWarningDialog(e1.getMessage());
        }
    }


    private void updateTableView() {
        try {
            List<Item> items = app.getRestService().getAll();
            observableList.clear();
            observableList.addAll(items);

        } catch (ServerException e) {
            app.showAlertWarningDialog(e.getMessage());
        }
        itemTable.setItems(observableList);
    }


    public void setApp(App app) {
        this.app = app;
        updateTableView();
    }


    private void checkItem(String name, String price) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Item's name shouldn't be empty");
        }
        try {
            double p = Double.parseDouble(price);
            if (p < 0) {
                throw new IllegalArgumentException("Price shouldn't be less than zero");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Price is not valid!");
        }
    }

}
