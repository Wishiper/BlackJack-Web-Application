package agprojects.blackjack.models;

import agprojects.blackjack.models.card.Card;
import org.springframework.stereotype.Component;

/**
 * This class represents the dealer in BlackJack.
 */
@Component
public final class Dealer {

    /**
     * The dealers deck which consists of 312 cards (6 decks)
     */
    private final Deck deck;

    private final Hand dealersHand;

    public Dealer() {
        this.deck = new Deck();
        this.dealersHand = new Hand();
    }

    public Card draw(){
        return deck.getCard();

    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getDealersHand() {
        return dealersHand;
    }
}
