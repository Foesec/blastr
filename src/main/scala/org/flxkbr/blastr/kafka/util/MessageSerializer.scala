package org.flxkbr.blastr.kafka.util

import org.apache.kafka.common.serialization.Serializer
import scalapb.GeneratedMessage

object MessageSerializer extends Serializer[scalapb.GeneratedMessage] {
  override def serialize(topic: String, data: GeneratedMessage): Array[Byte] = {
    data.toByteArray
  }
}
