package com.webtrends.harness.command.v2

import scala.reflect.ClassTag

abstract class CommandRegister[Input <: Any : ClassTag, Output <: Any : ClassTag] {
  def register(id: String, unmarshal: Any => Input, command: Input => Output): Unit
}