package booking.model

import zio.json._
import zio.schema.{DeriveSchema, Schema}

case class FinalInfo(booking: Booking, status: String)

object FinalInfo {
  implicit val encoder: JsonEncoder[FinalInfo] =
    DeriveJsonEncoder.gen[FinalInfo]

  implicit val decoder: JsonDecoder[FinalInfo] =
    DeriveJsonDecoder.gen[FinalInfo]

  implicit lazy val schema: Schema[FinalInfo] = DeriveSchema.gen[FinalInfo]
}
