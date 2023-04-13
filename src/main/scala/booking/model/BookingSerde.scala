package booking.model

import zio.ZIO
import zio.json._
import zio.kafka.serde.Serde

object BookingMessageSerde {
  val key: Serde[Any, Int] = Serde.int

  val value: Serde[Any, BookingMessage] = Serde.string.inmapM[Any, BookingMessage](str =>
    ZIO.fromEither(str.fromJson[BookingMessage])
      .mapError(new RuntimeException(_))
  )(b => ZIO.succeed(b.toJson))
}
