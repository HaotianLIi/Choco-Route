package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class UIHelper {

    public static void showMessage(Label lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.setStyle("-fx-text-fill: " + (success ? "green" : "red") + ";");
    }

    public static <T> void loadTable(TableView<T> table, List<T> items) {
        table.setItems(FXCollections.observableArrayList(items));
    }

    public static void bindColumn(TableColumn<String[], String> col, int index) {
        col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[index]));
    }

    public static void bindColumn(TableColumn<String[], String> col, int index, String prefix) {
        col.setCellValueFactory(data -> new SimpleStringProperty(prefix + data.getValue()[index]));
    }
}
