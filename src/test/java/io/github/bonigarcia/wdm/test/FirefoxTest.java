/*
 * (C) Copyright 2020 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.wdm.test;

import static java.lang.invoke.MethodHandles.lookup;
import static org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

class FirefoxTest {

    static final Logger log = getLogger(lookup().lookupClass());

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        // Using a custom geckodriver to test bug 1732076
        // https://bugzilla.mozilla.org/show_bug.cgi?id=1732076
        if (IS_OS_WINDOWS) {
            File geckodriver = new File(FirefoxTest.class.getClassLoader()
                    .getResource("geckodriver.exe").getFile());
            System.setProperty("webdriver.gecko.driver", geckodriver.getPath());
        } else {
            WebDriverManager.firefoxdriver().setup();
        }
    }

    @BeforeEach
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.TRACE);
        options.addPreference("toolkit.asyncshutdown.log", true);
        driver = new FirefoxDriver(options);
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void test() {
        String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/";
        driver.get(sutUrl);
        String title = driver.getTitle();
        log.debug("The title of {} is {}", sutUrl, title);
        assertThat(title).contains("Selenium WebDriver");
    }

}
