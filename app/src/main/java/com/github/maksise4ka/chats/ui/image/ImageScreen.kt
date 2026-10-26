package com.github.maksise4ka.chats.ui.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.SubcomposeAsyncImage
import com.github.maksise4ka.chats.R
import com.github.maksise4ka.chats.data.remote.ImageUrls
import org.kodein.di.compose.rememberInstance

@Composable
fun ImageScreen(
    link: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageUrls: ImageUrls by rememberInstance()

    Surface(modifier = modifier.fillMaxSize()) {
        Box {
            SubcomposeAsyncImage(
                model = imageUrls.full(link),
                contentDescription = stringResource(R.string.image),
                modifier = Modifier.fillMaxSize(),
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.image_load_error))
                    }
                },
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.close),
                )
            }
        }
    }
}
