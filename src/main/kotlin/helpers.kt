package exacting

import java.util.*

private val random = Random()

fun chance(percentage: Int): Boolean {
    return random.nextInt(100) < percentage
}
