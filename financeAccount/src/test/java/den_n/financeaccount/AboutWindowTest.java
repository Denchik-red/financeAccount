package den_n.financeaccount;

import den_n.financeaccount.module.Account;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AboutWindowTest {

    @Start
    private void start(Stage stage) throws Exception {
        new MainApplication().start(stage);
    }

    @Test
    void testSettingsConnection(FxRobot robot) {
        robot.sleep(500);
        robot.clickOn("#connectionSettings");
        robot.sleep(500);
        robot.clickOn("#hostField");
        robot.sleep(500);
        TextField tf = robot.lookup("#hostField").query();
        tf.clear();
        robot.write("TestAddress");
        robot.sleep(500);

        String value = robot.lookup("#hostField").queryTextInputControl().getText();
        assertEquals("TestAddress", value);
    }

    @Test
    void testAddAccountAndVerifyInList(FxRobot robot) {
        robot.sleep(500);
        robot.clickOn("#addAccount");

        robot.clickOn("#accountNameField");
        robot.write("TestAccount11");

        robot.clickOn("#saveAccountBtn");
        robot.sleep(500);

        ListView<?> accountListView = (ListView<?>) robot.lookup("#accountListView").query();
        List<?> items = accountListView.getItems();

        boolean found = items.stream()
                .anyMatch(item -> {
                    if (item instanceof Account) {
                        return "TestAccount11".equals(((Account) item).getName());
                    }
                    return false;
                });
        assertTrue(found, "Счёт 'TestAccount' не добавлен в список!");
    }

    @Test
    void testAddCategory(FxRobot robot) {
        robot.sleep(500);
        robot.clickOn("#addCategory");
        robot.sleep(500);
        robot.clickOn("#addNewCategory");

        robot.clickOn("#categoryNameField");
        robot.sleep(500);
        TextField tf = robot.lookup("#categoryNameField").query();
        tf.clear();
        robot.write("TestCategory");
        robot.sleep(500);
        String value = robot.lookup("#categoryNameField").queryTextInputControl().getText();
        assertEquals("TestCategory", value);

    }

    @Test
    void testNewTransaction(FxRobot robot) {
        robot.sleep(500);
        robot.clickOn("#newTransaction");
        robot.sleep(500);
        robot.clickOn("#amountField");
        robot.sleep(500);
        TextField tf = robot.lookup("#amountField").query();
        tf.clear();
        robot.write("1000");
        robot.sleep(500);
        String value = robot.lookup("#amountField").queryTextInputControl().getText();
        assertEquals("1000", value);
    }
}
