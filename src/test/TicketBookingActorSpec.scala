import booking.handler.actor.TicketBookingActor.ticketBookingflowActor
import booking.model.Booking
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.actors.{ActorSystem, Supervisor}

object TicketBookingActorSpec extends ZIOAppDefault {
  val data: Booking = booking.handler.actor.JsonSampleData.booking

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for {
      system <- ActorSystem("ticketBookingSystem")
      actor <- system.make("ticketBookingflowActor", Supervisor.none, (), ticketBookingflowActor)
      result <- actor ! BookingMessage(data)
    }
    yield result
}
