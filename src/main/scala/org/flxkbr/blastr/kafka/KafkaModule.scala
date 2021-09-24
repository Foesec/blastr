package org.flxkbr.blastr.kafka

import akka.stream.Materializer
import org.flxkbr.blastr.config.ConfigModule

class KafkaModule(configModule: ConfigModule)(implicit
    mat: Materializer
) {

  private val config   = configModule.config
  val publisherManager = new PublisherManager(config)
}
