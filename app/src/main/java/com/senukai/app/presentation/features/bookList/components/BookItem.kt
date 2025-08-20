package com.senukai.app.presentation.features.bookList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senukai.app.core.utils.ApiUtil
import com.senukai.app.core.extensions.shimmer
import com.senukai.app.domain.model.Book
import com.senukai.app.presentation.components.SmartImage
import com.senukai.app.presentation.features.bookList.state.BookListState

@Composable
fun BookItem(
    book: Book,
    onClick: (Book) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book) }
            .height(100.dp)
    ) {

        SmartImage(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            url = book.image
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BookItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BookItem(
        book = Book.mock(),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BookItemSkeletonPreview() {
    BookItemSkeleton()
}
