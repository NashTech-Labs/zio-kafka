package booking.handler

import booking.model.{Booking, BookingMessage, BookingMessageSerde}
import kafka.zio.KafkaProdConsLayer.producer
import kafka.zio.KafkaTopics
import org.apache.kafka.clients.producer.ProducerRecord
import zio.kafka.producer.Producer
import zio.stream.ZStream
import zio.{Clock, Random, ZIO}

object KafkaProducer {

  def producerRun(booking: Booking) = {
    ZIO.logInfo("data ...." + booking)
    ZStream
      .repeatZIO(Random.nextUUID <*> Clock.currentDateTime)
      .map { case (_, time) =>
        new ProducerRecord(
          KafkaTopics.KAFKA_TOPIC_BOOKING_RESPONSE,
          time.getMinute,
          BookingMessage(booking)
        )
      }
      .via(Producer.produceAll(BookingMessageSerde.key, BookingMessageSerde.value))
      .drain
      .runDrain.provide(producer)
  }
}
