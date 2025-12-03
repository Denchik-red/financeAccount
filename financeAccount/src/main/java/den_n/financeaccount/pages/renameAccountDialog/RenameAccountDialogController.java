package den_n.financeaccount.pages.renameAccountDialog;

import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.pages.accountInfo.AccountInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

public class RenameAccountDialogController {

    @FXML
    private TextField categoryNameField;

    private AccountInfoController accountInfoController;
    private Account account;
    private DAO<Account> accountDAO;

    public void saveCategoryBtnClick(ActionEvent actionEvent) {

        account.setName(categoryNameField.getText());
        accountDAO.update(account);

        accountInfoController.load();
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void closeBtnClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void putProperties(SessionFactory sessionFactory, AccountInfoController accountInfoController, Account account) {
        this.accountInfoController = accountInfoController;
        this.account = account;

        this.accountDAO = new DAO<>(sessionFactory, Account.class);
    }
}
