package booking.handler.actor

import booking.handler.actor.ThreatreActor.theatreActor
import booking.model.{Booking, BookingMessage}
import zio.actors.{ActorSystem, Supervisor}
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object ThreatreActorSpec extends ZIOAppDefault{

  val data: Booking = booking.handler.actor.JsonSampleData.booking

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for {
      system <- ActorSystem("ticketBookingSystem")
      actor <- system.make("ticketBookingflowActor", Supervisor.none, (), theatreActor)
      _ <- actor !  BookingMessage(data)
    }
    yield {
      Thread.sleep(8000)
    }
}
