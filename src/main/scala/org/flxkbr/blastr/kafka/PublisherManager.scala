package org.flxkbr.blastr.kafka

import akka.NotUsed
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Keep, Source}
import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.VoidSerializer
import org.flxkbr.blastr.config.Config
import org.flxkbr.blastr.kafka.model.PublisherId
import org.flxkbr.blastr.kafka.util.MessageSerializer
import org.flxkbr.blastr.proto.BasicMessage

import scala.collection.concurrent
import scala.concurrent.duration.DurationInt

class PublisherManager(config: Config)(implicit mat: Materializer) {

  private val producersStore =
    concurrent.TrieMap.empty[PublisherId, UniqueKillSwitch]

  def startBasicInfiniteProduce(
      customConfig: Option[Config] = None
  ): UniqueKillSwitch = {
    val actualConfig = customConfig.getOrElse(config)
    var i            = -1
    Source
      .tick(0.seconds, actualConfig.DefaultTickTime, NotUsed)
      .map { _ =>
        i = i + 1
        new ProducerRecord(
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
            new VoidSerializer,
            MessageSerializer
          )
        )
      )(Keep.left)
      .run()
  }

  def startStandardInfiniteProducer(): UniqueKillSwitch = {
    ???
  }

}
