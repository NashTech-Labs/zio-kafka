package kafka.zio

import zio.ZLayer
import zio.kafka.consumer._
import zio.kafka.producer._

object KafkaProdConsLayer {
  val consumerLayer: ZLayer[Any, Throwable, Consumer] =
    ZLayer.scoped(
      Consumer.make(
        ConsumerSettings(KafkaMeta.BOOSTRAP_SERVERS)
          .withGroupId(KafkaMeta.CONSUMER_GROUP)
      )
    )

  val producer: ZLayer[Any, Throwable, Producer] =
    ZLayer.scoped(
      Producer.make(
        ProducerSettings(KafkaMeta.BOOSTRAP_SERVERS)
      )
    )
}
