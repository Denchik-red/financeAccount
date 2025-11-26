package den_n.financeaccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private VBox accountList;

    @FXML
    private Button addAccountBtn;

    @FXML
    private Button addCategoryBtn;

    // Можно добавить логику при клике
    public void initialize() {
        System.out.println("UI initialized!");
    }

    @FXML
    private void onAddAccount() {
        System.out.println("Add Account clicked");
    }

    @FXML
    private void onAddCategory() {
        System.out.println("Add Category clicked");
    }
}