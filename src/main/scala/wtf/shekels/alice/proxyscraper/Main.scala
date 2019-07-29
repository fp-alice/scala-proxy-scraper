package wtf.shekels.alice.proxyscraper

import cats.effect._
import org.apache.log4j.BasicConfigurator
import wtf.shekels.alice.proxyscraper.impl.{RSRequestState, RealScraper}
import wtf.shekels.alice.proxyscraper.requests.ProxyScraper

object Main extends IOApp {

  val scraper = new RealScraper

  def run(args: List[String]): IO[ExitCode] = {
    BasicConfigurator.configure()

    ProxyScraper.getProxiedClients.flatMap { clients =>
      clients.use { httpClients =>
        scraper.run(RSRequestState(httpClients, 1))
      }
    }
  }
}
