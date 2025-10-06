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

    @Когда("пользователь переводит {string} рублей с карты с номером {string} на свою {string} карту с главной страницы")
    public void userTransfersMoney(String amount, String fromCardNumber, String toCardNumber) {
        int transferAmount = Integer.parseInt(amount);
        int targetCardIndex = Integer.parseInt(toCardNumber);

        DataHelper.CardInfo targetCard = (targetCardIndex == 1) ?
                DataHelper.getFirstCardInfo() : DataHelper.getSecondCardInfo();

        TransferPage transferPage = dashboardPage.selectCardToTransfer(targetCard.getTestId());
        dashboardPage = transferPage.makeTransfer(fromCardNumber, transferAmount);
    }

    @Тогда("баланс его {string} карты из списка на главной странице должен стать {string} рублей")
    public void verifyCardBalance(String cardNumber, String expectedBalance) {
        int cardIndex = Integer.parseInt(cardNumber);
        int expectedBal = Integer.parseInt(expectedBalance);

        DataHelper.CardInfo card = (cardIndex == 1) ?
                DataHelper.getFirstCardInfo() : DataHelper.getSecondCardInfo();

        int actualBalance = dashboardPage.getCardBalance(card.getTestId());

        assertEquals(expectedBal, actualBalance,
                "Баланс карты " + cardIndex + " должен быть " + expectedBal + " рублей, но был " + actualBalance);
    }
}