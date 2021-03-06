package swot

import java.io.File

/**
 * @author max
 */

object CompilationState {
    val blacklist = File("lib/domains/blacklist.txt").readLines().toHashSet()
    val domains = File("lib/domains/tlds.txt").readLines().toHashSet()
}

fun main(args: Array<String>) {
    val root = File("lib/domains")
    root.recurse {
        if (it.isFile()) {
            val parts = root.relativePath(it).trimTrailing(".txt").split('/').toList()
            if (!checkSet(CompilationState.blacklist, parts) && !checkSet(CompilationState.domains, parts)) {
                CompilationState.domains.add(parts.reverse().join("."))
            }
        }
    }

    val blacklist = CompilationState.blacklist.map { "-$it" }.sort().join("\n")
    File("out/artifacts/swot.txt").writeText(blacklist + "\n" + CompilationState.domains.sort().join("\n"))
}
