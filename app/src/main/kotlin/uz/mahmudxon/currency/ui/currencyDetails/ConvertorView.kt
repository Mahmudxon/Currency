package uz.mahmudxon.currency.ui.currencyDetails

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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


@Composable
fun ConvertorView(
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