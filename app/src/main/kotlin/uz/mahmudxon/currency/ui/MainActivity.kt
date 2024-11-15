package uz.mahmudxon.currency.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import uz.mahmudxon.currency.R
import uz.mahmudxon.currency.ui.navigation.ApplicationNavGraph
import uz.mahmudxon.currency.ui.theme.ContrastAwareCurrencyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Bad Idea
    lateinit var currencyNameMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        // I know bro, I know :)
        val codes = resources.getStringArray(R.array.currency_codes)
        val names = resources.getStringArray(R.array.currency_names)
        currencyNameMap = codes.zip(names).toMap()
        setContent {
            ContrastAwareCurrencyTheme(dynamicColor = true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                ) {
                    ApplicationNavGraph()
                }
            }
        }
    }
}