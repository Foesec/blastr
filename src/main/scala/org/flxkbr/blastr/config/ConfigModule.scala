package org.flxkbr.blastr.config

import com.typesafe.config.{Config => TsConfig}
import com.typesafe.config.ConfigFactory

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}

class ConfigModule {

  private val localConfig  = ConfigFactory.load()
  private val blastrConfig = localConfig.getConfig("blastr")

  lazy val config: Config = new Config {
    override val DefaultTopic: String = blastrConfig.getString("default-topic")
    override val DefaultTickTime: FiniteDuration =
      FiniteDuration(
        blastrConfig.getDuration("default-tick-time").toMillis,
        TimeUnit.MILLISECONDS
      )
    override val DefaultBootstrapServer: String =
      localConfig.getString("akka.default-bootstrap-server")
    override val Hostname: String            = blastrConfig.getString("hostname")
    override val Port: Int                   = blastrConfig.getInt("port")
    override val ApplicationConfig: TsConfig = localConfig
  }
}
