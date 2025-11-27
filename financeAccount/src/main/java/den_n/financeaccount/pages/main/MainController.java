package den_n.financeaccount.pages.main;

import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.hibernate.SessionFactory;

import java.util.List;

public class MainController {

    @FXML
    private ListView<Account> accountListView;


    private SessionFactory sessionFactory;
    private DAO<Account> accountDAO;

    public void initialize() {
        System.out.println("UI initialized!");
    }

    public void putProperties(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        accountDAO = new DAO<>(sessionFactory, Account.class);

        List<Account> accountList = accountDAO.findAll();
        accountList.forEach(System.out::println);
        ObservableList<Account> accountObservableList = FXCollections.observableList(accountList);
        accountListView.setItems(accountObservableList);
        accountListView.setCellFactory(param -> new AccountListCell());

    }

    @FXML
    private void addAccountClick() {
        System.out.println("Add Account clicked");
        // Ваша логика открытия формы
    }

    @FXML
    private void addCategoryClick() {
        System.out.println("Add Category clicked");
        // Ваша логика
    }

    @FXML
    public void reloadClick(ActionEvent actionEvent) {
        System.out.println("Reload clicked");
    }

    @FXML
    public void newTransactionBtnClick(ActionEvent actionEvent) {
        System.out.println("Add transaction clicked");
    }
}