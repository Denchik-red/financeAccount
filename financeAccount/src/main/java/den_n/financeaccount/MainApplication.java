package den_n.financeaccount;

import den_n.financeaccount.pages.AddCategory.AddCategoryController;
import den_n.financeaccount.pages.AddCategory.AddCategoryDialogController;
import den_n.financeaccount.pages.accountInfo.AccountInfoController;
import den_n.financeaccount.pages.connecrionSettings.ConnectionSettingsController;
import den_n.financeaccount.pages.newTransaction.NewTransactionController;
import den_n.financeaccount.pages.main.MainController;
import den_n.financeaccount.util.ConnectionPreferences;
import den_n.financeaccount.util.DatabaseConfigUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.io.IOException;

public class MainApplication extends Application {

    SessionFactory sessionFactory;

    @Override
    public void start(Stage stage) throws IOException {

        //окно настроек подключения
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/connectionSettings-view.fxml"));
        Scene content = new Scene(fxmlLoader.load());
        ConnectionSettingsController connectionSettingsController = fxmlLoader.getController();
        content.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/connectionSettings.css").toExternalForm());
        Stage connectionSettingsStage = new Stage();
        connectionSettingsStage.setTitle("Connection Settings");
        connectionSettingsStage.setMinWidth(450);
        connectionSettingsStage.setMinHeight(680);
        connectionSettingsStage.setScene(content);

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + ConnectionPreferences.getHost() + ":" + ConnectionPreferences.getPort() + "/" + ConnectionPreferences.getDatabase();
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, ConnectionPreferences.getUsername(), ConnectionPreferences.getPassword());
            conn.close();
        } catch (Exception e) {
            Alert.ErrorAlert("Connection Failed", "Could not connect to database: " + e.getMessage());
            connectionSettingsStage.showAndWait();
            System.exit(0);
        }

        try {
            sessionFactory = DatabaseConfigUtil.createSessionFactory();
        } catch (Exception e) {
            System.out.println("connection to the database failed: " + e.getMessage());
            Alert.ErrorAlert("Connection to database failed", e.getMessage());
        }

        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Long count = session.createQuery("SELECT COUNT(c) FROM Category c", Long.class).uniqueResult();
            boolean categoriesExist = count != null && count > 0;


            String standardCategoriesInsert = """
                    INSERT INTO categories (name) VALUES
                    ('Продукты'),
                    ('Транспорт'),
                    ('Жильё (аренда, ипотека)'),
                    ('Коммунальные услуги'),
                    ('Рестораны и кафе'),
                    ('Развлечения'),
                    ('Одежда и обувь'),
                    ('Здоровье и медицина'),
                    ('Образование'),
                    ('Переводы'),
                    ('Зарплата'),
                    ('Прочие доходы'),
                    ('Прочие расходы');""";

            String createTrigger = """
                    CREATE OR REPLACE FUNCTION validate_transaction_amount()
                    RETURNS TRIGGER AS $$
                    BEGIN
                        IF NEW.created_at > LOCALTIMESTAMP THEN
                            RAISE EXCEPTION 'Transaction may not be in the future' USING ERRCODE = '22000';
                        END IF;
                        RETURN NEW;
                    END;
                    $$ LANGUAGE plpgsql;
                    DROP TRIGGER IF EXISTS validate_transaction_amount_trigger ON transactions;
                    CREATE TRIGGER validate_transaction_amount_trigger
                        BEFORE INSERT OR UPDATE ON transactions
                        FOR EACH ROW EXECUTE FUNCTION validate_transaction_amount();
                """;

            String createFunctionSql = """
                CREATE OR REPLACE FUNCTION getTransactionsMonthly(
                    p_account_name VARCHAR,
                    p_start_date DATE,
                    p_end_date DATE
                )
                RETURNS TABLE (
                    date DATE,
                    amount NUMERIC(19, 2)
                ) AS $$
                BEGIN
                    RETURN QUERY
                    SELECT 
                        CAST(t.created_at AS DATE),
                        SUM(t.amount)
                    FROM transactions t
                    JOIN accounts a ON t.account_id = a.id
                    WHERE a.name = p_account_name
                      AND CAST(t.created_at AS DATE) BETWEEN p_start_date AND p_end_date
                    GROUP BY CAST(t.created_at AS DATE)
                    ORDER BY CAST(t.created_at AS DATE);
                END;
                $$ LANGUAGE plpgsql;
            """;
            session.createNativeQuery(createFunctionSql).executeUpdate();

            session.createNativeQuery(createTrigger).executeUpdate();

            if (!categoriesExist) {
                session.createNativeQuery(standardCategoriesInsert).executeUpdate();
            }
            transaction.commit();

        } catch (Exception e) {
            System.out.println("init database structure failed: " + e.getMessage());
            Alert.ErrorAlert("Init database structure failed", e.getMessage());
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
        stage.setOnCloseRequest(evt -> {
            boolean confirmClose = Alert.ConfirmAlert("Close app");
            if (!confirmClose) {
                evt.consume();
            }
        });
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

        // Окно меню транзакции
        FXMLLoader accauntInfo_fxmlLoader = new FXMLLoader(getClass().getResource("/den_n/financeaccount/fxml/accountInfo-view.fxml"));
        Scene accountInfo_scene = new Scene(accauntInfo_fxmlLoader.load());
        AccountInfoController accountInfoController = accauntInfo_fxmlLoader.getController();
        accountInfo_scene.getStylesheets().add(getClass().getResource("/den_n/financeaccount/css/accountInfo.css").toExternalForm());
        Stage accoutInfo_stage = new Stage();
        accoutInfo_stage.setTitle("Счет");
        accoutInfo_stage.setMinWidth(500);
        accoutInfo_stage.setMinHeight(635);
        accoutInfo_stage.setScene(accountInfo_scene);
        accoutInfo_stage.initOwner(stage);
//        accaoutInfo_stage.show();




        //передача переменных окнам
        mainController.putProperties(sessionFactory, addCategoryStage, addCategoryController, newTransactionStage, connectionSettingsStage, accoutInfo_stage, newTransactionController, accountInfoController);
        addCategoryController.putProperties(sessionFactory, addCategoryDialogStage);
        addCategoryDialogController.putProperties(sessionFactory, addCategoryController);
        newTransactionController.putProperties(sessionFactory, mainController);

    }
}
