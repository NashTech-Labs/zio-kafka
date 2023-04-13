package booking.handler

import _root_.kafka.zio.{KafkaProdConsLayer, KafkaTopics}
import booking.actor.system.TicketBookingSystem.actorSystem
import booking.handler.actor.ThreatreActor.theatreActor
import booking.model.BookingMessageSerde
import zio._
import zio.kafka.consumer.{Consumer, Subscription}

object KafkaConsumer {
  def consumerRun: ZIO[Any, Throwable, Unit] = {
    ZIO.logInfo("Starting KafkaConsumer ")
    val finalInfoStream =
      Consumer
        .subscribeAnd(Subscription.topics(KafkaTopics.KAFKA_TOPIC_BOOKING_REQUEST))
        .plainStream(BookingMessageSerde.key, BookingMessageSerde.value)
        .tap{comRec =>
          val ticketBooking = comRec.value
          for {
            _ <- ZIO.logInfo("KafkaConsumer"+ticketBooking)
            theatreActor <- actorSystem.flatMap(x => x.make("ticketBookingflowActor", zio.actors.Supervisor.none, (), theatreActor))
            theatreActorData <- theatreActor ! ticketBooking
          } yield theatreActorData
        }
        .map(_.offset)
        .aggregateAsync(Consumer.offsetBatches)
        .mapZIO(_.commit)
        .drain

      finalInfoStream.runDrain.provide(KafkaProdConsLayer.consumerLayer ++ KafkaProdConsLayer.producer)
  }
}
