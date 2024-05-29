package com.example.amphibians.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.amphibians.R
import com.example.amphibians.model.Amphibian
import com.example.compose.AmphibiansTheme

@Composable
fun HomeScreen(
    amphibiansUiState: AmphibianUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (amphibiansUiState) {
        is AmphibianUiState.Loading ->
            LoadingScreen(
                modifier.fillMaxSize()
            )
        is AmphibianUiState.Success ->
            SuccessScreen(
                amphibians = amphibiansUiState.amphibians,
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        is AmphibianUiState.Error ->
            ErrorScreen(
                retryAction = retryAction,
                modifier.fillMaxSize()
            )
    }
}

@Composable
fun SuccessScreen(
    amphibians: List<Amphibian>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = amphibians,
            key = { amphibian -> amphibian.name }
        ) {
            AmphibianCard(amphibian = it, modifier = Modifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    var showCardInfo by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        onClick = { showCardInfo = !showCardInfo }
    ) {
        AmphibianCardImage(
            amphibianImage = amphibian.imgSrc,
            amphibianName = amphibian.name
        )
        if (showCardInfo) AmphibianCardText(
            amphibianName = amphibian.name,
            amphibianType = amphibian.type,
            amphibianDescription = amphibian.description,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun AmphibianCardImage(
    amphibianImage: String,
    amphibianName: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(amphibianImage)
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = stringResource(R.string.amphibian_photo, amphibianName),
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun AmphibianCardText(
    amphibianName: String,
    amphibianType: String,
    amphibianDescription: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "$amphibianName ($amphibianType)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = amphibianDescription,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.loading),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.error),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(onClick = retryAction) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Preview
@Composable
fun SuccessScreenPreview() {
    val mockData = listOf(
        Amphibian(
            name = "Amphibian 1",
            type = "Toad",
            description = "this is a test",
            imgSrc = "",
        ),
        Amphibian(
            name = "Amphibian 2",
            type = "Salamander",
            description = "this is a test",
            imgSrc = "",
        )
    )
    AmphibiansTheme {
        SuccessScreen(
            amphibians = mockData,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(
        retryAction = {},
        modifier = Modifier.fillMaxSize()
    )
}
