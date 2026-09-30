package ai;

import java.util.List;
import java.util.Map;

public final class ExistingPomRegistry {
    private ExistingPomRegistry() { }

    private record Entry(String page, String method, String strategy, String locator,
                         String action, String elementName) { }

    private static final List<Entry> ENTRIES = List.of(
        new Entry("LoginPage", "setUserName", "xpath", "//input[@placeholder='Username']", "type", "txtUsername"),
        new Entry("LoginPage", "setPassword", "xpath", "//input[@placeholder='Password']", "type", "txtPassword"),
        new Entry("LoginPage", "clickLogin", "xpath", "//button[@type='submit']", "click", "btnLogin"),

        new Entry("HomePage", "isDashboardDisplayed", "xpath", "//h6[text()='Dashboard']", "verify", "dashboardText"),
        new Entry("HomePage", "clickLogout", "linktext", "Logout", "click", "lnkLogout"),
        new Entry("HomePage", "clickRecruitment", "xpath", "(//span[contains(normalize-space(),'Recruitment')])[1]", "click", "recruitment"),

        new Entry("CandidatePage", "clickAddButton", "xpath", "//button[normalize-space()='Add']", "click", "btnAdd"),
        new Entry("CandidatePage", "enterFirstName", "xpath", "//input[@placeholder='First Name']", "type", "txtFirstName"),
        new Entry("CandidatePage", "enterMiddleName", "xpath", "//input[@placeholder='Middle Name']", "type", "txtMiddleName"),
        new Entry("CandidatePage", "enterLastName", "xpath", "//input[@placeholder='Last Name']", "type", "txtLastName"),
        new Entry("CandidatePage", "clickVacancyDropdown", "xpath", "//label[normalize-space()='Vacancy']/following::div[contains(normalize-space(),'-- Select --')][1]", "click", "drpVacancy"),
        new Entry("CandidatePage", "selectSeniorQALead", "xpath", "(//span[normalize-space()='Senior QA Lead'])[1]", "click", "optionSeniorQALead"),
        new Entry("CandidatePage", "enterEmail", "xpath", "//label[normalize-space()='Email']/following::input[@placeholder='Type here'][1]", "type", "txtEmail"),
        new Entry("CandidatePage", "enterContactNumber", "xpath", "//label[normalize-space()='Contact Number']/following::input[@placeholder='Type here']", "type", "txtContactNumber"),
        new Entry("CandidatePage", "clickSaveButton", "xpath", "(//button[normalize-space()='Save'])[1]", "click", "btnSave"),
        new Entry("CandidatePage", "isSuccessMessageDisplayed", "xpath", "//p[contains(normalize-space(),'Successfully Saved')]", "verify", "txtSuccessMessage")
    );

    public static BrowserAction resolve(BrowserAction action) {
        if (action == null || action.locator() == null) return action;

        for (Entry e : ENTRIES) {
            if (sameLocator(e, action)) {
                String value = action.value();
                return new BrowserAction(e.action(), e.strategy(), e.locator(), value,
                        e.page(), e.elementName(), e.method());
            }
        }
        return action;
    }

    public static String pageFor(BrowserAction action) {
        BrowserAction resolved = resolve(action);
        return resolved.pageObject();
    }

    public static boolean isKnown(BrowserAction action) {
        return pageFor(action) != null && !pageFor(action).isBlank();
    }

    private static boolean sameLocator(Entry e, BrowserAction a) {
        return e.strategy().equalsIgnoreCase(a.strategy())
                && e.locator().equals(a.locator());
    }
}
