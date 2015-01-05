package com.lucidchart.jsonsample

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by thayne on 1/3/15.
 */
object PersonSample {

  case class Person(name: String, age: Int, spouse: Option[Person], children: Seq[Person])

  implicit val personReader: Reads[Person] = (
    (__ \ "name").read[String] and
    (__ \ "age").read[Int] and
    (__ \ "spouse").lazyReadNullable(personReader) and
    (__ \ "children").lazyReadNullable(Reads.seq[Person](personReader)).map(_.getOrElse(Seq()))
    )(Person)

  implicit val personWriter: OWrites[Person] = (
    (__ \ "name").write[String] and
    (__ \ "age").write[Int] and
    (__  \ "spouse").lazyWriteNullable(personWriter) and
    (__ \ "children").lazyWriteNullable(Writes.seq(personWriter)).contramap({ children: Seq[Person] =>
      if (children.isEmpty) {
        None
      } else {
        Some(children)
      }
    })
    )(unlift(Person.unapply))

  val personFormat: OFormat[Person] = (
    (__ \ "name").format[String] and
    (__ \ "age").format[Int] and
    (__ \ "spouse").lazyFormatNullable(personFormat) and
    (__ \ "children").lazyFormatNullable(Reads.seq(personFormat), Writes.seq(personFormat)).inmap({x: Option[Seq[Person]] =>
      x.getOrElse(Seq())
    }, { children: Seq[Person] =>
      if (children.isEmpty) {
        None
      } else {
        Some(children)
      }
    })
    )(Person,unlift(Person.unapply))


  val johnDoeJson = """{
      "name": "John Doe",
      "age": 40,
      "spouse": {
        "name": "Jane Doe",
        "age": 39
      },
      "children": [
        {
          "name": "Bob Doe",
          "age": 14
        }, {
          "name": "Roe Doe",
          "age": 7
        }
      ]
    }"""

  val johnLittle = Person("John Little", 34, Some(Person("Lily Little", 36, None, List())), List(
    Person("Bobby Little", 6, None, List()),
    Person("Sarah Little", 3, None, List())
  ))

 def main(args: Array[String]) {
   val johnDoe = Json.parse(johnDoeJson).as[Person]

   println("John Doe Scala:")
   println(johnDoe)

   val johnLittleJson = Json.toJson(johnLittle)

   println("John Little JSON:")
   println(johnLittleJson)

   println("\nFORMAT\n=====\n")

   println(personFormat.reads(Json.parse(johnDoeJson)))
   println(personFormat.writes(johnLittle))

 }

}
