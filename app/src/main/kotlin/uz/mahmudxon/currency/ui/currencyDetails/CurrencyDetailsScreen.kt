package uz.mahmudxon.currency.ui.currencyDetails

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.ui.component.chart.LineChart
import uz.mahmudxon.currency.ui.component.chart.LineChartData
import uz.mahmudxon.currency.ui.component.chart.common.animation.simpleChartAnimation
import uz.mahmudxon.currency.ui.component.chart.renderer.line.GradientLineShader
import uz.mahmudxon.currency.ui.component.chart.renderer.line.SolidLineDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.point.NoPointDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.xaxis.SimpleXAxisDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.yaxis.SimpleYAxisDrawer
import uz.mahmudxon.currency.ui.component.error.NetworkErrorScreenWithButton
import uz.mahmudxon.currency.util.toMoneyString

@Composable
fun CurrencyDetailsScreen(
    state: CurrencyDetailsState,
    onEvent: (CurrencyDetailsEvent) -> Unit,
    onBackClick: () -> Unit
) {

    if (state.error != -1) {
        NetworkErrorScreenWithButton {
            onEvent.invoke(CurrencyDetailsEvent.Refresh)
        }
        return
    }

    val points = state.chart.map {
        LineChartData.Point(it.rate.toFloat(), it.date)
    }

    val data = LineChartData(
        points = points, lineDrawer = SolidLineDrawer(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp
        )
    )
    LazyColumn {
        item {
            ToolbarDetailsScreen(state, onBackClick)
        }
        item {
            LineChart(
                linesChartData = listOf(data),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(start = 24.dp, top = 8.dp, end = 36.dp, bottom = 8.dp),
                animation = simpleChartAnimation(),
                pointDrawer = NoPointDrawer,
                lineShader = GradientLineShader(
                    colors = listOf(MaterialTheme.colorScheme.primaryContainer, Color.Transparent)
                ),
                xAxisDrawer = SimpleXAxisDrawer(
                    axisLineColor = MaterialTheme.colorScheme.onBackground,
                    labelTextColor = MaterialTheme.colorScheme.onBackground,
                    labelRatio = points.size - 1
                ),
                yAxisDrawer = SimpleYAxisDrawer(
                    axisLineColor = MaterialTheme.colorScheme.onBackground,
                    labelTextColor = MaterialTheme.colorScheme.onBackground,
                    labelRatio = 5,
                    labelValueFormatter = {
                        if (it > 100) {
                            val value = it.toInt()
                            "$value"
                        } else "$it"
                    }
                ),
                horizontalOffset = 0f
            )
        }

        item {
            ConvertorScreen(
                currencyName = state.currency?.name ?: "",
                currencyCode = state.currency?.code ?: "",
                isForeignCurrencyTop = state.isForeignCurrencyTop,
                localValue = state.localValue,
                foreignValue = state.foreignValue,
                localInput = {
                    onEvent.invoke(CurrencyDetailsEvent.LocalCurrencyInput(it))
                },
                foreignInput = {
                    onEvent.invoke(CurrencyDetailsEvent.ForeignCurrencyInput(it))
                },
                onSwitchClick = {
                    onEvent.invoke(CurrencyDetailsEvent.SwitchCurrency)
                }
            )
        }

        items(state.bankPrices, key = { it.bank.id }) {
            BankCurrencyPriceItem(item = it) {}
        }
    }
}

@Composable
fun ToolbarDetailsScreen(state: CurrencyDetailsState, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .clickable { onBackClick.invoke() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${state.currency?.name}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "${state.currency?.code}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        AsyncImage(
            model = "file:///android_asset/flags/${state.currency?.code?.lowercase()}.webp",
            contentDescription = "${state.currency?.code}",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.BankCurrencyPriceItem(item: BankPrice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .animateItemPlacement()
            .clickable { onClick.invoke() },
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = item.bank.logo,
                    contentDescription = item.bank.name,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = item.bank.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.buy),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = if (item.buy == 0.0) "-" else "${item.buy.toMoneyString()} so'm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.sell),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = if (item.sell == 0.0) "-" else "${item.sell.toMoneyString()} so'm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ConvertorScreen(
    currencyName: String, currencyCode: String,
    isForeignCurrencyTop: Boolean,
    localValue: String,
    foreignValue: String,
    localInput: (String) -> Unit,
    foreignInput: (String) -> Unit,
    onSwitchClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {

        Text(
            text = if (isForeignCurrencyTop) currencyName else stringResource(id = R.string.uzs_full_name),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(end = 16.dp)
        )

        Crossfade(targetState = isForeignCurrencyTop, label = "ConvertorScreen") { isTop ->
            if (isTop) {
                ConvertorScreenItem(
                    currencyCode = currencyCode,
                    currencyValue = foreignValue,
                    onEdit = foreignInput
                )
            } else {
                ConvertorScreenItem(
                    currencyCode = "UZS",
                    currencyValue = localValue,
                    onEdit = localInput
                )
            }
        }

        IconButton(onClick = onSwitchClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_swap_vert),
                contentDescription = "swap",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = if (!isForeignCurrencyTop) currencyName else stringResource(id = R.string.uzs_full_name),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(end = 16.dp)
        )

        Crossfade(targetState = isForeignCurrencyTop, label = "ConvertorScreen") { isTop ->
            if (!isTop) {
                ConvertorScreenItem(
                    currencyCode = currencyCode,
                    currencyValue = foreignValue,
                    onEdit = foreignInput
                )
            } else {
                ConvertorScreenItem(
                    currencyCode = "UZS",
                    currencyValue = localValue,
                    onEdit = localInput
                )
            }
        }


    }
}

@Composable
fun ConvertorScreenItem(
    currencyCode: String,
    currencyValue: String,
    onEdit: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
            AsyncImage(
                model = "file:///android_asset/flags/${currencyCode.lowercase()}.webp",
                contentDescription = currencyCode,
                modifier = Modifier.size(24.dp)
            )

            TextField(
                modifier = Modifier.weight(1f),
                value = TextFieldValue(
                    text = currencyValue,
                    selection = TextRange(currencyValue.length)
                ),
                onValueChange = {
                    onEdit.invoke(it.text)
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.End,
                ),
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
    }
}