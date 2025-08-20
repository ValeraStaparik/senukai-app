package com.senukai.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.senukai.app.presentation.features.bookList.BookListScreen
import com.senukai.app.presentation.features.book_details.BookDetailsScreen
import com.senukai.app.presentation.features.reading_book_status.BooksListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReadingStatusBookList
    ) {
        composable<ReadingStatusBookList> {
            BooksListScreen(
                onNavigateToBookList = { name ->
                    navController.navigate(BookListRoute(name))
                },
                onNavigateToBookDetails = { name ->
                    navController.navigate(BookDetailsRoute(name))
                }
            )
        }

        composable<BookListRoute> {
            BookListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToBook = { name ->
                    navController.navigate(BookDetailsRoute(name))
                }
            )
        }

        composable<BookDetailsRoute> { backStackEntry ->
            BookDetailsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
