package dam.exer_1

sealed class Event(val username: String)

class Login(username: String, val timestamp: Long): Event(username)

class Purchase(username: String, val amount: Double, val timestamp: Long): Event(username)

class Logout(username: String, val timestamp: Long): Event(username)

fun List<Event>.filterByUser(username: String): List<Event> {
    return this.filter {it.username == username}
}

fun List<Event>.totalSpent(username: String): Double{
    /*
    val userEvents = this.filterByUser(username)
    val userPurchases = userEvents.filterIsInstance<Purchase>()
    return userPurchases.sumOf {it.amount}
     */
    return this.filterByUser(username)
        .filterIsInstance<Purchase>()
        .sumOf {it.amount}
}

fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    for (event in events) {
        handler(event)
    }
}

fun main() {

    val events = listOf (
        Login("alice", 1_000 ),
        Purchase("alice", 49.99 , 1_100 ),
        Purchase("bob", 19.99 , 1_200 ),
        Login("bob", 1_050 ),
        Purchase("alice", 15.00 , 1_300 ),
        Logout("alice", 1_400 ),
        Logout("bob", 1_500 )
    )

    processEvents(events) { event ->
        when(event) {
            is Login -> {
                println("[LOGIN]    ${event.username} logged in at t=${event.timestamp}")
            }is Purchase -> {
                println("[PURCHASE] ${event.username} spent $${event.amount} at t=${event.timestamp}")
            }is Logout -> {
                println("[LOGOUT]   ${event.username} logged out at t=${event.timestamp}")
            }
        }
    }

    println()
    println("Total spent by alice: $ \$%.2f".format(events.totalSpent("alice")))
    println("Total spent by bob: $" + events.totalSpent("bob"))

    val aliceEvents = events.filterByUser("alice")
    println()
    println("Events for alice:")
    processEvents(aliceEvents) { event ->
        when(event) {
            is Login -> {
                println("  Login(username=${event.username}, " +
                        "timestamp=${event.timestamp})")
            }is Purchase -> {
            println("  Purchase(username=${event.username}, " +
                    "amount=${event.amount}, " +
                    "timestamp=${event.timestamp})")
            }is Logout -> {
                println("  Logout(username=${event.username}, " +
                        "timestamp=${event.timestamp})")
            }
        }
    }
}