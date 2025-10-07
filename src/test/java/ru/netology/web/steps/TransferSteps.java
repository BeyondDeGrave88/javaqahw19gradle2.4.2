package ru.netology.web.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.VerificationPage;
import ru.netology.web.page.TransferPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferSteps {
    private DashBoardPage dashboardPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void userIsLoggedIn(String login, String password) {
        LoginPage loginPage = Selenide.open("http://localhost:9999", LoginPage.class);

        DataHelper.AuthInfo authInfo = new DataHelper.AuthInfo(login, password);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);

        DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы")
    public void userTransfersMoney(int amount, String fromCardNumber, int toCardIndex) {
        TransferPage transferPage = dashboardPage.selectCardToTransferByIndex(toCardIndex - 1);
        dashboardPage = transferPage.makeTransfer(fromCardNumber, amount);
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей")
    public void verifyCardBalance(int cardIndex, int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalanceByIndex(cardIndex - 1);

        assertEquals(expectedBalance, actualBalance,
                "Баланс карты " + cardIndex + " должен быть " + expectedBalance + " рублей, но был " + actualBalance);
    }
}