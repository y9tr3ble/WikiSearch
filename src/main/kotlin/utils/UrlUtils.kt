package utils

import java.net.URLEncoder

fun buildSearchUrl(language: String, query: String): String =
    "https://$language.wikipedia.org/w/api.php?action=query&list=search&srsearch=${
        URLEncoder.encode(
            query,
            "UTF-8"
        )
    }&format=xml"

fun buildArticleUrl(language: String, pageid: String): String =
    "https://$language.wikipedia.org/w/api.php?action=query&prop=extracts&pageids=$pageid&format=xml"