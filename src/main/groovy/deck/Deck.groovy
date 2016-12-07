package deck

import groovy.transform.CompileStatic

@CompileStatic
class Deck extends LinkedList<Card> {
    boolean shuffled = false
    Map<String, Deck> piles = new HashMap<>()

    Deck() {
        super()
        Suite.values().each { Suite suite ->
            Rank.values().each { Rank rank ->
                this.add(new Card(suit: suite, rank: rank))
            }
        }
    }

    Deck shuffle() {
        shuffled = true
        Collections.shuffle(this)
        this
    }

    List<Card> take(int n) {
        if (size() == 0) {
            return null
        }

        n = Math.min(size(), n)

        def result = new LinkedList<>()
        n.times {
            result.add(this.pop())
        }
        result
    }

    static Deck fromString(String str) {
        Deck deck = new Deck()
        deck.clear()
        str.split(/,/).each { String it -> deck.add(Card.fromCode(it)) }
        return deck
    }

    String toString() {
        this.join(",")
    }

    Deck getPile(String id) {
        piles.computeIfAbsent(id) { k ->
            def deck = new Deck()
            deck.clear()
            deck
        }
    }
}
