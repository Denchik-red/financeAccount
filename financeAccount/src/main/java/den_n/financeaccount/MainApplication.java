package den_n.financeaccount;

import den_n.financeaccount.pages.AddCategory.AddCategoryController;
import den_n.financeaccount.pages.AddCategory.AddCategoryDialogController;
import den_n.financeaccount.pages.main.MainController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.io.IOException;

public class MainApplication extends Application {

    SessionFactory sessionFactory;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("connection to the database failed"); //message
        }

        // Главное окно приложения main-view
        FXMLLoader main_fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/den_n/financeaccount/fxml/main-view.fxml"));
        Scene main_scene = new Scene(main_fxmlLoader.load(), 320, 240);
        MainController mainController = main_fxmlLoader.getController();
        main_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/main-view.css").toExternalForm());
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setTitle("Hello!");
        stage.setScene(main_scene);
        stage.show();

        // Окно добавления новой категории addCategory-view
        FXMLLoader addCategory_fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/den_n/financeaccount/fxml/addCategory-view.fxml"));
        Scene addCategory_scene = new Scene(addCategory_fxmlLoader.load(), 400, 300);
        AddCategoryController addCategoryController = addCategory_fxmlLoader.getController();
        addCategory_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/addCategory-view.css").toExternalForm());
        Stage addCategoryStage = new Stage();
        addCategoryStage.setWidth(400);
        addCategoryStage.setHeight(600);
        addCategoryStage.setMinWidth(400);
        addCategoryStage.setMinHeight(400);
        addCategoryStage.setTitle("Категории");
        addCategoryStage.setScene(addCategory_scene);
        addCategoryStage.initOwner(stage);
        addCategoryStage.show();

        FXMLLoader fxmlLoaderOrders = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/addCategoryDialog-view.fxml"));
        VBox content = fxmlLoaderOrders.load();
        AddCategoryDialogController addCategoryDialogController = fxmlLoaderOrders.getController();
        Dialog<Void> addCategoryDialog = new Dialog<>();
        addCategoryDialog.setTitle("Новая категория");
        addCategoryDialog.setWidth(600);
        addCategoryDialog.setHeight(300);
        addCategoryDialog.getDialogPane().setContent(content);
        Stage addCategoryDialogStage = (Stage) addCategoryDialog.getDialogPane().getScene().getWindow();
        addCategoryDialogStage.setResizable(true);
        addCategoryDialogStage.setOnCloseRequest(evt -> {
            addCategoryDialogStage.close();
        });
        addCategoryDialogStage.show();

        //передача переменных окнам
        mainController.putProperties(sessionFactory, addCategoryStage, addCategoryController);
        addCategoryController.putProperties(sessionFactory);

    }
}
