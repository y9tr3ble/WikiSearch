package utils

import org.w3c.dom.Document
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

fun extractFirstSearchResult(searchDoc: Document): org.w3c.dom.Element =
    searchDoc.getElementsByTagName("p").item(0) as org.w3c.dom.Element

fun extractArticleText(articleDoc: Document): String =
    articleDoc.getElementsByTagName("extract").item(0).textContent

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