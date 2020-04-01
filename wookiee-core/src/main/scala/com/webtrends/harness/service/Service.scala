/*
 * Copyright 2015 Webtrends (http://www.webtrends.com)
 *
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webtrends.harness.service

import akka.actor.ActorContext
import com.webtrends.harness.app.HActor
import com.webtrends.harness.app.HarnessActor.ConfigChange
import com.webtrends.harness.command._
import com.webtrends.harness.command.v2.CommandHelperV2
import com.webtrends.harness.component.ComponentHelper
import com.webtrends.harness.service.messages._
import com.webtrends.harness.service.meta.{ServiceMetaData, ServiceMetaDetails}

import scala.concurrent.ExecutionContextExecutor

trait Service extends HActor
    with CommandHelper
    with ComponentHelper
    with CommandHelperV2 {

  implicit val executor: ExecutionContextExecutor = context.dispatcher

  def actorRefFactory: ActorContext = context

  // To be defined in service actor, be sure to route through super.serviceRecieve like so:
  //
  def serviceReceive: Receive = {
    case GetMetaDetails => sender ! getMetaDetails()
    case Ready(meta) => ready(meta) // Meta info received
  }: Receive

  override def preStart() {
    initCommandHelper()
    initComponentHelper
    log.info("The service {} started", serviceName)
  }

  // Override to act after Service and Component actors are started
  def ready(meta: ServiceMetaData): Unit = {}

  // Override to act right after Service has been started (after preStart)
  // Return whether or not this Service supports HTTP requests in any form (for the /services endpoint)
  protected def getMetaDetails(): ServiceMetaDetails = {
    ServiceMetaDetails(false)
  }

  def serviceName : String = this.getClass.getSimpleName

  // Combine the services receive along with any optional routes
  override def receive: Receive = super.receive orElse ({
    case Ping =>
      sender() ! Pong

    case ConfigChange() =>
      // User can receive this themselves to renew their config

    case GetMetaData => (context.parent ! GetMetaData(Some(self.path)))(sender())
  }: Receive) orElse serviceReceive
}