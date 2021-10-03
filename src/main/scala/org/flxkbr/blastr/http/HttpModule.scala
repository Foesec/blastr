package org.flxkbr.blastr.http

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{
  ContentTypes,
  HttpEntity,
  HttpResponse,
  StatusCodes
}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging
import org.flxkbr.blastr.config.ConfigModule
import org.flxkbr.blastr.kafka.KafkaModule

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class HttpModule(configModule: ConfigModule, kafkaModule: KafkaModule)(implicit
    system: ActorSystem[_]
) extends LazyLogging {

  implicit val ec: ExecutionContext = system.executionContext

  private val kafkaRoutes: Route = pathPrefix("api" / "kafka") {
    concat(
      pathPrefix("producers") {
        concat(
          pathPrefix("basic") {
            post {
              val pubId =
                kafkaModule.publisherManager.startBasicInfiniteProducer()
              complete(
                HttpEntity(
                  ContentTypes.`text/plain(UTF-8)`,
                  s"Successfully started BASIC PUBLISHER ${pubId.value}"
                )
              )
            }
          }
        )
      },
      pathPrefix("consumers") {
        complete(StatusCodes.NotImplemented)
      }
    )

  }

  private val healthRoutes: Route = pathPrefix("health") {
    get {
      complete(StatusCodes.OK)
    }
  }

  private val topLevelRoutes = healthRoutes ~ kafkaRoutes

  Http()
    .newServerAt(configModule.config.Hostname, configModule.config.Port)
    .bind(topLevelRoutes)
    .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

  logger.info(
    s"Server listening on ${configModule.config.Hostname}:${configModule.config.Port}"
  )
}
