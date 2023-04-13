package booking.model

import zio.ZIO
import zio.json.{DecoderOps, EncoderOps}
import zio.kafka.serde.Serde

object FinalInfoSerde {
  val key: Serde[Any, Int] = Serde.int

  val value: Serde[Any, FinalInfo] = Serde.string.inmapM[Any, FinalInfo](str =>
    ZIO.fromEither(str.fromJson[FinalInfo])
      .mapError(new RuntimeException(_))
  )(b => ZIO.succeed(b.toJson))
}
