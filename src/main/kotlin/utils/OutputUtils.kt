package utils

fun displayResult(language: String, title: String, snippet: String, articleText: String) {
    println("URL: https://$language.wikipedia.org/wiki/$title")
    println("Heading: $title")
    println("Snippet: $snippet")
    println("Text of the article: $articleText")
}

fun removeXmlTags(str: String): String = buildString {
    var insideTag = false
    for (char in str) {
        when (char) {
            '<' -> insideTag = true
            '>' -> insideTag = false
            else -> if (!insideTag) append(char)
        }
    }
}