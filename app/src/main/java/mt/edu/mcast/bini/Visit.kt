package mt.edu.mcast.bini

data class Visit(
    var name: String,
    var surname: String,
    var idCard: String,
    var number: String ?= null,
    var person: String,
    var timeEntered: String,
    var timeLeft: String ?= null
)
