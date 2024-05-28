package uz.mahmudxon.currency.ui.currencyDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.model.BankPrice
import uz.mahmudxon.currency.util.toMoneyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceInfoBottomSheet(
    cbuPrice: Double,
    bankPrice: BankPrice,
    onDismissRequest: () -> Unit,
    openWebsiteRequest: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            AsyncImage(
                model = bankPrice.bank.logo,
                contentDescription = bankPrice.bank.name,
                modifier = Modifier
                    .size(56.dp)
                    .padding(16.dp)
            )

            Text(
                text = bankPrice.bank.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(id = R.string.bank_price_will_be_different_waring),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.cbu_price),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "${cbuPrice.toMoneyString()} so'm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.3f)
                            .padding(top = 8.dp, bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.buy),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = if (bankPrice.buy == 0.0) "-" else "${bankPrice.buy.toMoneyString()} so'm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.3f)
                            .padding(top = 8.dp, bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.sell),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Text(
                        text = if (bankPrice.sell == 0.0) "-" else "${bankPrice.sell.toMoneyString()} so'm",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                }

            }

            Button(
                onClick = {
                    openWebsiteRequest.invoke(bankPrice.bank.website)
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = stringResource(id = R.string.goto_bank_website),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }
}