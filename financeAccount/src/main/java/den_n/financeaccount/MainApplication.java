package den_n.financeaccount;

import den_n.financeaccount.pages.AddCategory.AddCategoryController;
import den_n.financeaccount.pages.AddCategory.AddCategoryDialogController;
import den_n.financeaccount.pages.newTransaction.NewTransactionController;
import den_n.financeaccount.pages.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        stage.setMinHeight(450);
        stage.setMinWidth(500);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setTitle("Hello!");
        stage.setScene(main_scene);
        stage.show();

        // Окно просмотра категорий addCategory-view
        FXMLLoader addCategory_fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/den_n/financeaccount/fxml/addCategory-view.fxml"));
        Scene addCategory_scene = new Scene(addCategory_fxmlLoader.load(), 400, 300);
        AddCategoryController addCategoryController = addCategory_fxmlLoader.getController();
        addCategory_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/addCategory-view.css").toExternalForm());
        Stage addCategoryStage = new Stage();
        addCategoryStage.setMinHeight(500);
        addCategoryStage.setMinWidth(400);
        addCategoryStage.setTitle("Категории");
        addCategoryStage.setScene(addCategory_scene);
        addCategoryStage.initOwner(stage);
//        addCategoryStage.show();

        // Окно добавления категории addCategoryDialog-view
        FXMLLoader addCategoryDialog_fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/addCategoryDialog-view.fxml"));
        Scene addCategoryDialog_scene = new Scene(addCategoryDialog_fxmlLoader.load());
        AddCategoryDialogController addCategoryDialogController = addCategoryDialog_fxmlLoader.getController();
        addCategoryDialog_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/addCategoryDialog-view.css").toExternalForm());
        Stage addCategoryDialogStage = new Stage();
        addCategoryDialogStage.setTitle("Новая категория");
        addCategoryDialogStage.setMinWidth(400);
        addCategoryDialogStage.setMinHeight(250);
        addCategoryDialogStage.setScene(addCategoryDialog_scene);
        addCategoryDialogStage.initOwner(addCategoryStage);
//        addCategoryDialogStage.show();

        // Окно создания транзакции newTransaction-view
        FXMLLoader newTransaction_fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/newTransaction-view.fxml"));
        Scene newTransaction_scene = new Scene(newTransaction_fxmlLoader.load());
        NewTransactionController newTransactionController = newTransaction_fxmlLoader.getController();
        newTransaction_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/newTransaction-view.css").toExternalForm());
        Stage newTransactionStage = new Stage();
        newTransactionStage.setTitle("Новая транзакция");
        newTransactionStage.setMinWidth(400);
        newTransactionStage.setMinHeight(635);
        newTransactionStage.setScene(newTransaction_scene);
        newTransactionStage.initOwner(stage);
//        newTransactionStage.show();

        //передача переменных окнам
        mainController.putProperties(sessionFactory, addCategoryStage, addCategoryController, newTransactionStage);
        addCategoryController.putProperties(sessionFactory, addCategoryDialogStage);
        addCategoryDialogController.putProperties(sessionFactory, addCategoryController);
        newTransactionController.putProperties(sessionFactory);

    }
}
