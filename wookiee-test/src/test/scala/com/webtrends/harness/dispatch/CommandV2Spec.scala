package com.webtrends.harness.dispatch

import com.webtrends.harness.command.v2.{CommandHelperV2, CommandRegister}
import com.webtrends.harness.service.test.BaseWookieeTest
import org.scalatest.{Matchers, WordSpecLike}

class CommandV2Spec extends BaseWookieeTest with WordSpecLike with Matchers {
  var registeredParam = ""

  case class TestConfig(param: String) extends CommandRegister[String, String] {
    override def register(id: String, unmarshal: Any => String, command: String => String): Unit = {
      registeredParam = command(unmarshal(s"test-$param-$id"))
    }
  }

  "Dispatchers" should {
    "Register endpoints" in {
      CommandHelperV2.addCommand[String, String](TestConfig("regtest"), "id1",
        { in: Any => s"$in-unmarsh"}, { in: String => s"$in-command"})
      Thread.sleep(1)
      registeredParam shouldBe "test-regtest-id1-unmarsh-command"
    }
  }
}
