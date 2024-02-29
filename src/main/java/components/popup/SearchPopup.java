package components.popup;

import common.AbsCommon;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class SearchPopup extends AbsCommon implements IPopup{


    //это поле. т.к. используется в неск. методах. Если только в 1 методе, то писать надо
    //внутри этого метода
    private String searchInputPopupLocator= "//div[./input[@type='search']]";

    public SearchPopup(WebDriver driver) {
        super(driver);
    }

    @Override
    public void popupShouldNotBeVisible() {
        Assertions.assertTrue(waitTools.waitNotElementPresent(By.xpath(searchInputPopupLocator)));

    }

    @Override
    public void popupShouldBeVisible() {
        Assertions.assertTrue(waitTools.waitElementPresent(By.xpath(searchInputPopupLocator)));
    }

    public void enterSearchRequest(String search){
        driver.findElement(By.xpath(searchInputPopupLocator)).sendKeys(search);
       // Actions.sendKeys(Keys.ENTER).build().perform();
    }


}
