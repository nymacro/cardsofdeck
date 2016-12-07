package deck

import groovy.transform.CompileStatic

@CompileStatic
enum Rank {
    Two('2' as char),
    Three('3' as char),
    Four('4' as char),
    Five('5' as char),
    Six('6' as char),
    Seven('7' as char),
    Eight('8' as char),
    Nine('9' as char),
    Ten('0' as char),
    Jack('J' as char),
    Queen('Q' as char),
    King('K' as char),
    Ace('A' as char)

    char code

    Rank(char code) {
        this.code = code
    }

    String toString() {
        return "${code}"
    }
}
