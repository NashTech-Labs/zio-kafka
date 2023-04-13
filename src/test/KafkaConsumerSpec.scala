package kafka.zio

import booking.model.{Booking, BookingSerde}
import kafka.zio.KafkaProdConsLayer.producer
import org.apache.kafka.clients.producer.ProducerRecord
import zio.kafka.producer.Producer
import zio.stream.ZStream
import zio.{Clock, Random, Schedule, ZIOAppDefault, durationInt}

object KafkaConsumerSpec extends ZIOAppDefault {

  def run = {
    val finalInfoStream =
      Consumer
        .subscribeAnd(Subscription.topics(KafkaTopics.KAFKA_TOPIC_BOOKING_RESPONSE))
        .plainStream(BookingSerde.key, BookingSerde.value)
        .tap { comRec =>
          val ticketBooking = comRec.value
          zio.Console.printLine("Ticket booking response "+ticketBooking)

        }
        .map(_.offset)
        .aggregateAsync(Consumer.offsetBatches)
        .mapZIO(_.commit)
        .drain

    finalInfoStream.runDrain.provide(KafkaProdConsLayer.consumerLayer ++ KafkaProdConsLayer.producer)
  }
}
