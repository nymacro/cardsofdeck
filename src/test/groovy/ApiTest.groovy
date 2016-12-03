import groovy.json.JsonSlurper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import spock.lang.*

// TODO fix these tests... How to test this stuff properly?
class ApiTest extends Specification {
    @AutoCleanup
    @Shared
    GroovyRatpackMainApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

    @Unroll
    def "new deck"() {
        when:
        def json = aut.httpClient.getText("/new/shuffled")

        def s = new JsonSlurper().parse(json.bytes)

        then:
        s.success == true
        s.remaiing == 52
        s.shuffled == true
        UUID.fromString(s.deckId) != null
    }
}
