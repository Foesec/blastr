package org.flxkbr.blastr.kafka

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.stream.Materializer
import org.flxkbr.blastr.config.ConfigModule

import scala.concurrent.Future

class KafkaModule(configModule: ConfigModule)(implicit
    mat: Materializer
) {

  private val config   = configModule.config
  val publisherManager = new PublisherManager(config)

  CoordinatedShutdown(mat.system)
    .addTask(
      CoordinatedShutdown.PhaseBeforeClusterShutdown,
      "blastr::shutdown_remaining_publishers"
    ) { () =>
      publisherManager.shutdownAll()
    }
}
