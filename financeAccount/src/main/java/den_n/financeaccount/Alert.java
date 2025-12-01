package den_n.financeaccount;

import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

import java.util.Optional;

public class Alert {
    public static void ErrorAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        DialogPane dialogPane = alert.getDialogPane();

        dialogPane.getStylesheets().add(MainApplication.class.getResource("/den_n/financeaccount/css/alerts.css").toExternalForm());
        dialogPane.getStyleClass().add("alert-error");

        alert.showAndWait();
    }

    // Аналогично для других:
    public static void InfoAlert(String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(MainApplication.class.getResource("/den_n/financeaccount/css/alerts.css").toExternalForm());
        dialogPane.getStyleClass().add("alert-information");

        alert.showAndWait();
    }

    public static boolean ConfirmAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(MainApplication.class.getResource("/den_n/financeaccount/css/alerts.css").toExternalForm());
        dialogPane.getStyleClass().add("alert-confirmation");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}