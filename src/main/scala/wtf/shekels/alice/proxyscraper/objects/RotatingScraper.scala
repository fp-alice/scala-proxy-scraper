package wtf.shekels.alice.proxyscraper.objects

import cats.data.StateT
import cats.effect.IO
import cats.implicits._
import org.http4s.{EntityDecoder, Uri}
import wtf.shekels.alice.proxyscraper.requests.ProxyScraper

trait RotatingScraper[S <: RequestState, R] {

  implicit val decoder: EntityDecoder[IO, R]

  val scrape: StateT[IO, S, Option[R]] = StateT { s =>
    ProxyScraper.request[R](makeUri(s), s.proxiedClients.head)
      .attemptT
      .semiflatMap(r => success(s)(r))
      .leftSemiflatMap(e => failure(s)(e))
      .merge
  }

  def makeUri(s: S): Uri

  def success(s: S)(r: Option[R]): IO[(S, Option[R])]

  def failure(s: S)(e: Throwable): IO[(S, Option[R])]

  def run(start: S): IO[Nothing] = scrape.runS(start).flatMap(run)

}
