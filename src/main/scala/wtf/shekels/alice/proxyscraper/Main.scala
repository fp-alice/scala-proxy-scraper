package wtf.shekels.alice.proxyscraper

import cats.effect._
import org.apache.log4j.BasicConfigurator
import wtf.shekels.alice.proxyscraper.impl.{RequestState, RunescapeScraper}
import wtf.shekels.alice.proxyscraper.requests.ProxyScraper

object Main extends IOApp {

  val scraper = new RunescapeScraper

  def run(args: List[String]): IO[ExitCode] = {
    BasicConfigurator.configure()

//    ProxyScraper.getProxies.flatMap {
//      case Some(proxies) =>
//        println(proxies.size)
//        doItForever(RequestState(proxies, 2))(scraper.scrape.runS)
//    }

    ProxyScraper.getProxiedClients.flatMap { clients =>
      clients.use { proxiedClients =>
        doItForever(RequestState(proxiedClients, 2))(scraper.scrape.runS)
      }
    }
  }

  def doItForever(start: RequestState)(loop: RequestState => IO[RequestState]): IO[Nothing] = {
    loop(start).flatMap(doItForever(_)(loop))
  }
}
