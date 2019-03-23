import java.time.{Instant, ZoneId, ZonedDateTime}

import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.PropertyChecks

import scala.collection.JavaConverters._

/**
  * This property-test will show that timezones in java.time are not symmetrical, as "GMT0" is printed when
  * formatting an ISO8601 String, but cannot be parsed again.
  *
  * {{{
  *    DateTimeParseException was thrown during property evaluation.
  *    Message: Text '1970-01-01T00:00Z[GMT0]' could not be parsed, unparsed text found at index 17
  *    Occurred when passed generated values (
  *      arg0 = GMT0
  *    )
  * }}}
  *
  * Note that "GMT0" is deprecated according to Wikipedia, so this is unlikely to be an issue for most users
  * of `java.time`.
  *
  * @see https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
  */
class TimezoneSpec extends WordSpec with MustMatchers with PropertyChecks {

  override implicit val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 10000)

  val availableTimeZones = ZoneId.getAvailableZoneIds.asScala.toSeq.map(idStr => ZoneId.of(idStr))
  val fixedDateTime      = Instant.EPOCH

  val genZoneId: Gen[ZoneId] = Gen.oneOf(availableTimeZones)

  "Available java.time timezones" must {
    "be symmetrical" when {
      "formatting/parsing as ISO8601" in {
        forAll(genZoneId) { timezone =>
          val initialDateTime: ZonedDateTime = fixedDateTime.atZone(timezone)

          // roundtrip to test symmetry between formatting/parsing
          val isoString      = initialDateTime.toString
          val parsedDateTime = ZonedDateTime.parse(isoString)

          parsedDateTime mustBe initialDateTime
        }
      }
    }
  }
}
