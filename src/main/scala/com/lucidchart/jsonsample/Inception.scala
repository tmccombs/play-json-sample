package com.lucidchart.jsonsample

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by thayne on 1/3/15.
 */
object Inception {
  case class ImageData(name: String, mimeType:String, width: Int, height: Int, metadata: Map[String, String])

  /**
   * Play JSON library uses Scala macros to automatically create a Format (or Reads or Writes)
   * at compile time for simple case classes.
   */
  implicit val imageDataFormat = Json.format[ImageData]

  val imageDataJson =
    """{
      "name": "picture.png",
      "mimeType": "image/png",
      "width": 100,
      "height": 100,
      "metadata": {
        "creator": "Photoshop",
        "date": "2014-12-30"
      }
    }"""

  val hubbleImage = ImageData("hubble-ultra-deep-field.jpg", "image/jpeg", 6200, 6200, Map(
    "telescope" -> "HST",
    "date" -> "2004-01-16"
  ))

  def main(args: Array[String]) {

    val imageData = Json.parse(imageDataJson).as[ImageData]

    println(imageData)

    println(Json.toJson(hubbleImage))

  }

}
