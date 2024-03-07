package fi.solita.hrnd.domain.utils

import fi.solita.hrnd.core.utils.parseToLocalDateTime
import io.kotest.matchers.shouldBe
import kotlinx.datetime.*
import kotlin.test.Test


class DateTimeUtilTest {

    @Test
    fun shouldParseCorrectLocalDateTime() {
        val foo = "2024-01-01T15:34:41Z".parseToLocalDateTime()
        println(foo.toString())
        foo.toString() shouldBe LocalDateTime(
            year = 2024,
            month = Month.JANUARY,
            dayOfMonth = 1,
            hour = 15,
            minute = 34,
            second = 41
        ).toString().also { println(it) }
    }

    @Test
    fun shouldGetCorrectAgeYears() {
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
    fun shouldGetCorrectAgeMonths() {
        // Given
        val born2020 = "01/01/2020"
        val today2020 = LocalDateTime.parse("2020-06-01T22:19:44").date

        // When
        val ageInMonths = getAge(born2020, today2020)

        // Then
        ageInMonths?.months shouldBe 5
    }
}