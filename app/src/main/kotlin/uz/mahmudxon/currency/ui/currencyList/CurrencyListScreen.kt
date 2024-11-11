package uz.mahmudxon.currency.ui.currencyList

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.model.Currency
import uz.mahmudxon.currency.ui.component.error.NetworkErrorScreenWithButton
import uz.mahmudxon.currency.util.getCurrencyName
import uz.mahmudxon.currency.util.toMoneyString


@Composable
fun CurrencyListScreen(
    state: CurrencyListState,
    selectedCurrency: Currency?,
    onEvent: (CurrencyListEvent) -> Unit,
    navigateDetails: (Currency) -> Unit
) {
    Crossfade(targetState = state.isLoading, label = "CurrencyListScreen") {
        if (it) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center),
                    strokeWidth = 4.dp
                )
            }
        } else if (state.errorCode != -1) {
            NetworkErrorScreenWithButton {
                onEvent.invoke(CurrencyListEvent.OnRefresh)
            }
        } else {
            Column(
                modifier = Modifier.safeDrawingPadding()
            ) {
                CurrencySearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp, vertical = 4.dp), state.searchQuery
                ) {
                    onEvent.invoke(
                        CurrencyListEvent.Search(it)
                    )
                }

                LazyColumn {
                    items(state.currencies, key = { it.code })
                    {
                        CurrencyItem(
                            item = it,
                            isSelected = it.code == selectedCurrency?.code
                        ) {
                            onEvent.invoke(CurrencyListEvent.SelectCurrency(it))
                            navigateDetails.invoke(it)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CurrencySearchBar(modifier: Modifier, searchQuery: String, onEdit: (String) -> Unit) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search), contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextField(
                value = searchQuery,
                onValueChange = onEdit,
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_currency),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@Composable
fun LazyItemScope.CurrencyItem(item: Currency, isSelected: Boolean = false, onClick: () -> Unit) {
    val iconPath = "file:///android_asset/flags/${item.code.lowercase()}.webp"
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .animateItem(
                fadeInSpec = null,
                fadeOutSpec = null
            )
            .clickable { onClick.invoke() },
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val icon: Int
            var color: Color
            val c = item.diff.firstOrNull() ?: ' '
            when (c) {
                '+' -> {
                    color = Color(0xFF4CAF50)
                    icon = R.drawable.ic_trending_up
                }

                '-' -> {
                    color = Color(0xFFF44336)
                    icon = R.drawable.ic_trending_down
                }

                else -> {
                    color = contentColor
                    icon = R.drawable.ic_trending_flat
                }
            }

            if (isSelected) color = contentColor.copy(alpha = 0.6f)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = iconPath,
                    contentDescription = item.code,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = item.code,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                )

                Icon(painter = painterResource(id = icon), contentDescription = null, tint = color)
            }

            Text(
                text = getCurrencyName(item),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.n_summ_value, item.rate.toMoneyString()),
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.diff,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color,
                )
            }
        }
    }
}