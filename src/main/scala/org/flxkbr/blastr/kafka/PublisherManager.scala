package org.flxkbr.blastr.kafka

import akka.{Done, NotUsed}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Keep, Source}
import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringSerializer, VoidSerializer}
import org.flxkbr.blastr.config.Config
import org.flxkbr.blastr.kafka.model.PublisherId
import org.flxkbr.blastr.kafka.util.MessageSerializer
import org.flxkbr.blastr.proto.BasicMessage

import scala.collection.concurrent
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class PublisherManager(config: Config)(implicit mat: Materializer)
    extends LazyLogging {

  private val producersStore =
    concurrent.TrieMap.empty[PublisherId, UniqueKillSwitch]

  def startBasicInfiniteProducer(
      customConfig: Option[Config] = None
  ): PublisherId = {
    val actualConfig = customConfig.getOrElse(config)
    var i            = -1
    val killSwitch = Source
      .tick(0.seconds, actualConfig.DefaultTickTime, NotUsed)
      .map { _ =>
        i = i + 1
        new ProducerRecord[String, scalapb.GeneratedMessage](
          actualConfig.DefaultTopic,
          BasicMessage(i.toString)
        )
      }
      .viaMat(KillSwitches.single)(Keep.right)
      .toMat(
        Producer.plainSink(
          ProducerSettings(
            actualConfig.ApplicationConfig
              .getConfig("akka.kafka.producer"),
            new StringSerializer,
            MessageSerializer
          ).withBootstrapServers(actualConfig.DefaultBootstrapServer)
        )
      )(Keep.left)
      .run()
    val key = PublisherId.generate()
    producersStore.put(key, killSwitch)
    key
  }

  def shutdownAll(): Future[Done] = {
    logger.info("Shutting down all running producers...")
    producersStore.map { case (pubId, killSwitch) =>
      logger.info(s"Shutting down producer $pubId")
      killSwitch.shutdown()
    }
    Future.successful(Done)
  }

}
