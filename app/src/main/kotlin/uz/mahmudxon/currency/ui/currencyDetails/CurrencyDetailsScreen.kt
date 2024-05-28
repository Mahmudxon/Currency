package uz.mahmudxon.currency.ui.currencyDetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.ui.component.chart.LineChart
import uz.mahmudxon.currency.ui.component.chart.LineChartData
import uz.mahmudxon.currency.ui.component.chart.common.animation.simpleChartAnimation
import uz.mahmudxon.currency.ui.component.chart.renderer.line.GradientLineShader
import uz.mahmudxon.currency.ui.component.chart.renderer.line.SolidLineDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.point.NoPointDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.xaxis.SimpleXAxisDrawer
import uz.mahmudxon.currency.ui.component.chart.renderer.yaxis.SimpleYAxisDrawer
import uz.mahmudxon.currency.ui.component.error.NetworkErrorScreenWithButton

@Composable
fun CurrencyDetailsScreen(
    state: CurrencyDetailsState,
    onEvent: (CurrencyDetailsEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
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
            ConvertorView(
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

        if (state.sellingAvailable && state.bestOfferForSelling.price != 0.0) {
            item {
                BestOfferView(
                    title = stringResource(id = R.string.best_offer_for_selling),
                    bestOffer = state.bestOfferForSelling
                )
            }
        }

        if (state.sellingAvailable && state.bestOfferForBuying.price != 0.0) {
            item {
                BestOfferView(
                    title = stringResource(id = R.string.best_offer_for_buying),
                    bestOffer = state.bestOfferForBuying
                )
            }
        }
        items(state.bankPrices, key = { it.bank.id }) {
            BankCurrencyPriceItemView(item = it) {
                onEvent.invoke(CurrencyDetailsEvent.CurrencyPriceClick(it))
            }
        }
    }

    if (state.showBankPrice != null) {
        PriceInfoBottomSheet(
            cbuPrice = state.currency?.rate ?: 0.0,
            bankPrice = state.showBankPrice,
            onDismissRequest = {
                onEvent.invoke(CurrencyDetailsEvent.OnCurrencyInfoDismissRequest)
            }) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            context.startActivity(intent)
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




