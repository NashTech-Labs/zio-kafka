package booking.handler.actor

import booking.model.{Booking, BookingMessage, ZioMessage}
import zio._
import zio.actors.Actor.Stateful
import zio.actors._
import zio.dynamodb.DynamoDBQuery.{get, put}
import zio.aws.core.config
import zio.aws.{dynamodb, netty}
import zio.dynamodb.{DynamoDBExecutor, PrimaryKey}
import booking.handler.KafkaProducer

object BookingSyncActor {

  def InsertDetails(bookingValues: Booking): ZIO[DynamoDBExecutor, Throwable, Unit] = {
    for {
      _ <- ZIO.logInfo("Inputting Booking Details")
      _ <- put("BookingDetails", bookingValues).execute
      bookingDetailsSuccess <- get[Booking]("BookingDetails", PrimaryKey("uuid" -> bookingValues.uuid)).execute
      _ <- ZIO.logInfo(s"Booking Details Captured $bookingDetailsSuccess")
    } yield ()
  }

  val bookingSyncActor: Stateful[Any, Unit, ZioMessage] = new Stateful[Any, Unit, ZioMessage] {
    override def receive[A](state: Unit, msg: ZioMessage[A], context: Context): Task[(Unit, A)] =
      msg match {
        case BookingMessage(value) =>
          for {
            _ <- ZIO.logInfo("Booking Sync Actor ................" + value)
            _ <- InsertDetails(value).provide(
              netty.NettyHttpClient.default,
              config.AwsConfig.default, // uses real AWS dynamodb
              dynamodb.DynamoDb.live,
              DynamoDBExecutor.live
            )
            _ <- KafkaProducer.producerRun(value)
          }yield((),value)
      }
  }
}
