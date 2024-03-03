package fi.solita.hrnd.designSystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.solita.hrnd.domain.utils.getAgeAndDateTimeUnit
import hrnd.composeapp.generated.resources.Res
import hrnd.composeapp.generated.resources.*
import kotlinx.datetime.DateTimeUnit
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

enum class GenderAndAgeRowSize{
    BIG, SMALL
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GenderAndAgeRow(gender: String?, dateOfBirth: String?, modifier: Modifier = Modifier, size: GenderAndAgeRowSize) {
    val ageAndDateTimeUnit = getAgeAndDateTimeUnit(dateOfBirth)
    val age: String = when (ageAndDateTimeUnit?.second) {
        DateTimeUnit.YEAR -> {
            stringResource(Res.string.years_old,ageAndDateTimeUnit.first.toString())
        }
        DateTimeUnit.MONTH -> {
            stringResource(Res.string.months_old,ageAndDateTimeUnit.first.toString())
        }
        DateTimeUnit.WEEK -> {
            stringResource(Res.string.weeks_old,ageAndDateTimeUnit.first.toString())
        }
        else -> stringResource(Res.string.unknown_age)
    }
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if(size == GenderAndAgeRowSize.SMALL) 8.dp else 12.dp)
    ) {
        Text(gender ?: stringResource(Res.string.unknown_gender), style = if(size == GenderAndAgeRowSize.SMALL) MaterialTheme.typography.body1 else MaterialTheme.typography.h3)
        DecorativeBall(
            Modifier.size(if(size == GenderAndAgeRowSize.SMALL) 12.dp else 16.dp),
            color = MaterialTheme.colors.primaryVariant
        )
        Text(text = age, style = if(size == GenderAndAgeRowSize.SMALL) MaterialTheme.typography.body1 else MaterialTheme.typography.h3)
    }
}
