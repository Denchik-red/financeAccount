package den_n.financeaccount.pages.AddAccount;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.pages.main.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;

public class AddAccountController {
    @FXML
    private TextField accountNameField;

    private SessionFactory sessionFactory;
    private MainController mainController;

    private DAO<Account> accountDAO;

    public void closeBtnClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void saveAccountBtnClick(ActionEvent actionEvent) {

        if (accountNameField.getText().isBlank()) {
            System.out.println("name is empty");
            Alert.InfoAlert("name is empty");
            return;
        }
        try (Session session = sessionFactory.openSession()) {
            boolean exists = session
                    .createQuery("SELECT COUNT(a) FROM Account a WHERE a.name = :name", Long.class)
                    .setParameter("name", accountNameField.getText())
                    .uniqueResult() > 0;
            if (exists) {
                System.out.println("this account already exists");
                Alert.InfoAlert("this account already exists");
                return;
            }
            accountDAO.save(new Account(accountNameField.getText(), BigDecimal.valueOf(0)));

            accountNameField.setText("");
            mainController.reloadClick(new ActionEvent());

            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    public void putProperties(SessionFactory sessionFactory, MainController mainController) {
        this.sessionFactory = sessionFactory;
        this.mainController = mainController;

        this.accountDAO = new DAO<>(sessionFactory, Account.class);
    }
}
