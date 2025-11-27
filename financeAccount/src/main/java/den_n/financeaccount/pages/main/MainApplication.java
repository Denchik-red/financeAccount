package den_n.financeaccount.pages.main;

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

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/den_n/financeaccount/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        MainController mainController = fxmlLoader.getController();
        mainController.putProperties(sessionFactory);
        scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/main-view.css").toExternalForm());
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


//        DAO<Account> accountDAO = new DAO<>(sessionFactory, Account.class);



    }
}
