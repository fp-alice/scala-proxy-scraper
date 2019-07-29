package wtf.shekels.alice.proxyscraper.impl

import cats.effect.IO
import cats.implicits._
import io.circe.Json
import org.http4s.{EntityDecoder, Uri}
import wtf.shekels.alice.proxyscraper.objects.RotatingScraper

class RealScraper extends RotatingScraper[RSRequestState, Json] {

  override def makeUri(s: RSRequestState): Uri = {
    Uri.unsafeFromString(s"http://services.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item=${s.index}")
  }

  override implicit val decoder: EntityDecoder[IO, Json] = org.http4s.circe.jsonDecoder[IO]

  override def success(s: RSRequestState)(r: Option[Json]): IO[(RSRequestState, Option[Json])] = IO {
    r match {
      case Some(x) => println(x)
      case None =>
    }
    r
  }.tupleLeft(RSRequestState(s.proxiedClients.tail :+ s.proxiedClients.head, s.index + 1))

  override def failure(s: RSRequestState)(e: Throwable): IO[(RSRequestState, Option[Json])] = IO {
    None
  }.tupleLeft(RSRequestState(s.proxiedClients.tail, s.index))
}
