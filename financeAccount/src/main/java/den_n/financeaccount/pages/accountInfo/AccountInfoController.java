package den_n.financeaccount.pages.accountInfo;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.module.Transaction;
import den_n.financeaccount.pages.renameAccountDialog.RenameAccountDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountInfoController {
    @FXML
    private BarChart barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private ListView<Transaction> transactionsListView;
    @FXML
    private Label accountNameLabel;
    @FXML
    private Label balanceLabel;

    private Stage renameAccountDialogStage;

    private SessionFactory sessionFactory;
    private Account account;

    private DAO<Transaction> transactionDAO;

    public void putProperties(SessionFactory sessionFactory, Account account) {
        this.sessionFactory = sessionFactory;
        this.account = account;

        this.transactionDAO = new DAO<>(sessionFactory, Transaction.class);

        load();
    }

    public void load() {
        accountNameLabel.setText("Счёт: " + account.getName());


        BigDecimal balance = getBalanceForAccount(account);
        balanceLabel.setText("Баланс: " + balance.toString());

        transactionsListView.setCellFactory(param -> new TransactionInfoListCell());
        // Загрузите транзакции из сервиса/DAO и установите в список
        List<Transaction> transactionList = getTransactionsByAccount(account);
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(transactionList);
        transactionsListView.setItems(transactions);

        pieChart.getData().clear();

        Map<String, BigDecimal> totalByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
        totalByCategory.forEach((category, amount) -> {
            pieChart.getData().add(new PieChart.Data(category,  amount.doubleValue()));
        });

        LocalDate now = LocalDate.now();
        LocalDate start = now.minusDays(30);
        List<Object[]> transactionByDays = callGetTransactionsMonthly(sessionFactory, account.getName(), start, now);

        XYChart.Series<String, BigDecimal> series = new XYChart.Series<>();
        series.setName("Расходы за день");

        transactionByDays.forEach(param -> {
            System.out.println(param[0] + " : " + param[1]);
            BigDecimal amountNumber = (BigDecimal) param[1]; // ← Это безопасно, если СУБД возвращает число

            series.getData().add(new XYChart.Data<>(param[0].toString(), amountNumber));
        });
        barChart.getData().setAll(series);
    }

    private BigDecimal getBalanceForAccount(Account account) {
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
    private List<Transaction> getTransactionsByAccount(Account account) {
        try (Session session = sessionFactory.openSession()) {
           return session.createQuery("From Transaction t where t.account.id = :account_id", Transaction.class)
                    .setParameter("account_id", account.getId())
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get entities", e);
        }
    }

    public List<Object[]> callGetTransactionsMonthly(SessionFactory sessionFactory, String accountName, LocalDate startDate, LocalDate endDate) {
        String callFunctionSql = "SELECT * FROM getTransactionsMonthly(:accountName, :startDate, :endDate)";

        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(callFunctionSql)
                    .setParameter("accountName", accountName)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        }
    }

    public void onRenameButtonClicked(ActionEvent actionEvent) {

        if (renameAccountDialogStage != null && renameAccountDialogStage.isShowing()) {
            renameAccountDialogStage.requestFocus();
            renameAccountDialogStage.toFront();
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/renameAccountDialog-view.fxml"));
            Scene content = new Scene(fxmlLoader.load());
            RenameAccountDialogController renameAccountDialogController = fxmlLoader.getController();
            content.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/addAccount-view.css").toExternalForm());
            renameAccountDialogStage = new Stage();
            renameAccountDialogStage.setTitle("Новая категория");
            renameAccountDialogStage.setMinWidth(600);
            renameAccountDialogStage.setMinHeight(300);
            renameAccountDialogStage.setScene(content);

            Node node = (Node) actionEvent.getSource();
            Stage mainStage = (Stage) node.getScene().getWindow();

            renameAccountDialogStage.initOwner(mainStage);

            renameAccountDialogController.putProperties(sessionFactory, this, account);

            renameAccountDialogStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            Alert.ErrorAlert("Erorr", e.getMessage());
        }
    }
}
