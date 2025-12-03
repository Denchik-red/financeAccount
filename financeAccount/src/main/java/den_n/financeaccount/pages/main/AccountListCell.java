package den_n.financeaccount.pages.main;

import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Account;
import den_n.financeaccount.module.Transaction;
import den_n.financeaccount.pages.accountInfo.AccountInfoController;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountListCell extends ListCell<Account> {

    private Stage accoutInfo_stage;
    private AccountInfoController accountInfoController;

    private final HBox card;
    private final VBox leftBox;
    private final Label nameLabel;
    private final Label balanceLabel;
    private final Button monthlyButton;

    private SessionFactory sessionFactory;

    public AccountListCell() {
        nameLabel = new Label();
        nameLabel.getStyleClass().add("card-label");

        balanceLabel = new Label();
        balanceLabel.getStyleClass().add("card-amount");

        leftBox = new VBox(5, nameLabel, balanceLabel);
        leftBox.setPadding(new Insets(15, 0, 15, 15));
        leftBox.getStyleClass().add("left-box");

        monthlyButton = new Button("Monthly →");
        monthlyButton.getStyleClass().add("btn-small");

        HBox rightBox = new HBox(monthlyButton);
        rightBox.setPadding(new Insets(15, 15, 15, 0));
        rightBox.getStyleClass().add("right-box");

        card = new HBox(leftBox, rightBox);
        card.getStyleClass().add("card");
        HBox.setHgrow(leftBox, Priority.ALWAYS); // левая часть растягивается

        monthlyButton.setOnAction(e -> {
            Account account = getItem();
            if (account != null) {
                System.out.println("Monthly clicked for: " + account.getName());
                accountInfoController.putProperties(sessionFactory, account);
                accoutInfo_stage.show();
            }
        });
    }

    @Override
    protected void updateItem(Account account, boolean empty) {
        super.updateItem(account, empty);

        if (empty || account == null) {
            setGraphic(null);
        } else {

            nameLabel.setText(account.getName());
            BigDecimal balance = getBalanceForAccount(account);
            System.out.println(balance);
            balanceLabel.setText(balance.toString());

            // 🔸 Опционально: задаём градиент в зависимости от типа счёта
            if ((balance.compareTo(BigDecimal.valueOf(100.0))) < 0) {
                card.getStyleClass().add("credit");
            } else if ((balance.compareTo(BigDecimal.valueOf(1000.0))) < 0) {
                card.getStyleClass().add("investment");
            } else {
                card.getStyleClass().add("savings");
            }

            setGraphic(card);
        }
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

    public void putProperties(SessionFactory sessionFactory, Stage accoutInfo_stage, AccountInfoController accountInfoController) {
        this.sessionFactory = sessionFactory;
        this.accoutInfo_stage = accoutInfo_stage;
        this.accountInfoController = accountInfoController;
    }
}