package org.example.dam.exer_3

class Pipeline {
    private val stages: MutableList<Pair<String, (List<String>) -> List<String>>> = mutableListOf()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stages.add(Pair(name, transform))
    }

    fun execute(input: List<String>): List<String> {
        return stages.fold(input) { acc, (_, transform) -> transform(acc) }
    }

    fun describe() {
        println("Pipeline stages:")
        stages.forEachIndexed { index, (name, _) ->
            println("  ${index + 1}. $name")
        }
    }

    fun compose(firstName: String, secondName: String, newName: String) {
        val first = stages.find { it.first == firstName }?.second
            ?: error("Stage '$firstName' not found")
        val second = stages.find { it.first == secondName }?.second
            ?: error("Stage '$secondName' not found")

        val composed: (List<String>) -> List<String> = { input -> second(first(input)) }
        stages.add(Pair(newName, composed))
    }

    fun fork(other: Pipeline, input: List<String>): Pair<List<String>, List<String>> {
        return Pair(this.execute(input), other.execute(input))
    }
}

fun buildPipeline(init: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.init()
    return pipeline
}

fun main() {
    val logs = listOf(
        "  INFO: server started  ",
        "  ERROR: disk full  ",
        "  DEBUG: checking config  ",
        "  ERROR: out of memory  ",
        "  INFO: request received  ",
        "  ERROR: connection timeout  "
    )

    val pipeline = buildPipeline {
        addStage("Trim") { lines ->
            lines.map { it.trim() }
        }
        addStage("Filter errors") { lines ->
            lines.filter { it.contains("ERROR") }
        }
        addStage("Uppercase") { lines ->
            lines.map { it.uppercase() }
        }
        addStage("Add index") { lines ->
            lines.mapIndexed { index, line -> "${index + 1}. $line" }
        }
    }

    pipeline.describe()

    val result = pipeline.execute(logs)
    println("\nResult:")
    result.forEach { println("  $it") }

    // --- Challenge: compose ---
    println("\n--- Challenge (compose) ---")
    val composePipeline = buildPipeline {
        addStage("Trim") { lines -> lines.map { it.trim() } }
        addStage("Filter errors") { lines -> lines.filter { it.contains("ERROR") } }
        compose("Trim", "Filter errors", "Trim+Filter")
    }
    composePipeline.describe()
    println("\nComposed result:")
    composePipeline.execute(logs).forEach { println("  $it") }

    // --- Challenge: fork ---
    println("\n--- Challenge (fork) ---")
    val pipelineA = buildPipeline {
        addStage("Trim") { lines -> lines.map { it.trim() } }
        addStage("Filter errors") { lines -> lines.filter { it.contains("ERROR") } }
        addStage("Uppercase") { lines -> lines.map { it.uppercase() } }
    }
    val pipelineB = buildPipeline {
        addStage("Trim") { lines -> lines.map { it.trim() } }
        addStage("Filter INFO") { lines -> lines.filter { it.contains("INFO") } }
        addStage("Uppercase") { lines -> lines.map { it.uppercase() } }
    }

    val (errorsResult, infoResult) = pipelineA.fork(pipelineB, logs)
    println("Fork A (errors): $errorsResult")
    println("Fork B (info):   $infoResult")
}