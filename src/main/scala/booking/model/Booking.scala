package booking.model

import zio.json._
import zio.schema.{DeriveSchema, Schema}

sealed trait ZioMessage[+_]
case class BookingMessage(msg: Booking) extends ZioMessage[Unit]
case class Booking(uuid: String, bookingDate: String, theatreName: String,
                   theatreLocation: String, movieName: String, showTimings: String,  seatNumbers: String, cardNumber: Int, pin: Int, cvv: Int, otp: Int, paymentStatus:Option[String], bookingStatus:Option[String])

object Booking {
  implicit lazy val schema: Schema[Booking] = DeriveSchema.gen[Booking]

  implicit val encoder: JsonEncoder[Booking] =
    DeriveJsonEncoder.gen[Booking]

  implicit val decoder: JsonDecoder[Booking] =
    DeriveJsonDecoder.gen[Booking]
}

object BookingMessage {
  implicit val encoder: JsonEncoder[BookingMessage] =
    DeriveJsonEncoder.gen[BookingMessage]

  implicit val decoder: JsonDecoder[BookingMessage] =
    DeriveJsonDecoder.gen[BookingMessage]
}