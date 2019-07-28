package wtf.shekels.alice.proxyscraper.impl

import cats.data.StateT
import cats.effect.IO
import cats.implicits._
import io.circe.Json
import org.http4s.Uri
import wtf.shekels.alice.proxyscraper.requests.ProxyScraper

class RunescapeScraper {

  val scrape: StateT[IO, RequestState, Option[Json]] = StateT { s =>
    ProxyScraper.request(makeUri(s), s.proxiedClients.head).attemptT.semiflatMap { r =>
      IO {
        println(r)
        r
      }.tupleLeft(RequestState(s.proxiedClients.tail :+ s.proxiedClients.head, s.index + 1))
    }.leftSemiflatMap { e =>
      IO {
//        e.printStackTrace()
        None
      }.tupleLeft(RequestState(s.proxiedClients.tail, s.index + 1))
    }.merge
//    ProxyScraper.request(makeUri(s), s.proxiedClients.head).map { r =>
//      println(r)
//      r
//    }.tupleLeft(RequestState(s.proxiedClients.tail :+ s.proxiedClients.head, s.index + 1))
  }

  def makeUri(requestState: RequestState): Uri = {
    Uri.fromString(s"http://services.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item=${requestState.index}").getOrElse(null)
  }
}
