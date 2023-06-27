import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    val language = promptLanguage()
    val query = promptQuery()

    val searchUrl = buildSearchUrl(language, query)
    val searchDoc = getXmlDocument(searchUrl)
    val pElement = extractFirstSearchResult(searchDoc)

    val title = pElement.getAttribute("title")
    val snippet = pElement.getAttribute("snippet")
    val pageid = pElement.getAttribute("pageid")

    val articleUrl = buildArticleUrl(language, pageid)
    val articleDoc = getXmlDocument(articleUrl)
    val articleText = extractArticleText(articleDoc)

    val snippetWithoutTags = removeXmlTags(snippet)
    val articleTextWithoutTags = removeXmlTags(articleText)

    displayResult(language, title, snippetWithoutTags, articleTextWithoutTags)
}

fun promptLanguage(): String {
    print("Choose a language to search for articles (en, ru, fr and others): ")
    return readlnOrNull() ?: "en"
}

fun promptQuery(): String {
    print("Enter your request for wikipedia: ")
    return readlnOrNull() ?: ""
}

fun buildSearchUrl(language: String, query: String): String =
    "https://$language.wikipedia.org/w/api.php?action=query&list=search&srsearch=${URLEncoder.encode(query, "UTF-8")}&format=xml"

fun buildArticleUrl(language: String, pageid: String): String =
    "https://$language.wikipedia.org/w/api.php?action=query&prop=extracts&pageids=$pageid&format=xml"

fun extractFirstSearchResult(searchDoc: Document): org.w3c.dom.Element =
    searchDoc.getElementsByTagName("p").item(0) as org.w3c.dom.Element

fun extractArticleText(articleDoc: Document): String =
    articleDoc.getElementsByTagName("extract").item(0).textContent

fun displayResult(language: String, title: String, snippet: String, articleText: String) {
    println("URL: https://$language.wikipedia.org/wiki/$title")
    println("Heading: $title")
    println("Snippet: $snippet")
    println("Text of the article: $articleText")
}

fun getXmlDocument(url: String): Document {
    val connection = (URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
    }

    return connection.runCatching {
        inputStream.use { inputStream ->
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            builder.parse(inputStream)
        }
    }.onFailure {
        connection.disconnect()
    }.getOrThrow()
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