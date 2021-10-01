package org.flxkbr.blastr.kafka.model

import java.util.UUID

case class PublisherId(value: String)

object PublisherId {
  def generate(): PublisherId = PublisherId(UUID.randomUUID().toString)
}
