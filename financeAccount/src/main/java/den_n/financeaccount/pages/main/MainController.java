package den_n.financeaccount.pages.main;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;

import den_n.financeaccount.pages.AddAccount.AddAccountController;
import den_n.financeaccount.pages.AddCategory.AddCategoryController;
import den_n.financeaccount.pages.accountInfo.AccountInfoController;
import den_n.financeaccount.pages.newTransaction.NewTransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private ListView<Account> accountListView;
    ObservableList<Account> accountObservableList;


    private SessionFactory sessionFactory;
    private Stage addCategoryStage;
    private Stage newTransactionStage;
    private Stage addAccountStage;
    private Stage connectionSettingsStage;
    private Stage accoutInfo_stage;
    private AddCategoryController addCategoryController;
    private NewTransactionController newTransactionController;
    private AccountInfoController accountInfoController;

    private DAO<Account> accountDAO;

    public void initialize() {
        System.out.println("UI initialized!");
    }

    public void putProperties(SessionFactory sessionFactory, Stage addCategoryStage, AddCategoryController addCategoryController, Stage newTransactionStage, Stage connectionSettingsStage, Stage accoutInfo_stage, NewTransactionController newTransactionController, AccountInfoController accountInfoController) {
        this.sessionFactory = sessionFactory;
        this.addCategoryStage = addCategoryStage;
        this.addCategoryController = addCategoryController;
        this.newTransactionStage = newTransactionStage;
        this.connectionSettingsStage = connectionSettingsStage;
        this.accoutInfo_stage = accoutInfo_stage;
        this.newTransactionController = newTransactionController;
        this.accountInfoController = accountInfoController;

        accountDAO = new DAO<>(sessionFactory, Account.class);

        List<Account> accountList = accountDAO.findAll();
        accountList.forEach(System.out::println);
        accountObservableList = FXCollections.observableList(accountList);
        accountListView.setItems(accountObservableList);
        accountListView.setCellFactory(param -> {

            AccountListCell accountListCell = new AccountListCell();
            accountListCell.putProperties(sessionFactory, accoutInfo_stage, accountInfoController, this);
            return accountListCell;
        });
    }

    @FXML
    private void addAccountClick(ActionEvent actionEvent) {
        System.out.println("Add Account clicked");

        if (addAccountStage != null && addAccountStage.isShowing()) {
            addAccountStage.requestFocus();
            addAccountStage.toFront();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/addAccount-view.fxml"));
            Scene content = new Scene(fxmlLoader.load());
            AddAccountController addAccountController = fxmlLoader.getController();
            content.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/addAccount-view.css").toExternalForm());
            addAccountStage = new Stage();
            addAccountStage.setTitle("Новая категория");
            addAccountStage.setMinWidth(600);
            addAccountStage.setMinHeight(300);
            addAccountStage.setScene(content);

            Node node = (Node) actionEvent.getSource();
            Stage mainStage = (Stage) node.getScene().getWindow();

            addAccountStage.initOwner(mainStage);

            addAccountController.putProperties(sessionFactory, this);

            addAccountStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            Alert.ErrorAlert("Erorr", e.getMessage());
        }
    }

    @FXML
    private void addCategoryClick(ActionEvent actionEvent) {
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
        newTransactionStage.show();
        newTransactionController.reloadData();

    }

    @FXML
    private void connectionSettingsClick(ActionEvent actionEvent) {
        System.out.println("Connection Settings clicked");
        connectionSettingsStage.show();
    }
}