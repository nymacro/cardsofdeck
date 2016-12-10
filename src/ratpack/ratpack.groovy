import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

import static ratpack.groovy.Groovy.ratpack
import deck.*
import static ratpack.jackson.Jackson.json

static int minmax(int min, int max, int n) {
    Math.max(min, Math.min(max, n))
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class CardResponse {
    String image
    String value
    String suit
    String code
}

class PileResponse {
    int remaining
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class DeckResponse {
    boolean success
    @JsonProperty("deck_id")
    String deckId
    boolean shuffled
    int remaining
    List<CardResponse> cards
    Map<String, CardResponse> piles
}

static int remaining(String deckId) {
    Database.instance.get(deckId)?.size() ?: 0
}

static List<CardResponse> cardsToResponse(List<Card> cards) {
    cards.collect { card ->
        new CardResponse(value: card.rank.toString().toUpperCase(), suit: card.suit.toString().toUpperCase(),
                code: Util.cardCode(card), image: Util.cardImage(card))
    }
}

static Map<String, CardResponse> pilesToResponse(Map<String, Deck> piles) {
    def result = [:]
    piles.each {
        result.put(it.key, new PileResponse(remaining: it.value.size()))
    }
    result
}

ratpack {
    handlers {
        path("new") {
            String cards = request.queryParams['cards']
            Deck deck
            if (cards != null) {
                deck = Deck.fromString(cards)
            } else {
                int deckCount = minmax(1, 10, Integer.parseInt(request.queryParams['deck_count'] ?: '0'))
                deck = Util.decks(deckCount)
            }
            String deckId = Database.instance.create(Database.randomId(), deck)
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled, remaining: deck.size()))
        }

        path("new/shuffle") {
            String cards = request.queryParams['cards']
            Deck deck
            if (cards != null) {
                deck = Deck.fromString(cards)
            } else {
                int deckCount = minmax(1, 10, Integer.parseInt(request.queryParams['deck_count'] ?: '0'))
                deck = Util.decks(deckCount)
            }
            deck.shuffle()
            def deckId = Database.instance.create(Database.randomId(), deck)
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled, remaining: deck.size()))
        }

        path(":deckId/draw") {
            String deckId = pathTokens.deckId
            Deck deck
            if (deckId == "new") {
                // create a new deck
                deck = Util.decks(1).shuffle()
                deckId = Database.instance.create(Database.randomId(), deck)
            } else {
                deck = Database.instance.get(deckId)
            }
            int count = minmax(1, deck.size(), Integer.parseInt(request.queryParams['count'] ?: '0'))
            def cards = deck.take(count)
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled, cards: cardsToResponse(cards), remaining: deck.size()))
        }

        path(":deckId/shuffle") {
            String deckId = pathTokens.deckId
            Deck deck = Database.instance.get(deckId).shuffle()
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled, remaining: deck.size()))
        }

        path(":deckId/pile/:pileName/add") {
            String deckId = pathTokens.deckId
            String pileName = pathTokens.pileName
            String cards = request.queryParams['cards']
            Deck deck = Database.instance.get(deckId)
            Deck pile = deck.getPile(pileName)
            pile.addAll(Deck.fromString(cards))
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled, remaining: deck.size(),
                piles: pilesToResponse(deck.piles)))
        }

        path(":deckId/pile/:pileName/draw") {
            String deckId = pathTokens.deckId
            String pileName = pathTokens.pileName
            Deck deck = Database.instance.get(deckId)
            Deck pile = deck.getPile(pileName)
            int count = minmax(1, deck.size(), Integer.parseInt(request.queryParams['count'] ?: '0'))
            List<Card> cards = pile.take(count)
            render json(new DeckResponse(success: true, deckId: deckId, shuffled: deck.shuffled,
                cards: cardsToResponse(cards), remaining: deck.size(), piles: pilesToResponse(deck.piles)))
        }
    }
}
