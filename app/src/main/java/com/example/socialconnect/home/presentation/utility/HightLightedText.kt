package com.example.socialconnect.home.presentation.utility

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle


@Composable
fun HighlightedText(
    text: String,
    query: String,
    modifier: Modifier = Modifier,
    highlightColor: Color = Color.Yellow
) {
    val matches = remember(text, query) {
        if (query.isBlank()) emptyList()
        else Regex(query, RegexOption.IGNORE_CASE)
            .findAll(text)
            .toList()
    }

    if (matches.isEmpty()) {
        Text(text, modifier = modifier)
    } else {
        val annotatedString = buildAnnotatedString {
            var lastIndex = 0
            matches.forEach { match ->
                // Add non-matching text
                append(text.substring(lastIndex, match.range.first))

                withStyle(style = SpanStyle(background = highlightColor)) {
                    append(match.value)
                }

                lastIndex = match.range.last + 1
            }

            // Add remaining text
            append(text.substring(lastIndex))
        }

        Text(annotatedString, modifier = modifier)
    }
}