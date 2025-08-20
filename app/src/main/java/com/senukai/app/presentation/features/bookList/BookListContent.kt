package com.senukai.app.presentation.features.bookList

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senukai.R
import com.senukai.app.core.extensions.estimateSkeletonItemCount
import com.senukai.app.domain.model.Book
import com.senukai.app.presentation.components.CustomTopAppBar
import com.senukai.app.presentation.features.bookList.components.BookItem
import com.senukai.app.presentation.features.bookList.components.BookItemSkeleton
import com.senukai.app.presentation.features.bookList.state.BookListState

@Composable
fun BookListContent(
    uiState: BookListState,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.book_list_title),
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color.White
        ) {
            when (uiState) {
                is BookListState.Idle -> IdleScreen(onRetry = onRetry)
                is BookListState.Loading -> BookListSkeleton()
                is BookListState.Success -> BookList(
                    bookList = uiState.readStatusBook,
                    onItemClick = { book ->
                        onNavigateToDetails(book.id)
                    }
                )

                is BookListState.Error -> {
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
private fun BookList(
    bookList: List<Book>,
    onItemClick: (Book) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(items = bookList, key = { it.id }) { book ->
            BookItem(
                book = book,
                onClick = { onItemClick(book) }
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun BookListSkeleton() {
    val configuration = LocalConfiguration.current
    val itemsToShow = configuration.estimateSkeletonItemCount(itemHeightDp = 108)

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(itemsToShow) {
            BookItemSkeleton()
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
    BookListContent(
        uiState = BookListState.Success(readStatusBook = Book.mockList()),
        onRetry = {},
        onNavigateBack = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun IdlePreview() {
    BookListContent(
        uiState = BookListState.Idle,
        onRetry = {},
        onNavigateBack = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BookListContent(
        uiState = BookListState.Loading,
        onRetry = {},
        onNavigateBack = {},
        onNavigateToDetails = {}
    )
}
