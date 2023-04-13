package booking.handler.actor

import booking.actor.system.TicketBookingSystem.actorSystem
import booking.handler.actor.BookingSyncActor.bookingSyncActor
import booking.model.{Booking, BookingMessage, ZioMessage}
import zio.Task
import zio.actors.Actor.Stateful
import zio.actors._

object PaymentGatewayActor {
   val paymentGatewayflowActor: Stateful[Any, Unit, ZioMessage] = new Stateful[Any, Unit, ZioMessage] {
    override def receive[A](state: Unit, msg: ZioMessage[A], context: Context): Task[(Unit, A)] =
      msg match {
        case BookingMessage(value) =>
          println("paymentInfo ................" + value)
          val booking = Booking(value.uuid, value.bookingDate, value.theatreName, value.theatreLocation, value.seatNumbers, value.cardNumber, value.pin,
            value.cvv, value.otp, Some("Success"), Some(""))
          for {
            bookingSyncActor <- actorSystem.flatMap(x => x.make("bookingSyncActor", zio.actors.Supervisor.none, (), bookingSyncActor))
            //ZIO.succeed(booking)
          }yield{
            println("paymentInfo return................" + booking)
            ( BookingMessage(booking), ())
          }

        case _ => throw new Exception("Wrong value Input")
      }
  }
}
