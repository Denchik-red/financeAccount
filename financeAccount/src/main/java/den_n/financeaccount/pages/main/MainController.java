package den_n.financeaccount.pages.main;

import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;

import den_n.financeaccount.pages.AddCategory.AddCategoryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.util.List;

public class MainController {

    @FXML
    private ListView<Account> accountListView;
    ObservableList<Account> accountObservableList;


    private SessionFactory sessionFactory;
    private Stage addCategoryStage;
    private AddCategoryController addCategoryController;

    private DAO<Account> accountDAO;

    public void initialize() {
        System.out.println("UI initialized!");
    }

    public void putProperties(SessionFactory sessionFactory, Stage addCategoryStage, AddCategoryController addCategoryController) {
        this.sessionFactory = sessionFactory;
        this.addCategoryStage = addCategoryStage;
        this.addCategoryController = addCategoryController;

        accountDAO = new DAO<>(sessionFactory, Account.class);

        List<Account> accountList = accountDAO.findAll();
        accountList.forEach(System.out::println);
        accountObservableList = FXCollections.observableList(accountList);
        accountListView.setItems(accountObservableList);
        accountListView.setCellFactory(param -> new AccountListCell());

    }

    @FXML
    private void addAccountClick() {
        System.out.println("Add Account clicked");
    }

    @FXML
    private void addCategoryClick() {
        System.out.println("Add Category clicked");
        addCategoryController.loadCategories();
        addCategoryStage.show();
    }

    @FXML
    public void reloadClick(ActionEvent actionEvent) {
        List<Account> accountList = accountDAO.findAll();
        accountObservableList = FXCollections.observableList(accountList);
        accountListView.setItems(accountObservableList);
    }

    @FXML
    public void newTransactionBtnClick(ActionEvent actionEvent) {
        System.out.println("Add transaction clicked");
    }
}