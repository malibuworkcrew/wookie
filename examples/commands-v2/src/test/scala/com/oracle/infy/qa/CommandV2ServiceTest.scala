package com.oracle.infy.qa

import akka.testkit.TestProbe
import com.typesafe.config.{Config, ConfigFactory}
import com.webtrends.harness.service.Service
import com.webtrends.harness.service.messages.GetMetaDetails
import com.webtrends.harness.service.meta.ServiceMetaDetails
import com.webtrends.harness.service.test.{BaseWookieeTest, TestHarness}
import org.scalatest.{Matchers, WordSpecLike}

class CommandV2ServiceTest extends BaseWookieeTest with WordSpecLike with Matchers {
  override def config: Config = ConfigFactory.empty()
  override def servicesMap: Option[Map[String, Class[_<:Service]]] = Some(Map("base" -> classOf[CommandV2Service]))

  "CommandV2Service" should {
    "start itself up" in {
      val probe = TestProbe()
      val testService = TestHarness.get(port).get.getService("base")
      assert(testService.isDefined, "Command Service was not registered")

      probe.send(testService.get, GetMetaDetails)
      ServiceMetaDetails(false) shouldBe probe.expectMsg(ServiceMetaDetails(false))
    }
  }
}
