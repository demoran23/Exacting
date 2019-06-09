package exacting

import java.util.*

val chance: (Int) -> Boolean = { percentage -> Random().nextInt(100) < percentage }
