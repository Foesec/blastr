package org.flxkbr.blastr

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.flxkbr.blastr.config.ConfigModule
import org.flxkbr.blastr.http.HttpModule
import org.flxkbr.blastr.kafka.KafkaModule

object Blastr {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty[NotUsed], "blastr")

    val configModule = new ConfigModule()
    val kafkaModule  = new KafkaModule(configModule)
    val httpModule   = new HttpModule(configModule, kafkaModule)
  }
}
