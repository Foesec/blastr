package org.flxkbr.blastr.config

import com.typesafe.config.{Config => TypesafeConfig}

import scala.concurrent.duration.FiniteDuration

abstract class Config {
  val ApplicationConfig: TypesafeConfig
  val Hostname: String
  val Port: Int
  val DefaultTopic: String
  val DefaultTickTime: FiniteDuration
}
