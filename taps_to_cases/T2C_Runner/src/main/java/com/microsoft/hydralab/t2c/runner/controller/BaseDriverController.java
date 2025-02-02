// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.
package com.microsoft.hydralab.t2c.runner.controller;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;

public class BaseDriverController {
    public WebDriver webDriver;
    protected Logger logger;

    public BaseDriverController(WebDriver webDriver, Logger logger) {
        this.webDriver = webDriver;
        this.logger = logger;
    }

    public void click(WebElement element) {
        element.click();
    }

    /**
     * Send content via keyboard, this will send the string directly to the current focus element
     * @param content string you want to input with
     */
    public void sendKeys(String content) {
        new Actions(webDriver).sendKeys(content).perform();
    }

    public void tap(int x, int y) {
    }

    public void input(WebElement element, String content) {
        element.sendKeys(content);
    }

    public void clear(WebElement element) {
        element.clear();
    }

    public void activateApp(String appPackageName) {
    }

    public void terminateApp(String appPackageName) {
    }

    public void pressKey(AndroidKey key) {
    }

    public void pressKeyCode(String keyCode) {
    }

    public void scroll(WebElement webElement, int xVector, int yVector) {
    }

    public void swipe(String direction) {
    }

    public void longClick(Integer duration, WebElement element) {
    }

    public void dragAndDrop(WebElement element, int xVec, int yVec) {
        Point location = element.getLocation();
        Dimension dimension = element.getSize();
        int x = location.getX();
        int y = location.getY();
        int width = dimension.getWidth();
        int height = dimension.getHeight();
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        dragAndDropWithPosition(centerX, centerY, centerX + xVec, centerY + yVec);
    }

    public void dragAndDrop(WebElement fromElement, WebElement toElement) {
        Point fromLocation = fromElement.getLocation();
        Dimension fromDimension = fromElement.getSize();
        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();
        int fromWidth = fromDimension.getWidth();
        int fromHeight = fromDimension.getHeight();
        int fromCenterX = fromX + fromWidth / 2;
        int fromCenterY = fromY + fromHeight / 2;

        Point toLocation = toElement.getLocation();
        Dimension toDimension = toElement.getSize();
        int toX = toLocation.getX();
        int toY = toLocation.getY();
        int toWidth = toDimension.getWidth();
        int toHeight = toDimension.getHeight();
        int toCenterX = toX + toWidth / 2;
        int toCenterY = toY + toHeight / 2;

        dragAndDropWithPosition(fromCenterX, fromCenterY, toCenterX, toCenterY);
    }

    public void dragAndDropWithPosition(int fromX, int fromY, int toX, int toY) {

    }

    public void navigateTo(String url) {
    }

    //Only the following attributes are supported: [checkable, checked, {class,className}, clickable,
    //{content-desc,contentDescription}, enabled, focusable, focused, {long-clickable,longClickable},
    //package, password, {resource-id,resourceId}, scrollable, selection-start, selection-end, selected,
    //{text,name}, bounds, displayed, contentSize]
    public void assertElementAttribute(WebElement webElement, String attribute, String expectedValue) {
        String tagName = webElement.getAttribute(attribute);
        assert tagName != null : "Can't get this attribute: " + attribute;
        assert tagName.equals(expectedValue) : "Doesn't match expectedVal: "
                + "expectedVal: " + expectedValue + "; " + "resultVal: " + tagName;
        System.out.println("Matched!" + "Value: " + tagName);
    }

    public String getInfo(WebElement webElement, String attribute) {
        String result = webElement.getAttribute(attribute);
        assert result != null : "Can't get this attribute: " + attribute;
        return result;
    }

    public void switchToUrl(String url) {
    }

    public WebElement findElementByAccessibilityId(String accessibilityId) {
        WebElement elementFound = null;
        try {
            elementFound = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(new ExpectedCondition<WebElement>() {
                        @Override
                        public WebElement apply(WebDriver input) {
                            return webDriver.findElement(new AppiumBy.ByAccessibilityId(accessibilityId));
                        }
                    });
        } catch (Exception e) {
            logger.info("Can not find element by AccessibilityId: " + accessibilityId);
            e.printStackTrace();
        }
        return elementFound;
    }

    public WebElement findElementByXPath(String xpath) {
        WebElement elementFound = null;
        try {
            elementFound = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(driver -> driver.findElement(AppiumBy.xpath(xpath)));
        } catch (Exception e) {
            logger.info("Can not find element by XPath: " + xpath);
        }
        return elementFound;
    }

    public WebElement findElementByName(String name) {
        WebElement elementFound = null;
        try {
            elementFound = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(driver -> driver.findElement(AppiumBy.name(name)));
        } catch (Exception e) {
            logger.info("Can not find element by name: " + name);
        }
        return elementFound;
    }
}
