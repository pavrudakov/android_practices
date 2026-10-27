package com.example.android_practice.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android_practice.domain.model.Brewery
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.FilterField
import com.example.android_practice.ui.common.UiState
import com.example.android_practice.ui.list.components.BreweryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreweryListScreen(
    onBreweryClick: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BreweryListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.filterSettings.collectAsState()
    val showBadge by viewModel.showBadge.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Пивоварни США",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            actions = {
                IconButton(onClick = onNavigateToSettings) {
                    BadgedBox(
                        badge = {
                            if (showBadge) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(8.dp)
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтры"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        // Active Filter Chips Row
        if (!settings.isDefault()) {
            ActiveFiltersRow(
                settings = settings,
                onChipClick = onNavigateToSettings,
                onClearFilter = { field -> viewModel.clearSingleFilter(field) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyListContent(
                            isFiltered = !settings.isDefault(),
                            onResetFilters = { viewModel.resetAllFilters() },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        BreweryListContent(
                            breweries = state.data,
                            onBreweryClick = onBreweryClick
                        )
                    }
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBreweries() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActiveFiltersRow(
    settings: FilterSettings,
    onChipClick: () -> Unit,
    onClearFilter: (FilterField) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (settings.state.isNotBlank()) {
            item {
                InputChip(
                    selected = true,
                    onClick = onChipClick,
                    label = { Text("Штат: ${settings.state}") },
                    trailingIcon = {
                        IconButton(
                            onClick = { onClearFilter(FilterField.STATE) },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Очистить штат"
                            )
                        }
                    }
                )
            }
        }
        if (settings.type.isNotBlank()) {
            item {
                InputChip(
                    selected = true,
                    onClick = onChipClick,
                    label = { Text("Тип: ${settings.type}") },
                    trailingIcon = {
                        IconButton(
                            onClick = { onClearFilter(FilterField.TYPE) },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Очистить тип"
                            )
                        }
                    }
                )
            }
        }
        if (settings.name.isNotBlank()) {
            item {
                InputChip(
                    selected = true,
                    onClick = onChipClick,
                    label = { Text("Название: ${settings.name}") },
                    trailingIcon = {
                        IconButton(
                            onClick = { onClearFilter(FilterField.NAME) },
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Очистить название"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyListContent(
    isFiltered: Boolean,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SportsBar,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isFiltered) "Ничего не найдено по выбранным фильтрам" else "Пивоварен не найдено",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isFiltered) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onResetFilters) {
                Text("Сбросить фильтры")
            }
        }
    }
}

@Composable
private fun BreweryListContent(
    breweries: List<Brewery>,
    onBreweryClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = breweries,
            key = { it.id }
        ) { brewery ->
            BreweryCard(
                brewery = brewery,
                onClick = { onBreweryClick(brewery.id) }
            )
        }
    }
}
