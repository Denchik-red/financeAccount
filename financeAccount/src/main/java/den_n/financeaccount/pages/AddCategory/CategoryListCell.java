package den_n.financeaccount.pages.AddCategory;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Category;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;

public class CategoryListCell extends ListCell<Category> {

    private AddCategoryController addCategoryController;
    private DAO<Category> categoryDAO;

    private final HBox card;
    private final Label nameLabel;
    private final Button deleteButton;
    private final Region spacer;

    public CategoryListCell() {
        nameLabel = new Label();
        nameLabel.getStyleClass().add("category_card-label");

        deleteButton = new Button();
        deleteButton.getStyleClass().add("category-delete-btn");

        Image image = new Image(getClass().getResource("/den_n/financeaccount/image/close_red.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);

        deleteButton.setGraphic(imageView);

        spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        card = new HBox(nameLabel, spacer, deleteButton);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("category_card");

        deleteButton.setOnAction(e -> {
            Category category = getItem();
            if (category != null && Alert.ConfirmAlert("delete category: " + category.getName())) {
                try {
                    categoryDAO.delete(category);
                    addCategoryController.loadCategories();
                } catch (ConstraintViolationException err) {
                    System.out.println("This category is specified in the transaction");
                    Alert.InfoAlert("This category is specified in the transaction");
                }
            }
        });
    }

    @Override
    protected void updateItem(Category account, boolean empty) {
        super.updateItem(account, empty);

        if (empty || account == null) {
            setGraphic(null);
        } else {

            nameLabel.setText(account.getName());
            setGraphic(card);
        }
    }

    public void putProperties(AddCategoryController addCategoryController, DAO<Category> categoryDAO) {
        this.addCategoryController = addCategoryController;
        this.categoryDAO = categoryDAO;
    }
}