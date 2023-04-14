package booking.actor.system

import booking.model.BookingMessageSerde
import kafka.zio.{KafkaProdConsLayer, KafkaTopics}
import zio.kafka.consumer.{Consumer, Subscription}
import zio.{ZIO, ZIOAppDefault}

object KafkaConsumerSpec extends ZIOAppDefault {

  def run = {
    val finalInfoStream =
      Consumer
        .subscribeAnd(Subscription.topics(KafkaTopics.KAFKA_TOPIC_BOOKING_RESPONSE))
        .plainStream(BookingMessageSerde.key, BookingMessageSerde.value)
        .tap { comRec =>
          val ticketBooking = comRec.value
          ZIO.logInfo("Ticket Booking Response " + ticketBooking)
        }
        .map(_.offset)
        .aggregateAsync(Consumer.offsetBatches)
        .mapZIO(_.commit)
        .drain

    finalInfoStream.runDrain.provide(KafkaProdConsLayer.consumerLayer ++ KafkaProdConsLayer.producer)
  }
}
