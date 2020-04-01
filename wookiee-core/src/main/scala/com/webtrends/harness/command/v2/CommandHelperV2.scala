package com.webtrends.harness.command.v2

import akka.actor.{Actor, ActorSystem}
import com.webtrends.harness.command.{AddCommandV2, CommandHelper}

import scala.reflect.ClassTag

object CommandHelperV2 {
  /**
    * Main method for adding a new V2 Command to Wookiee, will automatically register the Command on instantiation
    * @param helper Component-specific assistant that knows how to get Input/Output into the right shape, and where/how
    *               to register this new Command (e.g. making it into an HTTP endpoint)
    * @param id Unique and arbitrary ID to be used for calling this Command anywhere else in a Wookiee Service
    * @param command Function or string of functions that take Input and provide Output
    * @param unmarshal Function meant to take any input (e.g. JSON from a HTTP payload) and convert it to Input
    * @param system Current actor system, necessary to register new Command in Wookiee
    * @tparam Input Input of command function, assumed that the passed DispatcherConfig object knows how to marshal to it
    * @tparam Output Output of command function, assumed that the passed DispatcherConfig object knows how to unmarshal to it
    */
  def addCommand[Input <: Any : ClassTag, Output <: Any : ClassTag]
  (helper: CommandRegister[Input, Output], id: String, unmarshal: Any => Input, command: Input => Output)(implicit system: ActorSystem): Unit = {
    import system.dispatcher

    CommandHelper.getCommandManager(system) map { manager =>
      manager ! AddCommandV2(helper, id, unmarshal, command)
    }
  }
}

trait CommandHelperV2 { this: Actor =>
  def addCommandV2[Input <: Any : ClassTag, Output <: Any : ClassTag]
  (config: CommandRegister[Input, Output], id: String, unmarshal: Any => Input, command: Input => Output): Unit = {
    implicit val system: ActorSystem = context.system
    CommandHelperV2.addCommand[Input, Output](config, id, unmarshal, command)
  }
}