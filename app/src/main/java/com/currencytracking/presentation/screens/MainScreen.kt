package com.currencytracking.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencytracking.R
import com.currencytracking.domain.SortDirection
import com.currencytracking.domain.SortField
import com.currencytracking.domain.SortType
import com.currencytracking.domain.model.ICurrencyItem
import com.currencytracking.presentation.CurrencyTrackingViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val viewModel: CurrencyTrackingViewModel = hiltViewModel()
    val baseCurrency by viewModel.baseCurrency.collectAsState()
    val currencySymbols by viewModel.currencySymbols.collectAsState(emptyList())
    val currenciesPopular by viewModel.currencies.collectAsState()
    val currenciesFavorite by viewModel.currenciesFavourite.collectAsState()

    val sortTypeCurrency by viewModel.sortType.collectAsState()
    val scope = rememberCoroutineScope()
    var page by remember { mutableStateOf(Pages.POPULAR) }

    Scaffold(topBar = {
        TopAppBar {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)) {
                Box(modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 10.dp)) {
                    BaseCurrencyDropdownMenu(baseCurrency = baseCurrency,
                        currencySymbols = currencySymbols,
                        viewModel::setBaseCurrency)
                }
                IconButton(modifier = Modifier.padding(top = 10.dp),
                    onClick = { scope.launch { viewModel.updateCurrencyValues() } }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_update),
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "update"
                    )
                }
                Box(modifier = Modifier
                    .wrapContentWidth()) {
                    PrimaryText(text = stringResource(R.string.title))
                }
                Box(modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 10.dp, start = 10.dp)) {
                    SortTypeDropdownMenu(sortTypeCurrency, viewModel::setSortType)
                }
            }
        }
    },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(10.dp)
                ) {
                    Button(modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                        onClick = { page = Pages.POPULAR },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Popular", color = MaterialTheme.colors.surface)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                        onClick = {
                            page = Pages.FAVOURITE
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Favorite", color = MaterialTheme.colors.surface)
                    }
                }
            }
        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            CurrencyListContent(page = page, currenciesPopular, currenciesFavorite,
                onFavoriteChosen = { scope.launch { viewModel.switchFavourite(it) } })
        }
    }
}

@Composable
fun BaseCurrencyDropdownMenu(
    baseCurrency: String,
    currencySymbols: List<String>,
    setBaseCurrency: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.width(70.dp),
        onClick = { expanded = true },
        contentPadding = PaddingValues(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        )
    ) {
        SecondaryText(text = baseCurrency)
        Icon(Icons.Outlined.ArrowDropDown, contentDescription = "show currency symbols")
    }
    DropdownMenu(expanded = expanded, modifier = Modifier
        .wrapContentWidth(),
        onDismissRequest = { expanded = false }) {
        currencySymbols.forEachIndexed { _, value ->
            DropdownMenuItem(onClick = {
                setBaseCurrency(value)
                expanded = false
            }) {
                val isSelected = value == baseCurrency
                Text(text = value, style = getStyleText(isSelected))
            }
        }
    }
}

@Composable
fun SortTypeDropdownMenu(
    sortTypeCurrency: SortType,
    setSortType: (SortType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.width(80.dp),
        onClick = { expanded = true },
        contentPadding = PaddingValues(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        )
    ) {
        SecondaryText(text = sortTypeCurrency.sortField.name.replace("_", " "))
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ArrowDropDown, contentDescription = "show sort type")
    }
    DropdownMenu(expanded = expanded, modifier = Modifier.wrapContentWidth(),
        onDismissRequest = { expanded = false }) {
        SortType.values().filter { it != SortType.UNKNOWN }.forEach { sortType ->
            DropdownMenuItem(onClick = {
                expanded = false
                setSortType(sortType)
            }) {
                val isSelected = sortType.sortField.name == sortTypeCurrency.sortField.name &&
                        sortType.direction.name == sortTypeCurrency.direction.name
                Text(
                    text = stringResource(
                        id = if (sortType.sortField == SortField.BY_NAME) R.string.by_name
                        else R.string.by_value
                    ),
                    modifier = Modifier.padding(10.dp),
                    style = getStyleText(isSelected)
                )
                when (sortType.direction) {
                    SortDirection.ASCENDING -> Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        tint = getColor(isSelected),
                        contentDescription = "Sort direction"
                    )
                    SortDirection.DESCENDING -> Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        tint = getColor(isSelected),
                        contentDescription = "Sort direction"
                    )
                    else -> {}
                }
            }
            Divider()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CurrencyListContent(
    page: Pages,
    currencies: List<ICurrencyItem>,
    curDataFavourite: List<ICurrencyItem>,
    onFavoriteChosen: (currency: ICurrencyItem) -> Unit
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        count = Pages.values().size - 1, state = pagerState
    ) {
        when (it) {
            0 -> {
                CurrencyList(currencies,
                    onFavoriteChosen = onFavoriteChosen
                )
            }
            1 -> {
                CurrencyList(
                    curDataFavourite, onFavoriteChosen = onFavoriteChosen
                )
            }
            else -> Pages.UNDEFINED
        }
    }

    LaunchedEffect(key1 = page) {
        pagerState.scrollToPage(
            when (page) {
                Pages.POPULAR -> 0
                Pages.FAVOURITE -> 1
                else -> 0
            }
        )
    }
}

@Composable
fun CurrencyList(currencies: List<ICurrencyItem>,
                 onFavoriteChosen: (currency: ICurrencyItem) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        this.items(currencies) { currencyItem ->
            Row(modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(currencyItem.name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W200)
                Text(currencyItem.value.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W200)
                Spacer(modifier = Modifier.width(25.dp))
                IconButton(modifier = Modifier.padding(end = 10.dp),
                    onClick = { onFavoriteChosen(currencyItem) }) {
                    Icon(
                        painterResource(
                            id = if (currencyItem.isFavourite) R.drawable.ic_selected_favorite
                            else R.drawable.ic_not_selected_favorite
                        ), tint = MaterialTheme.colors.primary, contentDescription = "Is favorite"
                    )
                }
            }
        }
    }
}

@Composable
private fun getStyleText(isSelected: Boolean): TextStyle {
    return if (isSelected) {
        MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Bold,
            color = getColor(isSelected)
        )
    } else {
        MaterialTheme.typography.body1.copy(
            fontWeight = FontWeight.Normal,
            color = getColor(isSelected)
        )
    }
}

@Composable
private fun getColor(isSelected: Boolean): Color {
    return if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface
    }
}

@Composable
fun PrimaryText(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 21.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun SecondaryText(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.surface
    )
}

enum class Pages {
    UNDEFINED, POPULAR, FAVOURITE
}