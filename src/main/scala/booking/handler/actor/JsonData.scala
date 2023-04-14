package booking.handler.actor

import booking.model.Booking
import java.time.ZonedDateTime

object JsonSampleData {

  val booking: Booking = Booking("1234588", ZonedDateTime.now().toString, "PVR", "NewDelhi", "Avatar", "15:00", "A1,A2", 1234544566, 1234, 123, 4567, Some(""), Some(""))

  val json =
    """{
   "uuid" : "abc123",
   "ticketTheatreDetails" : {
                   "theatreName" : "PVR",
                   "theatreLocation" : "NewDelhi",
                   "movieName": "Avatar",
                   "showTimings": "15:00"
                   "seatNumbers" : "B4, B5"
                 },
   "paymentDetails" : {
                   "cardNumber" : 1,
                   "pin" : 2,
                   "cvv" : 3,
                   "otp" : 4
                 }
 }"""
}
