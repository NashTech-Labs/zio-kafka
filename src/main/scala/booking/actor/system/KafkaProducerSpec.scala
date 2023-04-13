package booking.actor.system

import booking.model.{Booking, BookingMessage, BookingMessageSerde}
import kafka.zio.KafkaProdConsLayer.producer
import kafka.zio.KafkaTopics
import org.apache.kafka.clients.producer.ProducerRecord
import zio.kafka.producer.Producer
import zio.stream.ZStream
import zio.{Clock, Random, Schedule, ZIOAppDefault, durationInt}

object KafkaProducerSpec extends ZIOAppDefault {
  def run = {
    val message = new ProducerRecord(
      KafkaTopics.KAFKA_TOPIC_BOOKING_REQUEST,
      0,
      BookingMessage(Booking("1", "april 11, 2023", "PVR", "NewDelhi", "B4 B5", 1, 2, 3, 4, Some(""), Some("")))
    )

    ZStream
      .fromIterable(List.fill(5)(message))
      .via(Producer.produceAll(BookingMessageSerde.key, BookingMessageSerde.value))
      .drain
      .runDrain.provide(producer)
  }
}
