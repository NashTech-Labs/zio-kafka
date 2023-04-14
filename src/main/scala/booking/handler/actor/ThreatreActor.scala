package booking.handler.actor

import booking.actor.system.TicketBookingSystem.actorSystem
import zio.actors.Actor.Stateful
import zio.actors._
import zio._
import booking.handler.actor.PaymentGatewayActor.paymentGatewayflowActor
import _root_.booking.handler.actor.BookingSyncActor.bookingSyncActor
import booking.model.{Booking, BookingMessage, ZioMessage}

object ThreatreActor {
  val theatreActor: Stateful[Any, Unit, ZioMessage] = new Stateful[Any, Unit, ZioMessage] {
    override def receive[A](state: Unit, msg: ZioMessage[A], context: Context): Task[(Unit, A)] =
      msg match {
        case BookingMessage(value) => {
          ZIO.logInfo("ThreatreActor ................" + value)
          val ticketConfirm= Booking(value.uuid, value.bookingDate, value.theatreName, value.theatreLocation, value.movieName, value.showTimings, value.seatNumbers, value.cardNumber, value.pin,
            value.cvv, value.otp, Some("Success"), Some("Confirmed"))
          for{
            paymentActor <- actorSystem.flatMap(x => x.make("paymentGatewayflowActor", zio.actors.Supervisor.none, (), paymentGatewayflowActor))
            paymentDetails <- paymentActor ? BookingMessage(value)
            bookingSyncActor <- actorSystem.flatMap(x => x.make("bookingSyncActor", zio.actors.Supervisor.none, (), bookingSyncActor))
            _ <- bookingSyncActor ! BookingMessage(ticketConfirm)
          }yield {
            ZIO.logInfo("Completed Theatre Actor")
            ((),())}

        }

        case _ => throw new Exception("Wrong value Input")
      }
  }
}
