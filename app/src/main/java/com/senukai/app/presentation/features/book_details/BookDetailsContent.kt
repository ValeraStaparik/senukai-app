package com.senukai.app.presentation.features.book_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senukai.R
import com.senukai.app.core.utils.ApiUtil
import com.senukai.app.core.extensions.shimmer
import com.senukai.app.domain.model.BookDetails
import com.senukai.app.presentation.components.CustomTopAppBar
import com.senukai.app.presentation.components.ErrorContent
import com.senukai.app.presentation.components.InfoRow
import com.senukai.app.presentation.components.InfoRowSkeleton
import com.senukai.app.presentation.components.SmartImage
import com.senukai.app.presentation.features.book_details.state.BookDetailsState

@Composable
fun BookDetailsContent(
    uiState: BookDetailsState,
    onRetry: () -> Unit,
    onNavigationBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.book_details_title),
                onNavigateBack = onNavigationBack
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
                is BookDetailsState.Loading -> BookDetailsSkeleton()
                is BookDetailsState.Success -> BookDetails(uiState.bookDetails)
                is BookDetailsState.Error -> ErrorContent(
                    message = uiState.message,
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
private fun BookDetails(bookDetails: BookDetails) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        SmartImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            url = bookDetails.image,
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = bookDetails.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        bookDetails.author?.let {
            InfoRow(
                title = stringResource(R.string.author),
                value = it,
                icon = Icons.Default.LocationOn
            )
        }
        bookDetails.isbn?.let {
            InfoRow(
                title = stringResource(R.string.number),
                value = it,
                icon = Icons.Filled.Restaurant
            )
        }
        bookDetails.description?.let {
            InfoRow(
                title = stringResource(R.string.description),
                value = it,
                icon = Icons.Filled.HourglassBottom
            )
        }
        bookDetails.publicationDate?.let {
            InfoRow(
                title = stringResource(R.string.publication_date),
                value = it.toString(),
                icon = Icons.Filled.AccountTree
            )
        }
        InfoRow(
            title = stringResource(R.string.id),
            value = bookDetails.listId.toString(),
            icon = Icons.Filled.Category
        )
    }
}

@Composable
fun BookDetailsSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmer()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )

        Spacer(modifier = Modifier.height(16.dp))

        repeat(3) {
            InfoRowSkeleton()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )

        repeat(3) {
            InfoRowSkeleton()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )

        repeat(2) {
            InfoRowSkeleton()
        }
    }
}

@Preview
@Composable
private fun SuccessPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Success(BookDetails.mock()),
        onRetry = {},
        onNavigationBack = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Loading,
        onRetry = {},
        onNavigationBack = {}
    )
}

@Preview
@Composable
private fun ErrorPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Error(null),
        onRetry = {},
        onNavigationBack = {}
    )
}
