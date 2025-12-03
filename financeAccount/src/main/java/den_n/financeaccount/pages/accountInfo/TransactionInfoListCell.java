// TransactionInfoListCell.java
package den_n.financeaccount.pages.accountInfo;

import den_n.financeaccount.module.Transaction;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class TransactionInfoListCell extends ListCell<Transaction> {

    private final HBox container = new HBox();
    private final Label amountLabel = new Label();
    private final Label categoryLabel = new Label();
    private final Label dateLabel = new Label();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TransactionInfoListCell() {
        container.setSpacing(15);
        container.setPadding(new Insets(10));
        container.getStyleClass().add("transaction-cell");

        VBox infoBox = new VBox(4);
        infoBox.getChildren().addAll(categoryLabel, dateLabel);
        infoBox.getStyleClass().add("transaction-info");

        container.getChildren().addAll(amountLabel, infoBox);
    }

    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
            setGraphic(null);
            setText(null);
        } else {
            // Форматирование суммы с учётом знака
            String amountText = transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0
                    ? "+ " + transaction.getAmount()
                    : "- " + transaction.getAmount().abs();
            amountLabel.setText(amountText);

            String categoryText = (transaction.getCategory() != null)
                    ? transaction.getCategory().getName()
                    : "Без категории";
            categoryLabel.setText(categoryText);

            String dateText = (transaction.getCreatedAt() != null)
                    ? transaction.getCreatedAt().format(formatter)
                    : "—";
            dateLabel.setText(dateText);

            amountLabel.getStyleClass().removeAll("income", "expense");
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                amountLabel.getStyleClass().add("income");
            } else {
                amountLabel.getStyleClass().add("expense");
            }
            setGraphic(container);
            setText(null); // Важно: используем только графику
        }
    }
}