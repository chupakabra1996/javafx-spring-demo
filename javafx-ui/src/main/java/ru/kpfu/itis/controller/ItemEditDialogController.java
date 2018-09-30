package ru.kpfu.itis.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.App;
import ru.kpfu.itis.exception.ServerException;
import ru.kpfu.itis.model.Item;

import java.util.List;

public class ItemEditDialogController {

    @FXML private Label nameLabel;

    @FXML private Label priceLabel;

    @FXML private TextField nameField;

    @FXML private TextField priceField;

    @FXML private Label IDLabel;

    @FXML private Text idText;

    @FXML private Button saveButton;

    private Stage dialogStage;

    private ObservableList<Item> items;

    private Item editedItem;

    private App app;

    @FXML
    private void initialize() {
        saveButton.setOnAction(this::handleSaveButtonAction);
    }


    public void setItem(Item item, ObservableList<Item> items) {
        this.editedItem = item;
        this.items = items;
        idText.setText(String.valueOf(item.getId()));
        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
    }


    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }


    @FXML
    private void handleSaveButtonAction(ActionEvent e) {

        try {
            checkItem(nameField.getText(), priceField.getText());

            Item newItem = new Item(nameField.getText(), Double.parseDouble(priceField.getText()));

            editedItem = app.getRestService().update(editedItem.getId(), newItem);

            List<Item> refresh = app.getRestService().getAll();
            items.clear();
            items.addAll(refresh);

        } catch (ServerException e1) {
            app.showAlertWarningDialog(e1.getMessage());
            e1.printStackTrace();
        } finally {
            dialogStage.close();
        }
    }


    public void setApp(App app) {
        this.app = app;
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
