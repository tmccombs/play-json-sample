package com.lucidchart.jsonsample

import play.api.libs.json._
import play.api.libs.json.Reads.JsObjectReducer
import play.api.libs.functional.syntax._

import scala.util.matching.Regex

/**
 * Created by thayne on 1/5/15.
 */
object Transforms {

  val inputJSon = Json.parse(
    """{
      "id": 468934,
      "email": "person@example.com",
      "first_name": "John",
      "last_name": "Doe",
      "super_secret_info": "A secret",
      "phone_number": "1234567890",
      "work": {
        "position": "Engineer",
        "employer": "Some Company",
        "years": 5.6
      }
    }""")

  /**
   * Remove the super_secret_info
   */
  val removeSecret: Reads[JsObject] = (__ \ 'super_secret_info).json.prune
  /**
   * Format we want:
   * {
   *   "name": "John Doe",
   *   "contact": {
   *     "email": "person@example.com",
   *     "phone": "(123)-456-7890"
   *   },
   *   "work": {
   *     "position": "Engineer',
   *     "employer": "Some Company",
   *     "years": 5.6
   *   }
   * }
   */
  val simplify: Reads[JsObject] = (
    ((__ \ 'name).json.pickBranch orElse (__ \ 'name).json.copyFrom(for(firstName <- (__ \ 'first_name).read[String]; lastName <- (__ \ 'last_name).read[String]) yield {
      JsString(s"$firstName $lastName")
    })) and
    (__ \ 'email).json.pickBranch(Reads.email.map(JsString)) and
    (__ \ 'phone).json.copyFrom((__ \ 'phone_number).read(Reads.pattern(new Regex("\\d{10}")).map { s =>
      JsString("(%s)-%s-%s".format(s.substring(0, 3), s.substring(3,6), s.substring(6)))
    })) and
    (__ \ "work").json.pickBranch
    ).reduce


  def main(args: Array[String]) {
    println("Remove secret info:")
    println(inputJSon.transform(removeSecret))

    println("\nSimplify:")
    println(inputJSon.transform(simplify))
  }

}
