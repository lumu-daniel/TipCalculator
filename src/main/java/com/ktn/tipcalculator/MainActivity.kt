package com.ktn.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ktn.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                TipTimeScreen()
            }
        }
    }
}

@Preview
@Composable
fun TipTimeScreen(){
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var isRounded by remember { mutableStateOf(false) }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercentage = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount,tipPercentage, isRounded)
    Column (
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) ){
        Text(
            text = stringResource(R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(value = amountInput,
            valueTip = tipInput,
            onValueChange = {amountInput = it},
            onValueChangeTip = {tipInput = it},
            onCheckedChange = {isRounded = it}
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.tip_amount, tip)
        )
    }
}

@Composable
fun EditNumberField(value: String
                    , valueTip: String
                    , onValueChange:(String) -> Unit
                    , onValueChangeTip:(String) -> Unit
                    , onCheckedChange: (Boolean)-> Unit){
    Row(
    ) {
        Text(
            text = stringResource(R.string.rounded),
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterVertically).weight(7f)
        )
        Switch(
            checked = false,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = valueTip,
        onValueChange = onValueChangeTip,
        label = {
            Text(
                text = stringResource(R.string.tip_percentage),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(R.string.bill_amount),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, isRounded: Boolean): String{
    val tip = tipPercent/100.0 * amount
    return if(isRounded) tip.roundToInt().toString() else NumberFormat.getCurrencyInstance(Locale.US).format(tip)
}

fun findOriginalArray(changed: IntArray): IntArray {
    var len = changed.size
    var hlfArray = IntArray(len/2)
    if(changed[(len/2)]/2==changed[0]){
        for(i in 0 until len/2){
            if(changed[i]!=changed[i+(len/2)]/2){
                return IntArray(0)
            }
            hlfArray[i] = changed[i]
        }
        return hlfArray
    }else{
        return IntArray(0)
    }
}