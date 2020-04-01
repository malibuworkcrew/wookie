package com.oracle.infy.qa

import com.webtrends.harness.service.Service

class CommandV2Service extends Service {
  /**
    * This function should be implemented by any service that wants to add
    * any commands to make available for use
    */
  override def addCommands(): Unit = {
    addCommandV2(new CommandV2Assistant[V2Input, V2Output](), "CountAs", marshal, countAs)
    addCommandV2(new CommandV2Assistant[V2Input, V2Output](), "CountAsPlus", marshalPlusA, countAs)
    addCommandV2(new CommandV2Assistant[V2Input, V2Output](), "CountBs", marshal, countBs)
  }

  def marshal(in: Any): V2Input = in match {
    case str: String => V2Input(str)
    case other => V2Input(other.toString)
  }

  def marshalPlusA(in: Any): V2Input = in match {
    case str: String => V2Input(str + "a")
    case other => V2Input(other.toString + "a")
  }

  def countAs(in: V2Input): V2Output = {
    val numOfAs = in.value.length - in.value.toLowerCase
      .replaceAll("a", "").length
    V2Output(numOfAs)
  }

  def countBs(in: V2Input): V2Output = {
    val numOfBs = in.value.length - in.value.toLowerCase
      .replaceAll("b", "").length
    V2Output(numOfBs)
  }
}
