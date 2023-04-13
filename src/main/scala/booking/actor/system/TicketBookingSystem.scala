package booking.actor.system

import booking.handler.KafkaConsumer
import zio.actors.ActorSystem
import zio.{ZIO, ZIOAppDefault}

object TicketBookingSystem extends ZIOAppDefault {

  val actorSystem = ActorSystem("ticketBookingSystem")

  def run = {

    for {
      _ <- ZIO.logInfo("Starting Actor System " )
      ticketInfoConsumerProducer <- KafkaConsumer.consumerRun.fork
      _ <- ticketInfoConsumerProducer.join
    } yield ()
  }
}
