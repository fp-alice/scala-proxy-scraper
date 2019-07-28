package wtf.shekels.alice.proxyscraper

import cats.effect._
import cats.syntax.all._
import org.apache.log4j.BasicConfigurator
import wtf.shekels.alice.proxyscraper.impl.{RequestState, RunescapeScraper}
import wtf.shekels.alice.proxyscraper.requests.ProxyScraper

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    BasicConfigurator.configure()

    ProxyScraper.getProxies.map {
      case Some(proxies) =>
        println(proxies.size)
        scrape(RequestState(proxies, 2))
      case None =>
    }.as(ExitCode.Success)
  }

  def scrape(rs: RequestState): Unit = {
    val scraper = new RunescapeScraper
    scrape(scraper.scrape.run(rs).unsafeRunSync()._1)
  }
}
