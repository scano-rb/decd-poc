package globals

import com.google.inject.{AbstractModule, Provides}
import configurations.{CREDIT_CARD_API_URL, CreditCardApiConfiguration}
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  val _ = environment // Just to not break at compile time for not using the environment field.

  override def configure(): Unit = bind(classOf[OnStopHook]).asEagerSingleton()

  @Provides
  def creditCardApiConfiguration: CreditCardApiConfiguration =
    CreditCardApiConfiguration(
      url = configuration.get(CREDIT_CARD_API_URL)
    )
}
