package sberTest;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CardFormSimpleTest extends Inits {
    @Test
    @DisplayName("Simple test")
    void testForm(){
        String baseUrl = "https://www.sberbank.ru/ru/person";
        driver.get(baseUrl);

        //Нажать на меню – Карты
        String sberCardsButtonXPath = "//a[@aria-label='Карты']";
        WebElement sberCardsButtonButton = driver.findElement(By.xpath(sberCardsButtonXPath));
        sberCardsButtonButton.click();

        //Выбрать подменю – «Дебетовые карты»
        String sberDebitCardsButtonXPath = "//a[@data-cga_click_top_menu='Карты_Дебетовые карты_type_important']";
        WebElement sberDebitCardsButton = driver.findElement(By.xpath(sberDebitCardsButtonXPath));
        sberDebitCardsButton.click();

        //Проверить наличие на странице заголовка – «Дебетовые карты»
        String sberDebitCardsHeaderXPath = "//h1[text() ='Дебетовые карты']";
        WebElement sberDebitCardsHeader = driver.findElement(By.xpath(sberDebitCardsHeaderXPath));
        Assertions.assertEquals("Дебетовые карты", sberDebitCardsHeader.getText(),
                "Заголовок отсутствует/не соответствует требуемому (Дебетовые карты)");

        //Под заголовком из представленных карт найти “Молодёжная карта” и кликнуть на кнопку данной карты “Заказать онлайн”
        String sberOrderCardButtonXPath = "//a[@data-product='Молодёжная карта' and @data-test-id='ProductCatalog_button']";
        WebElement sberOrderCardButton = driver.findElement(By.xpath(sberOrderCardButtonXPath));
        sberOrderCardButton.click();

        //Проверить наличие на странице заголовка – «Молодёжная карта»
        String sberYouthCardHeaderXPath = "//h1[text()='Молодёжная карта' and @data-test-id='PageTeaserDict_header']";
        WebElement sberYouthCardHeader = driver.findElement(By.xpath(sberYouthCardHeaderXPath));
        Assertions.assertEquals("Молодёжная карта", sberYouthCardHeader.getText(),
                "Заголовок отсутствует/не соответствует требуемому (Молодёжная карта)");

        try {//Ждём автоматической прокрутки (после того, как прошли по ссылке) вниз
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Кликнуть на кнопку «Оформить онлайн» под заголовком
        String sberIssueOnlineButtonXPath = "//a[@href='#order' and @data-test-id='PageTeaserDict_button']";
        WebElement sberIssueOnlineButton = driver.findElement(By.xpath(sberIssueOnlineButtonXPath));
        scrollToElementJs(sberIssueOnlineButton);
        try {//Ждём прокрутки обратно вверх
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sberIssueOnlineButton.click();

        try {//Ждём прокрутки вниз после клика на кнопку
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //В представленной форме заполнить поля + проверка на правильность заполнения
        String fieldXPath = "//input[@data-name='%s']";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "lastName"))), "Петров");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "firstName"))), "Пётр");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "middleName"))), "Петрович");
        WebElement sberDateInputField = driver.findElement(By.xpath(String.format(fieldXPath, "cardName")));
        sberDateInputField.sendKeys(Keys.CONTROL +"a", Keys.DELETE);
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "cardName"))), "PETER PETROV");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), "petrov@mail.mm");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), "1112223344",
                "+7 (111) 222-33-44");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), "01012000",
                "01.01.2000");

        //Нажать «Далее»
        String sberNextButtonXPath = "//button[contains(@class, 'odcui-button')]";
        WebElement sberNextButton = driver.findElement(By.xpath(sberNextButtonXPath));
        scrollToElementJs(sberNextButton);
        try {//Ждём прокрутки до кнопки "Далее"
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sberNextButton.click();

        //Проверить, что появилось сообщение именно у незаполненных полях – «Обязательное поле»
        Assertions.assertAll(
                () -> Assertions.assertEquals("Обязательное поле",
                        driver.findElement(By.xpath("//input[@data-name='series']/../div")).getText()),
                () -> Assertions.assertEquals("Обязательное поле",
                        driver.findElement(By.xpath("//input[@data-name='number']/../div")).getText()),
                () -> Assertions.assertEquals("Обязательное поле",
                        driver.findElement(By.xpath("//input[@id = 'odc-personal__issueDate']/../../../div[@class = " +
                                "'odcui-error__text']"))
                                .getText(), "Необходимое предупреждение не отображается у поля: Дата выдачи паспорта")
        );
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void fillInputField(WebElement element, String value) {
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assertions.assertEquals(value, element.getAttribute("value"), "Поле было заполнено некорректно");
    }
    private void fillInputField(WebElement element, String value, String expected) {
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assertions.assertEquals(expected, element.getAttribute("value"), "Поле было заполнено некорректно");
    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
