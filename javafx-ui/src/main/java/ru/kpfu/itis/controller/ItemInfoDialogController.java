package ru.kpfu.itis.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.kpfu.itis.model.Item;

public class ItemInfoDialogController {

    @FXML private Button OKButton;

    @FXML private Label idLabel;

    @FXML private Label nameLabel;

    @FXML private Label priceLabel;


    @FXML private Label idValueLabel;

    @FXML private Label priceValueLabel;

    @FXML private Label nameValueLabel;


    private Stage dialogStage;

    private Item item;


    @FXML
    private void initialize() {
        OKButton.setOnAction(this::handleOKAction);
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    public void setItem(Item item) {
        this.item = item;
        idValueLabel.setText(String.valueOf(item.getId()));
        priceValueLabel.setText(String.valueOf(item.getPrice()));
        nameValueLabel.setText(item.getName());
    }

    @FXML
    private void handleOKAction(ActionEvent e) {
        dialogStage.close();
    }
}
