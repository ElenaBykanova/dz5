package pages;


import data.CommunicationMethodData;
import data.EnglishLanguageData;
import data.FieldNameData;
import data.WorkScheduleData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import data.cities.ICityData;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.sql.Driver;
import java.util.Random;


public class PersonProfilePage extends AbsBasePage {

    private final Logger logger = LogManager.getLogger(PersonProfilePage.class);


    public PersonProfilePage(WebDriver driver) {
        super(driver);
    }

    public PersonProfilePage checkThatPersonalDataSectionIsOpen() {
        WebElement headTitleEl = driver.findElement(By.cssSelector("h3.text"));
        Assertions.assertEquals("Персональные данные", headTitleEl.getText());
        logger.info("Проверили, что перешли в раздел Персональные данные");

        return this;

    }

    public PersonProfilePage clearAndInputPersonalData(FieldNameData fieldNameData, String inputData) {
        WebElement inputEl = driver.findElement(By.cssSelector(String.format("input[name='%s']", fieldNameData.getName())));
        inputEl.clear();
        inputEl.sendKeys(inputData);
        logger.info("Заполнили персональные данные");

        return this;
    }



    public PersonProfilePage selectCuntryAndCity(ICityData cityData) throws InterruptedException {

        //тут кликаем в поле со страной - блок div
        WebElement selectCountryElement = driver.findElement(By.xpath("//input[@name='country']/following-sibling::div"));
        selectCountryElement.click();

        //тут выбираем элемент из списка стран
        WebElement countryItemEl = driver.findElement(By.cssSelector(String.format("[title='%s']", cityData.getCuntriesData().getName())));
        countryItemEl.click();


        ////input[@name='city' and @disabled='disabled']
        //тут описано 2 элемента
        //блок div - поле выбора города
        //элемент input - он меняет значение с disabled на НЕ disabled
        WebElement selectCityElement = driver.findElement(By.xpath("//input[@name='city']/following-sibling::div"));
        WebElement selectCityInputElement = driver.findElement(By.xpath("//input[@name='city']"));
        //тут 2 ожидания
        //ждем когда блок div станет кликабельным - он всегда кликабелен. Ожидание не работает(
        //ждем когда элемент input не будет содержать disabled - эта проверка всегда true, даже когда disabled присутствует. Ожидание не работает(
        //с Thread.sleep работает
        waitTools.waitForCondition(ExpectedConditions.elementToBeClickable(selectCityElement));
        waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.attributeToBe(selectCityInputElement, "disabled", "disabled")));
        Thread.sleep(2000);
        selectCityElement.click();

        //тут нужно дождаться когда откроется список
        WebElement cityWebContainer = driver.findElement(By.xpath("//input[@name='city']/../following-sibling::div"));
        waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.attributeContains(cityWebContainer, "class", "hide")));

        //тут выбираем значение из списка
        WebElement cityItemEl = driver.findElement(By.cssSelector(String.format("[title='%s']", cityData.getName())));
        cityItemEl.click();

        //тут ждем когда список закроется
        waitTools.waitForCondition(ExpectedConditions.attributeContains(cityWebContainer, "class", "hide"));


        return this;
    }

    public PersonProfilePage selectEnglishLevel(EnglishLanguageData englishLanguageData) {

        //тут кликаем на выпадашку для выбора уровня языка
        WebElement selectEnglishLevelElement = driver.findElement(By.xpath("//input[@name='english_level']/following-sibling::div"));
        selectEnglishLevelElement.click();

        //тут нужно дождаться когда откроется список
        WebElement englishLevelContainer = driver.findElement(By.xpath("//input[@name='english_level']/../following-sibling::div"));
        waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.attributeContains(englishLevelContainer, "class", "hide")));

        //тут выбираем элемент из списка уровень английского
        WebElement englishLevelItemElement = driver.findElement(By.cssSelector(String.format("[title='%s']", englishLanguageData.getName())));
        englishLevelItemElement.click();

        //тут ждем когда список закроется
        waitTools.waitForCondition(ExpectedConditions.attributeContains(englishLevelContainer, "class", "hide"));

        return this;
    }

    public PersonProfilePage selectWorkSchedule(boolean isSelected, WorkScheduleData... workScheduleData) {

        for (WorkScheduleData workSchedule : workScheduleData) {
            WebElement workScheduleElementSpan = driver.findElement(By.xpath(String.format("//input[@title='%s']/following-sibling::span", workSchedule.getName())));
            WebElement workScheduleElementInput = driver.findElement(By.xpath(String.format("//input[@title='%s']", workSchedule.getName())));
            if (workScheduleElementInput.isSelected() != isSelected) {
                workScheduleElementSpan.click();
            }
        }

        return this;
    }

    public PersonProfilePage selectIsReadyForRelocate(boolean isSelected) {
        String isRelocate = isSelected ? "Да" : "Нет";
        driver.findElement(By.xpath(String.format("//span[contains(text(),'%s')]", isRelocate))).click();

        return this;
    }

    public PersonProfilePage selectRandomSex() {
        Random random = new Random();
        boolean b = random.nextBoolean();
        String sex = b ? "Женский" : "Мужской";

        //тут кликаем на выпадашку для выбора пола
        WebElement selectSexElement = driver.findElement(By.cssSelector("[name='gender']"));
        selectSexElement.click();

        //выбираем нужное значение
        driver.findElement(By.xpath(String.format("//select//option[.='%s']", sex))).click();

        return this;
    }

    public void saveAndContinue() {
        driver.findElement(By.cssSelector("[name='continue']")).click();
    }



    public PersonProfilePage checkFieldsDataIsNotEmpty(FieldNameData fieldNameData) {
        Assertions.assertFalse(driver.findElement(By.cssSelector(String.format("input[name='%s']", fieldNameData.getName()))).getAttribute("value").isEmpty());

        return this;

    }

    public PersonProfilePage checkDropDownListIsNotEmpty(FieldNameData fieldNameData) {
        Assertions.assertFalse(driver.findElement(By.xpath(String.format("//input[@name='%s']/following-sibling::div", fieldNameData.getName()))).getText().isEmpty());

        return this;

    }

        public PersonProfilePage checkIsReadyForRelocate(boolean isSelected){
            String isRelocate = isSelected ? "1" : "0";
            Assertions.assertTrue(driver.findElement(By.xpath(String.format("//input[@id='id_ready_to_relocate_%s']", isRelocate))).isSelected());
            return this;
        }

    public PersonProfilePage checkWorkScheduleIsSelected(WorkScheduleData... workScheduleData) {
        for (WorkScheduleData workSchedule : workScheduleData) {
            Assertions.assertTrue(driver.findElement(By.xpath(String.format("//input[@title='%s']", workSchedule.getName()))).isSelected());
        }

        return this;
    }

    }

