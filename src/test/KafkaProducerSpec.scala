package kafka.zio

import booking.model.{Booking, BookingSerde}
import kafka.zio.KafkaProdConsLayer.producer
import org.apache.kafka.clients.producer.ProducerRecord
import zio.kafka.producer.Producer
import zio.stream.ZStream
import zio.{Clock, Random, Schedule, ZIOAppDefault, durationInt}

object KafkaProducerSpec extends ZIOAppDefault {

  def run = {
    ZStream
      .repeatZIO(Random.nextUUID <*> Clock.currentDateTime)
      .schedule(Schedule.spaced(1.second))
      .map { case (id, time) =>

        new ProducerRecord(
          KafkaTopics.KAFKA_TOPIC_BOOKING_REQUEST,
          time.getMinute,
          BookingMessage(Booking(id.toString, time.toZonedDateTime, "feb 20, 2023", "PVR", "NewDelhi", "B4 B5", 1,2,3,4,Some(""),Some("")))
        )
      }
      .via(Producer.produceAll(BookingSerde.key, BookingSerde.value))
      .drain
      .runDrain.provide(producer)
  }
}
