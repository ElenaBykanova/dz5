import com.github.javafaker.Faker;
import components.ContactInfomationComponent;
import data.CommunicationMethodData;
import data.FieldNameData;
import data.WorkScheduleData;
import data.cities.ICityData;
import data.cities.RussiaCityData;
import data.EnglishLanguageData;
import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.PersonProfilePage;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PersonProfilePageTest {
    private WebDriver driver;

    private Faker ruFaker = new Faker(new Locale("ru"));
    private Faker enFaker = new Faker(new Locale("en"));

    private final Logger logger = LogManager.getLogger(PersonProfilePageTest.class);


    @BeforeEach
    public void init() {
        this.driver = new DriverFactory().create();
    }

    @AfterEach
    public void stopDriver(){
        if (driver != null){
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }


    @Test
    public void personProfilePageTest() throws InterruptedException {

        ICityData[] cityData = RussiaCityData.values();
        ICityData city = ruFaker.options().nextElement(cityData);

        EnglishLanguageData[] englishLanguageData = EnglishLanguageData.values();
        EnglishLanguageData englishLanguage = ruFaker.options().nextElement(englishLanguageData);

        CommunicationMethodData[] communicationMethodData = CommunicationMethodData.values();


        MainPage mainPage = new MainPage(driver);
        mainPage.open("/");
        mainPage
                .loginClick()
                .doAuth()
                .goToMyProfile();

        PersonProfilePage personProfilePage = new PersonProfilePage(driver);
        personProfilePage
                .checkThatPersonalDataSectionIsOpen()
                .clearAndInputPersonalData(FieldNameData.FNAME, ruFaker.name().firstName())
                .clearAndInputPersonalData(FieldNameData.FNAME_LATIN, enFaker.name().firstName())
                .clearAndInputPersonalData(FieldNameData.LNAME, ruFaker.name().lastName())
                .clearAndInputPersonalData(FieldNameData.LNAME_LATIN, enFaker.name().lastName())
                .clearAndInputPersonalData(FieldNameData.BLOG_NAME, ruFaker.funnyName().name())
                .clearAndInputPersonalData(FieldNameData.DATEOFBIRTH, ruFaker.date().birthday().toInstant().atZone(ZoneId.
                        systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .selectCuntryAndCity(city)
                .selectEnglishLevel(englishLanguage)
                .selectIsReadyForRelocate(true)
                .selectWorkSchedule(true, WorkScheduleData.FULL, WorkScheduleData.REMOTE);

        ContactInfomationComponent contactInfomationComponent = new ContactInfomationComponent(driver);
        contactInfomationComponent
                .selectCommunicationMethodAndInputName(0, ruFaker.options().nextElement(communicationMethodData), ruFaker.funnyName().name())
                .addAnotherCommunicationMethod()
                .selectCommunicationMethodAndInputName(1, ruFaker.options().nextElement(communicationMethodData), ruFaker.funnyName().name());

        personProfilePage
                .selectRandomSex()
                .clearAndInputPersonalData(FieldNameData.COMPANY, ruFaker.company().name())
                .clearAndInputPersonalData(FieldNameData.JOBTITLE, ruFaker.name().title())
                .saveAndContinue();

    }

    @Test
    public void checkPersonData(){
        MainPage mainPage = new MainPage(driver);
        mainPage.open("/");
        mainPage
                .loginClick()
                .doAuth()
                .goToMyProfile();

        PersonProfilePage personProfilePage = new PersonProfilePage(driver);
        personProfilePage
                .checkThatPersonalDataSectionIsOpen()
                .checkFieldsDataIsNotEmpty(FieldNameData.FNAME)
                .checkFieldsDataIsNotEmpty(FieldNameData.FNAME_LATIN)
                .checkFieldsDataIsNotEmpty(FieldNameData.LNAME)
                .checkFieldsDataIsNotEmpty(FieldNameData.LNAME_LATIN)
                .checkFieldsDataIsNotEmpty(FieldNameData.BLOG_NAME)
                .checkFieldsDataIsNotEmpty(FieldNameData.DATEOFBIRTH)
                .checkFieldsDataIsNotEmpty(FieldNameData.COMPANY)
                .checkFieldsDataIsNotEmpty(FieldNameData.JOBTITLE)
                .checkDropDownListIsNotEmpty(FieldNameData.COUNTRY)
                .checkDropDownListIsNotEmpty(FieldNameData.CITY)
                .checkDropDownListIsNotEmpty(FieldNameData.ENGLISHLEVEL)
                .checkIsReadyForRelocate(true)
                .checkWorkScheduleIsSelected(WorkScheduleData.FULL, WorkScheduleData.REMOTE);

        ContactInfomationComponent contactInfomationComponent = new ContactInfomationComponent(driver);
        contactInfomationComponent.checkCommunicationInfo(0);
        contactInfomationComponent.checkCommunicationInfo(1);

    }




}
