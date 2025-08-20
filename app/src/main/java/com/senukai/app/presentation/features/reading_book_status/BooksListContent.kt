package com.senukai.app.presentation.features.reading_book_status

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senukai.R
import com.senukai.app.core.extensions.estimateSkeletonItemCount
import com.senukai.app.presentation.components.CustomTopAppBar
import com.senukai.app.presentation.components.SmartImage
import com.senukai.app.presentation.features.reading_book_status.components.BooksItemItemSkeleton
import com.senukai.app.presentation.features.reading_book_status.state.BookUi
import com.senukai.app.presentation.features.reading_book_status.state.BooksListState
import com.senukai.app.presentation.features.reading_book_status.state.ReadStatusUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksListContent(
    uiState: BooksListState,
    onRetry: () -> Unit,
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToFullList: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.book_list_title),
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.White
        ) {
            val isRefreshing = uiState is BooksListState.Loading

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRetry
            ) {
                when (uiState) {
                    is BooksListState.Idle -> IdleScreen(onRetry = onRetry)
                    is BooksListState.Loading -> BookListSkeleton()
                    is BooksListState.Success -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(uiState.bookList, key = { it.id }) { section ->
                                ReadStatusSection(
                                    title = section.title,
                                    books = section.books.take(5),
                                    onBookClick = { onNavigateToDetails(it.id) },
                                    onAllClick = { onNavigateToFullList(section.id) }
                                )
                            }
                        }
                    }
                    is BooksListState.Error -> {
                        val context = LocalContext.current
                        val errorMessage = uiState.message ?: stringResource(R.string.unknown_error)
                        IdleScreen(onRetry = onRetry)
                        LaunchedEffect(uiState.message) {
                            Toast.makeText(
                                context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadStatusSection(
    title: String,
    books: List<BookUi>,
    onBookClick: (BookUi) -> Unit,
    onAllClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(R.string.button_all),
                modifier = Modifier.clickable { onAllClick() },
                color = Color.Blue
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(books, key = { it.id }) { book ->
                ReadStatusCard(
                    book = book,
                    onClick = { onBookClick(book) }
                )
            }
        }
    }
}

@Composable
fun ReadStatusCard(
    book: BookUi,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        SmartImage(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            url = book.image
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.title,
            maxLines = 1,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun IdleScreen(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.fetch_books))
        }
    }
}

@Composable
private fun BookListSkeleton() {
    val configuration = LocalConfiguration.current
    val itemsToShow = configuration.estimateSkeletonItemCount(itemHeightDp = 108)
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(itemsToShow) {
            BooksItemItemSkeleton()
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

@Preview
@Composable
private fun SuccessPreview() {
    BooksListContent(
        uiState = BooksListState.Success(
            bookList = listOf(
                ReadStatusUiState(
                    id = 1,
                    title = "Read Status",
                    books = listOf(
                        BookUi(1, "Test", "img")
                    )
                ),
            ),
        ),
        onRetry = {},
        onNavigateToDetails = {},
        onNavigateToFullList = {}
    )
}

@Preview
@Composable
private fun IdlePreview() {
    BooksListContent(
        uiState = BooksListState.Idle,
        onRetry = {},
        onNavigateToDetails = {},
        onNavigateToFullList = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BooksListContent(
        uiState = BooksListState.Loading,
        onRetry = {},
        onNavigateToDetails = {},
        onNavigateToFullList = {},
    )
}
