package org.openjfx.lab2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openjfx.lab2.controller.LoginController;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;

/**
 * TestFX unit tests for the Lab 2 login UI.
 *
 * The login screen is loaded directly from login.fxml. A single known user is
 * injected into the controller so the tests do not depend on users.txt, which
 * keeps the valid/invalid login cases deterministic.
 *
 * @authors Waseem Sheety, Adam Karain
 */
@ExtendWith(ApplicationExtension.class)
class AppTest {

    private static final String VALID_USER = "test@mail.com";
    private static final String VALID_PASS = "Abcd123!";

    /** Builds the login scene and injects a known user before each test. */
    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
        Parent root = loader.load();

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(VALID_USER, VALID_PASS));

        LoginController controller = loader.getController();
        controller.setUsers(users);
        controller.setStage(stage);

        stage.setScene(new Scene(root, 320, 240));
        stage.show();
    }

    @Test
    void fields_are_empty_on_start(FxRobot robot) {
        FxAssert.verifyThat("#username", TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText(""));
    }

    @Test
    void login_button_has_correct_label(FxRobot robot) {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("login"));
    }

    @Test
    void invalid_credentials_show_error_and_clear_password(FxRobot robot) {
        robot.clickOn("#username").write("wrong@mail.com");
        robot.clickOn("#password").write("Wrong123!");
        robot.clickOn(".button");
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("user or password do not match"));
        FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    }

    @Test
    void valid_credentials_switch_to_welcome_screen(FxRobot robot) {
        robot.clickOn("#username").write(VALID_USER);
        robot.clickOn("#password").write(VALID_PASS);
        robot.clickOn(".button");
        WaitForAsyncUtils.waitForFxEvents();

        FxAssert.verifyThat("#welcomeLabel", LabeledMatchers.hasText("Welcome to the GCM system!"));
    }
}
