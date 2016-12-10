package deck

import groovy.transform.CompileStatic

@CompileStatic
enum Suite {
    Hearts('H' as char),
    Diamonds('D' as char),
    Spades('S' as char),
    Clubs('C' as char)

    char code

    Suite(char code) {
        this.code = code
    }
}
