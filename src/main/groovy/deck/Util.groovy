package deck

import groovy.transform.CompileStatic

@CompileStatic
class Util {
    static String cardCode(Card card) {
        "${card.rank.code}${card.suit.code}"
    }

    static String cardImage(Card card) {
        "${cardCode(card)}.png"
    }

    static Deck decks(int n) {
        if (n <= 0) {
            return [] as Deck
        }
        Deck d = new Deck()
        for (int i = 1; i < n; i++) {
            d.addAll(new Deck())
        }
        d
    }
}