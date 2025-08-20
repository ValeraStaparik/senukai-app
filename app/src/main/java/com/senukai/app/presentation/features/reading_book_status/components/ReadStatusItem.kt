package com.senukai.app.presentation.features.reading_book_status.components

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
import com.senukai.app.core.extensions.shimmer
import com.senukai.app.domain.model.ReadStatusBook
import com.senukai.app.presentation.features.bookList.components.BookItemSkeleton

@Composable
fun ReadStatusItem(
    readStatusBook: ReadStatusBook,
    onClick: (ReadStatusBook) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(readStatusBook) }
            .height(100.dp)
    ) {
//        val url = ApiUtil.BASE_URL + readStatusBook.image

//        SmartImage(
//            modifier = Modifier
//                .size(100.dp)
//                .clip(RoundedCornerShape(12.dp)),
//            url = url
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = readStatusBook.title,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BooksItemItemSkeleton() {
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
    ReadStatusItem(
        readStatusBook = ReadStatusBook.mock(),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun BooksItemItemSkeletonPreview() {
    BookItemSkeleton()
}
