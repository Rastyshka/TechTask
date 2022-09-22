package Api;

import TestBase.TestBaseApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ApiTests extends TestBaseApi {

    @Test
    @Tag("Api")
    @DisplayName("Получение спиcка пользователей")
    void getListUsers() {

        step("Получение спиcка пользователей", () -> {

                    given()
                            .spec(requestSpecification())
                            .when()
                            .get("api/users?page=2")
                            .then()
                            .spec(responseSpecificationScOk())
                            .body("page", is(2), "per_page", is(6),
                                    "total", is(12), "total_pages", is(2),
                                    "data[0].id", is(7), "data[0].email", is("michael.lawson@reqres.in"),
                                    "data[0].first_name", is("Michael"), "data[0].last_name", is("Lawson"),
                                    "data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Получение пустого спиcка пользователей по несуществующей странице")
    void getVoidListUsers() {

        step("Получение пустого спиcка пользователей по несуществующей странице", () -> {

                    given()
                            .spec(requestSpecification())
                            .when()
                            .get("api/users?page=88")
                            .then()
                            .body("page", is(88), "per_page", is(6),
                                    "total", is(12), "total_pages", is(2),
                                    "data", is(empty()));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Проверка полей")
    void getSingleUserInfo() {

        step("Проверка полей", () -> {

                    given()
                            .spec(requestSpecification())
                            .when()
                            .get("api/users/2")
                            .then()
                            .spec(responseSpecificationScOk())
                            .assertThat()
                            .body(matchesJsonSchemaInClasspath("UserInfoSchema.json"));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Проверка пустого юзера")
    void getSingleUserNotFound() {

        step("Проверка пустого юзера", () -> {

                    given()
                            .spec(requestSpecification())
                            .when()
                            .get("api/users/23")
                            .then()
                            .spec(responseSpecificationScNotFound());
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Удачная регистрация")
    void postRegisterSuccessful() {

        File successfullRegister = new File("src/test/resources/RegisterSuccessful.json");

        step("Удачная регистрация", () -> {

                    given()
                            .spec(requestSpecification())
                            .body(successfullRegister)
                            .when()
                            .post("api/register")
                            .then()
                            .spec(responseSpecificationScOk()
                                    .body("id", is(4), "token", is("QpwL5tke4Pnpja7X4")));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Неудачная регистрация")
    void postRegisterUnsuccessful() {

        File unsuccessfullRegister = new File("src/test/resources/RegisterUnsuccessful.json");

        step("Удачная регистрация", () -> {

                    given()
                            .spec(requestSpecification())
                            .body(unsuccessfullRegister)
                            .when()
                            .post("api/register")
                            .then()
                            .statusCode(400)
                            .body("error", is("Missing password"));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Изменение данных пользователя")
    void updateUser() {

        File updateUser = new File("src/test/resources/Updateuser.json");

        step("Изменение данных пользователя", () -> {

                    given()
                            .spec(requestSpecification())
                            .body(updateUser)
                            .when()
                            .put("api/users/2")
                            .then()
                            .spec(responseSpecificationScOk())
                            .body("name", is("morpheus"),
                                    "job", is("zion resident"));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Изменение данных пользователя без имени")
    void updateUserWithOutName() {

        File updateUser = new File("src/test/resources/UpdateuserWithOutName.json");

        step("Изменение данных пользователя без имени", () -> {

                    given()
                            .spec(requestSpecification())
                            .body(updateUser)
                            .when()
                            .put("api/users/2")
                            .then()
                            .spec(responseSpecificationScOk())
                            .body("job", is("driver"));
                }
        );
    }

    @Test
    @Tag("Api")
    @DisplayName("Удаление пользователя")
    void deleteUser () {

        step("Удаление пользователя", () -> {

                    given()
                            .spec(requestSpecification())
                            .when()
                            .delete("api/users/2")
                            .then()
                            .log().all()
                            .statusCode(204);
                }
        );
    }

}
