package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashBoardPage() {
        header.shouldBe(visible);
    }

    public void verifyPageIsVisible() {
        header.shouldBe(visible);
    }

    public int getCardBalanceByIndex(int cardIndex) {
        var cardElement = cards.get(cardIndex);
        var text = cardElement.text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTransferByIndex(int cardIndex) {
        var cardElement = cards.get(cardIndex);
        cardElement.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }

    public int getCardsCount() {
        return cards.size();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish).trim();
        return Integer.parseInt(value);
    }
}