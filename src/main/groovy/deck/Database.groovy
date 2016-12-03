package deck

import groovy.transform.CompileStatic

/**
 * In-memory "database" for decks
 */
@CompileStatic
class Database extends HashMap<String, Deck> {
    private static Database instance

    Database() {
        super()
    }

    static Database getInstance() {
        if (instance == null) {
            instance = new Database()
        }
        return instance
    }

    static String randomId() {
        UUID.randomUUID().toString()
    }

    String create(String id, Deck deck) {
        put(id, deck)
        id
    }
}