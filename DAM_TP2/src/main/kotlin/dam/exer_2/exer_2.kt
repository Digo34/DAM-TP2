package dam.exer_1

class Cache<K: Any, V: Any> {

    private val entries: MutableMap<K, V> = mutableMapOf()

    fun put(key: K, value: V) {
        entries[key] = value
    }

    fun get(key: K): V? {
        return entries[key]
    }

    fun evict(key: K) {
        entries.remove(key)
    }

    fun size(): Int {
        return entries.size
    }

    fun getOrPut(key: K, default: () -> V): V {
        return entries.getOrPut(key, default)
    }

    fun transform(key: K, action: (V) -> V): Boolean {
        val entry = entries[key]

        if (entry != null) {
            entries[key] = action(entry)
            return true
        }
        return false
    }

    fun snapshot(): Map<K, V> {
        return entries.toMap()
    }

    fun filterValues(predicate: (V) -> Boolean): Map<K, V> {
        return entries.filter { (_, v) -> predicate(v) }.toMap()
    }
}

fun main() {
    val wfc = Cache<String, Int>()
    wfc.put("kotlin", 1)
    wfc.put("scala", 1)
    wfc.put("haskell", 1)
    println("--- Word frequency cache ---")
    println("Size: " + wfc.size())
    println("Frequency of \"kotlin\": " + wfc.get("kotlin"))
    println("getOrPut \"kotlin\": " + wfc.getOrPut("kotlin") {1})
    println("getOrPut \"java\": " + wfc.getOrPut("java") {0})
    println("Size after getOrPut : " + wfc.size())
    println("Transform  of \"kotlin\" (+1): " + wfc.transform("kotlin") {it+1})
    println("Transform  of \"cobol\" (+1): " + wfc.transform("cobol") {it+1})
    println("Snapshot: " + wfc.snapshot())

    val irc = Cache<Int, String>()
    irc.put(1, "Alice")
    irc.put(2, "Bob")
    println("\n--- Id registry cache ---")
    println("Id 1 -> " + irc.get(1))
    println("Id 2 -> " + irc.get(2))
    irc.evict(1)
    println("After evict id 1, size: " + irc.size())
    println("Id 1 after evict -> " + irc.get(1))

    println("\n--- Challenge ---")
    println("Words with count > 1 : " + wfc.filterValues { it > 1 })
}