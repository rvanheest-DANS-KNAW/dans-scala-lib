package nl.knaw.dans.lib.string

import org.scalatest.{ FlatSpec, Matchers, OptionValues }

class StringExtensionsSpec extends FlatSpec with Matchers with OptionValues {

  "toOption" should "return an Option.empty when given an empty String" in {
    "".toOption shouldBe empty
  }

  it should "return an Option.empty when the input is null" in {
    (null: String).toOption shouldBe empty
  }

  it should "return an Option.empty when given a String with spaces only" in {
    "   ".toOption shouldBe empty
  }

  it should "return an Option.empty when given a String with tabs, spaces, newlines, etc" in {
    " \t  \r  \t \n  ".toOption shouldBe empty
  }

  it should "return the original String wrapped in Option when given a non-blank String" in {
    "abc".toOption.value shouldBe "abc"
  }

  it should "return the original String when the input contains both blank and non-blank characters" in {
    val input = "ab c "
    input.toOption.value shouldBe input
  }
}
