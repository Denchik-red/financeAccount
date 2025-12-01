package den_n.financeaccount.pages.AddCategory;

import den_n.financeaccount.Alert;
import den_n.financeaccount.DAO;
import den_n.financeaccount.module.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class AddCategoryDialogController {

    @FXML
    private TextField categoryNameField;

    private AddCategoryController addCategoryController;

    private SessionFactory sessionFactory;
    private DAO<Category> categoryDAO;


    public void closeBtnClick(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void saveCategoryBtnClick(ActionEvent actionEvent) {
        if (categoryNameField.getText().isBlank()) {
            System.out.println("name is empty");
            Alert.InfoAlert("name is empty");
            return;
        }
        try (Session session = sessionFactory.openSession()) {
            Category category = session
                    .createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                    .setParameter("name", categoryNameField.getText())
                    .uniqueResult();
            if (category != null) {
                System.out.println("this category already exists");
                Alert.InfoAlert("this category already exists");
                return;
            }
            categoryDAO.save(new Category(categoryNameField.getText()));

            categoryNameField.setText("");
            addCategoryController.loadCategories();
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    public void putProperties(SessionFactory sessionFactory, AddCategoryController addCategoryController) {
        this.sessionFactory = sessionFactory;
        this.addCategoryController = addCategoryController;

        categoryDAO = new DAO<>(sessionFactory, Category.class);
    }
}
