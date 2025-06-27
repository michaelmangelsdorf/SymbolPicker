package domain.model


data class Tile(
    val symbolStr: String,
    val userId: Int,
    val locked: Boolean,
    val synced: Boolean,
    val messageStr: String
)

data class UserGrid(
    val size: Int = 5,
    val tiles: List<List<Tile>> = List(5) {
        List(5) {
            Tile(symbolStr = "⬜", userId = 0, locked = false, synced = false, messageStr = "")
        }
    }
) {
    fun get(x: Int, y: Int): Tile {
        require(x in 0 until size && y in 0 until size) {
            "Coordinates out of bounds: ($x, $y)"
        }
        return tiles[y][x]
    }

    fun set(x: Int, y: Int, tile: Tile): UserGrid {
        require(x in 0 until size && y in 0 until size) {
            "Coordinates out of bounds: ($x, $y)"
        }

        val newRow = tiles[y].toMutableList().apply { this[x] = tile }
        val newTiles = tiles.toMutableList().apply { this[y] = newRow }

        return this.copy(tiles = newTiles)
    }
}


// Helper to just put in some data
fun putSampleData(userGrid: UserGrid) {
    val emojis = listOf("😀", "😂", "🥰", "😎", "😢", "🔥", "🎉", "👀", "💬", "📱", "👍", "💡")
    val messages = listOf(
        "Hey there!", "What's up?", "Let's meet at 5", "LOL 😂", "Can't talk now", "Call me maybe",
        "Just sent the doc", "Awesome!", "Busy rn", "Thanks!", "Good morning ☀️", "Check this out"
    )
    val users = listOf("alice", "bob", "carol", "dave", "eve", "frank")

    for (y in 0 until 5) {
        for (x in 0 until 5) {
            val tile = Tile(
                symbolStr = emojis.random(),
                userId = 1,
                locked = false,
                synced = false,
                messageStr = messages.random()
            )
            userGrid.set(x, y, tile)
        }
    }
}