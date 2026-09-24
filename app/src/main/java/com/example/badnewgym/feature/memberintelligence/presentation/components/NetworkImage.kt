package com.example.badnewgym.feature.memberintelligence.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun RemoteOrAssetImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallbackModel: Any? = null
) {
    val context = LocalContext.current
    val effectiveModel = model?.takeIf { it !is String || it.isNotBlank() } ?: fallbackModel
    val imageRequest = androidx.compose.runtime.remember(effectiveModel, fallbackModel) {
        ImageRequest.Builder(context)
            .data(effectiveModel)
            .apply {
                (fallbackModel as? Int)?.let {
                    error(it)
                    fallback(it)
                }
            }
            .crossfade(true)
            .build()
    }
    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}

@Composable
fun AvatarImage(
    model: Any?,
    size: Dp = 48.dp,
    width: Dp = size,
    height: Dp = size,
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(14.dp),
    placeholderTint: Color = Color(0xFF334155),
    fallbackModel: Any? = null
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(placeholderTint),
        contentAlignment = Alignment.Center
    ) {
        val effectiveModel = model?.takeIf { it !is String || it.isNotBlank() } ?: fallbackModel
        if (effectiveModel == null) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(minOf(width, height) * 0.45f)
            )
        } else {
            RemoteOrAssetImage(
                model = effectiveModel,
                fallbackModel = fallbackModel,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
