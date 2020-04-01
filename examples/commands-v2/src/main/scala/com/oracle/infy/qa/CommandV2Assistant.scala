package com.oracle.infy.qa

import com.webtrends.harness.command.v2.CommandRegister

import scala.collection.mutable
import scala.reflect.ClassTag

case class V2Input(value: String)
case class V2Output(value: Int)

object CommandV2Registry {
  val commandMap: mutable.Map[String, Product => Any] = mutable.Map[String, Product => Any]()
}

class CommandV2Assistant[Input <: Product : ClassTag, Output <: Any : ClassTag] extends CommandRegister[Input, Output] {
  override def register(id: String, marshal: Any => Input, command: Input => Output): Unit = {
    CommandV2Registry.commandMap.update(id, { in => command(marshal(in)) })
  }
}
