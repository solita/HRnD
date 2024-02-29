package fi.solita.hrnd.domain.utils

import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test


class DateTimeUtilTest {
    @Test
    fun shouldGetCorrectAgeYears(){
        // Given
        val born2010 = "01/01/2010"
        val born2005 = "05/05/2005"

        val today2020 = LocalDateTime.parse("2020-06-01T22:19:44").date
        val today2006 = LocalDateTime.parse("2006-06-01T22:19:44").date
        val today2011 = LocalDateTime.parse("2011-06-01T22:19:44").date

        // When
        val age2010 = getAge(born2010, today2020)
        val age2005 = getAge(born2005, today2020)
        val age2006 = getAge(born2005, today2006)
        val age2011 = getAge(born2010, today2011)

        // Then
        age2010?.years shouldBe 10
        age2005?.years shouldBe 15
        age2006?.years shouldBe 1
        age2011?.years shouldBe 1
    }

    @Test
    fun shouldGetCorrectAgeMonths(){
        // Given
        val born2020 = "01/01/2020"
        val today2020 = LocalDateTime.parse("2020-06-01T22:19:44").date

        // When
        val ageInMonths = getAge(born2020, today2020)

        // Then
        ageInMonths?.months shouldBe 5
    }
}