package deck

import groovy.transform.CompileStatic

@CompileStatic
class Card {
    Suite suit
    Rank rank

    static Card fromCode(String code) {
        Rank rank = Rank.values().find { it.code == code.charAt(0) }
        Suite suite = Suite.values().find { it.code == code.charAt(1) }
        return new Card(suit: suite, rank: rank)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Card card = (Card) o

        if (rank != card.rank) return false
        if (suit != card.suit) return false

        return true
    }

    String toString() {
        return "${rank.code}${suit.code}"
    }
}
