package Web.tests;

import Web.pages.RegistrationPage;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    RegistrationPage registrationPage = new RegistrationPage();

    @BeforeAll
    static void browserSetting() {

        Configuration.browserSize="1920x1080";

}
}
