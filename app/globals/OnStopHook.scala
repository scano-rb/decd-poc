package globals

import akka.actor.ActorSystem
import play.api.inject.ApplicationLifecycle

import javax.inject.{Inject, Singleton}

@Singleton
class OnStopHook @Inject()(lifecycle: ApplicationLifecycle, system: ActorSystem) {

  lifecycle.addStopHook { () =>
    system.terminate()
  }
}
