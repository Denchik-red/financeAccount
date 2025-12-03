package den_n.financeaccount.pages.connecrionSettings;

import den_n.financeaccount.Alert;
import den_n.financeaccount.util.ConnectionPreferences;
import den_n.financeaccount.util.DatabaseConfigUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionSettingsController implements Initializable {

    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField databaseField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField schemaField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button testConnectionButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCurrentSettings();
    }

    private void loadCurrentSettings() {
        try {
            hostField.setText(ConnectionPreferences.getHost());
            portField.setText(ConnectionPreferences.getPort());
            databaseField.setText(ConnectionPreferences.getDatabase());
            usernameField.setText(ConnectionPreferences.getUsername());
            passwordField.setText(ConnectionPreferences.getPassword());
            schemaField.setText(ConnectionPreferences.getSchema());
        } catch (Exception e) {
            Alert.ErrorAlert("Error", "Failed to load connection settings: " + e.getMessage());
        }
    }

    @FXML
    private void testConnectionClick(ActionEvent event) {
        String host = hostField.getText();
        String port = portField.getText();
        String database = databaseField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (host.isEmpty() || port.isEmpty() || database.isEmpty() || username.isEmpty()) {
            Alert.ErrorAlert("Validation Error", "Please fill in all required fields (Host, Port, Database, Username)");
            return;
        }

        try {
            int portNum = Integer.parseInt(port);
            if (portNum <= 0 || portNum > 65535) {
                Alert.ErrorAlert("Validation Error", "Port must be between 1 and 65535");
                return;
            }
        } catch (NumberFormatException e) {
            Alert.ErrorAlert("Validation Error", "Port must be a valid number");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, username, password);
            conn.close();
            Alert.InfoAlert("Connection successful!");
        } catch (Exception e) {
            Alert.ErrorAlert("Connection Failed", "Could not connect to database: " + e.getMessage());
        }
    }

    @FXML
    private void saveConnectionClick(ActionEvent actionEvent) {
        try {
            String host = hostField.getText();
            String port = portField.getText();
            String database = databaseField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String schema = schemaField.getText();

            DatabaseConfigUtil.saveConnectionSettings(host, port, database, username, password, schema);

            Alert.InfoAlert("Connection settings saved successfully!");

            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            System.exit(0);
        } catch (Exception e) {
            Alert.ErrorAlert("Error", "Failed to save connection settings: " + e.getMessage());
        }
    }

    @FXML
    private void cancelClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
