import utils.*

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
