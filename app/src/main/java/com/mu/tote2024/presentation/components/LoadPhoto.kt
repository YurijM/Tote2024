package com.mu.tote2024.presentation.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.mu.tote2024.R

@Composable
fun LoadPhoto(
    photoUrl: String = "",
    onClick: (uri: Uri?) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = modifier.padding(end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
        }
        Box(
            modifier = Modifier
                .width(148.dp)
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap.value != null) {
                Image(
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.size(148.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                if (photoUrl.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            //.size(148.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    ) {
                        val state = painter.state
                        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.fillMaxSize(.25f)
                                )
                            }
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.noname),
                        contentDescription = "gambler",
                        //modifier = Modifier.size(124.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedButton(
            onClick = {
                launcher.launch("image/*")
                onClick(imageUri)
            },
            contentPadding = PaddingValues(
                top = 0.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 0.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.load_photo),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}