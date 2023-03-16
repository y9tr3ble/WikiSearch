import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    print("Choose a language to search for articles (en, ru, fr and others): ")
    val language = readLine() ?: "en"

            print("Enter your request for wikipedia: ")
    val query = readLine() ?: ""

    val searchUrl = "https://$language.wikipedia.org/w/api.php?action=query&list=search&srsearch=${URLEncoder.encode(query, "UTF-8")}&format=xml"
    val searchDoc = getXmlDocument(searchUrl)

    val pElement = searchDoc.getElementsByTagName("p").item(0) as org.w3c.dom.Element
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
}

fun getXmlDocument(url: String): org.w3c.dom.Document {
    val connection = URL(url).openConnection() as HttpURLConnection
    val inputStream = connection.inputStream
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val doc = builder.parse(inputStream)
    connection.disconnect()
    inputStream.close()
    return doc
}

fun removeXmlTags(str: String): String {
    val result = StringBuilder()
    var insideTag = false
    for (char in str) {
        when (char) {
            '<' -> insideTag = true
            '>' -> insideTag = false
            else -> if (!insideTag) result.append(char)
        }
    }
    return result.toString()
}
