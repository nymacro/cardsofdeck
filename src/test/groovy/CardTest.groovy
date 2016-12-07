import spock.lang.*
import deck.*

class CardTest extends Specification {
    def "deck size"() {
        expect: Util.decks(1).size() == 52
    }

    def "deck take"() {
        def deck = new Deck()
        deck.take(1)

        expect:
        deck.size() == 51
    }

    def "deck spec"() {
        def deck = Deck.fromString('AH,2D,3S')
        expect:
        deck == [Card.fromCode('AH'), Card.fromCode('2D'), Card.fromCode('3S')]
        deck.take(1)[0] == Card.fromCode('3S')
    }

    def "deck toString"() {
        def deck = Deck.fromString('AH,2D,3S')
        expect:
        deck.toString() == 'AH,2D,3S'
    }
}
