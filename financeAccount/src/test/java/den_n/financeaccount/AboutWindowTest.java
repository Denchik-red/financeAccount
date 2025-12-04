package den_n.financeaccount;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
public class AboutWindowTest {

    @Start
    private void start(Stage stage) throws Exception {
        new MainApplication().start(stage);
    }

    @Test
    void testSettingsConnection(FxRobot robot) {
        robot.sleep(1000);
        robot.clickOn("#connectionSettings");
        robot.sleep(1000);
        robot.clickOn("#hostField");
        robot.sleep(1000);
        TextField tf = robot.lookup("#hostField").query();
        tf.clear();
        robot.write("TestAddress");
        robot.sleep(1000);

        String value = robot.lookup("#hostField").queryTextInputControl().getText();
        assertEquals("TestAddress", value);
    }

    @Test
    void testAddAccount(FxRobot robot) {
        robot.sleep(1000);
        robot.clickOn("#addAccount");
        robot.sleep(1000);
        robot.clickOn("#accountNameField");
        robot.sleep(1000);
        TextField tf = robot.lookup("#accountNameField").query();
        tf.clear();
        robot.write("TestAccount");
        robot.sleep(1000);
        String value = robot.lookup("#accountNameField").queryTextInputControl().getText();
        assertEquals("TestAccount", value);
    }

    @Test
    void testAddCategory(FxRobot robot) {
        robot.sleep(1000);
        robot.clickOn("#addCategory");
        robot.sleep(1000);
        robot.clickOn("#addNewCategory");

        robot.clickOn("#categoryNameField");
        robot.sleep(1000);
        TextField tf = robot.lookup("#categoryNameField").query();
        tf.clear();
        robot.write("TestCategory");
        robot.sleep(1000);
        String value = robot.lookup("#categoryNameField").queryTextInputControl().getText();
        assertEquals("TestCategory", value);

    }

    @Test
    void testNewTransaction(FxRobot robot) {
        robot.sleep(1000);
        robot.clickOn("#newTransaction");
        robot.sleep(1000);
        robot.clickOn("#amountField");
        robot.sleep(1000);
        TextField tf = robot.lookup("#amountField").query();
        tf.clear();
        robot.write("1000");
        robot.sleep(1000);
        String value = robot.lookup("#amountField").queryTextInputControl().getText();
        assertEquals("1000", value);
    }
}
