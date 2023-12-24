package src.builds

fun resolvePathToScript(path: String): String {
    return ".teamcity/$path"
}