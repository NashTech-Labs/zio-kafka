import booking.handler.actor.PaymentGatewayActor.paymentGatewayflowActor
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}
import zio.actors.{ActorSystem, Supervisor}

object PaymentGatewayActorSpec extends ZIOAppDefault{

  val data = booking.handler.actor.JsonSampleData.booking

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for {
      system <- ActorSystem("ticketBookingSystem")
      actor <- system.make("paymentGatewayflowActor", Supervisor.none, (), paymentGatewayflowActor)
      result <- actor ! BookingMessage(data)
    }
    yield result
}
