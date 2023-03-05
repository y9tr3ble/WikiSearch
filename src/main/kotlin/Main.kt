import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    println("Choose a language to search for articles (en, ru, fr и т.д.):")
    val language = readLine() ?: "en"
    println("Enter your request for wikipedia:")
    val query = readlnOrNull() ?: ""
    val encodedQuery = URLEncoder.encode(query, "UTF-8")
    val searchUrl = "https://$language.wikipedia.org/w/api.php?action=query&list=search&srsearch=$encodedQuery&format=xml"
    val searchDoc = getXmlDocument(searchUrl)
    val searchElement = searchDoc.getElementsByTagName("search").item(0)
    if (searchElement.hasChildNodes()) {
        val pElement = searchElement.firstChild as org.w3c.dom.Element
        val title = pElement.getAttribute("title")
        val snippet = pElement.getAttribute("snippet")
        val pageid = pElement.getAttribute("pageid")
        val articleUrl = "https://$language.wikipedia.org/w/api.php?action=query&prop=extracts&pageids=$pageid&format=xml"
        val articleDoc = getXmlDocument(articleUrl)
        val extractElement = articleDoc.getElementsByTagName("extract").item(0)
        val articleText = extractElement.textContent
        val snippetWithoutTags = removeXmlTags(snippet)
        val articleTextWithoutTags = removeXmlTags(articleText)
        println("URL: https://$language.wikipedia.org/wiki/$title")
        println("Heading: $title")
        println("Snippet: $snippetWithoutTags")
        println("Text of the article: $articleTextWithoutTags")
    } else {
        println("Nothing was found for your query.")
    }
}

fun getXmlDocument(url: String): org.w3c.dom.Document {
    val connection = URL(url).openConnection()
    val inputStream = connection.getInputStream()
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val doc = builder.parse(inputStream)
    (connection as HttpURLConnection).disconnect()
    inputStream.close()
    return doc
}

fun removeXmlTags(str: String): String {
    val result = StringBuilder()
    var insideTag = false
    for (char in str) {
        if (char == '<') {
            insideTag = true
        } else if (char == '>') {
            insideTag = false
        } else if (!insideTag) {
            result.append(char)
        }
    }
    return result.toString()
}