package den_n.financeaccount.pages.AddCategory;

import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class AddCategoryController {

    @FXML
    private ListView<Category> categoryListView;
    ObservableList<Category> categoryObservableList;

    private SessionFactory sessionFactory;
    private DAO<Category> categoryDAO;

    public void putProperties(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

         categoryDAO = new DAO<>(sessionFactory, Category.class);

        loadCategories();

        categoryListView.setCellFactory(param -> {
            CategoryListCell categoryListCell = new CategoryListCell();
            categoryListCell.putProperties(this, categoryDAO);
            return categoryListCell;
        });
    }

    public void addCategoryClick(ActionEvent actionEvent) throws IOException {
    }

    public void closeClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void loadCategories() {
        categoryObservableList = FXCollections.observableList(categoryDAO.findAll());
        categoryListView.setItems(categoryObservableList);
    }
}
