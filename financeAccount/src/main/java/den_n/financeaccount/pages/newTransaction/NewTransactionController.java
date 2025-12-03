package den_n.financeaccount.pages.newTransaction;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.module.Category;
import den_n.financeaccount.module.Transaction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
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



    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Account> accountCombo;
    @FXML private ComboBox<Category> categoryCombo;

    @FXML
    public void initialize() {
        datePicker.setEditable(false);
        datePicker.setValue(LocalDate.now());
    }

    public void putProperties(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

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


            transactionDAO.save(new Transaction(account, category, amount, ldt));

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

}