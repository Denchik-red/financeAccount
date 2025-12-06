package den_n.financeaccount.pages.newTransaction;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.MainApplication;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.module.Category;
import den_n.financeaccount.module.Transaction;
import den_n.financeaccount.pages.main.MainController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class NewTransactionController {

    private SessionFactory sessionFactory;
    private DAO<Category> categoryDAO;
    private DAO<Account> accountDAO;
    private DAO<Transaction> transactionDAO;

    private MainController mainController;


    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Account> accountCombo;
    @FXML private ComboBox<Category> categoryCombo;

    @FXML
    public void initialize() {
        datePicker.setEditable(false);
        datePicker.setValue(LocalDate.now());
    }

    public void putProperties(SessionFactory sessionFactory, MainController mainController) {
        this.sessionFactory = sessionFactory;
        this.mainController = mainController;

        categoryDAO = new DAO<>(sessionFactory, Category.class);
        accountDAO = new DAO<>(sessionFactory, Account.class);
        transactionDAO = new DAO<>(sessionFactory, Transaction.class);

        reloadData();
    }


    public void reloadData() {
        amountField.setText("");
        accountCombo.getItems().clear();
        accountCombo.getSelectionModel().clearSelection();
        categoryCombo.getItems().clear();
        categoryCombo.getSelectionModel().clearSelection();

        List<Account> accounts = accountDAO.findAll();
        List<Category> categories = categoryDAO.findAll();

        accountCombo.getItems().addAll(accounts);
        categoryCombo.getItems().addAll(categories);


    }

    @FXML
    public void handleCreateTransaction(ActionEvent actionEvent) {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            LocalDateTime ldt = LocalDateTime.of(datePicker.getValue(), LocalTime.now());
            Account account = accountCombo.getValue();
            Category category = categoryCombo.getValue();
            if (account == null || category == null) {
                throw new Exception();
            }
            if ((getBalanceForAccount(account).add(amount)).compareTo(BigDecimal.ZERO)  < 0) {
                Alert.ErrorAlert("Error", "there are insufficient funds in the account");
                return;
            }


            transactionDAO.save(new Transaction(account, category, amount, ldt));

            mainController.reloadClick(new ActionEvent());
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

        } catch (IllegalStateException e) {
            Alert.ErrorAlert("Error", "The transaction may not be in the future");

        } catch (Exception e) {
            Alert.ErrorAlert("Error", "Enter the correct information");
        }
    }

    private SQLException findSQLException(Throwable throwable) {
        while (throwable != null) {
            if (throwable instanceof SQLException) {
                return (SQLException) throwable;
            }
            throwable = throwable.getCause();
        }
        return null;
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public BigDecimal getBalanceForAccount(Account account) {
        try (Session session = sessionFactory.openSession()) {

            BigDecimal sum = session.createQuery(
                            "SELECT SUM(t.amount) FROM Transaction t WHERE t.account.id = :accountId",
                            BigDecimal.class
                    )
                    .setParameter("accountId", account.getId())
                    .uniqueResult();

            return sum != null ? sum : BigDecimal.ZERO;
        }  catch (Exception e) {
            throw new RuntimeException("Failed to get entities", e);
        }
    }

}