package wtf.shekels.alice.proxyscraper.requests



import cats.effect.{ContextShift, IO, Resource, Timer}
import cats.implicits._
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.ssl.{SslContext, SslContextBuilder}
import org.asynchttpclient.Dsl
import org.asynchttpclient.proxy.ProxyServer
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.asynchttpclient.AsyncHttpClient
import org.http4s.client.middleware.Logger
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global

object ProxyScraper {

  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val sslContext: SslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()

  private def getProxies: IO[Option[List[ProxyServer]]] = {
    val config = Dsl.config().setSslContext(sslContext).build()
    val httpClient: Resource[IO, Client[IO]] = AsyncHttpClient.resource(config)

    val acceptHeader = headers.Accept(org.http4s.MediaType.text.plain)

    val request = org.http4s.Request[IO](
      Method.GET,
      uri = uri"https://www.proxy-list.download/api/v1/get?type=http",
      headers = Headers.of(acceptHeader)
    )

    httpClient.use(client => {
      Logger(logBody = true, logHeaders = false)(client)
        .expect[String](request)
        .map {
          case s: String if s.nonEmpty =>
            val proxies = s.lines
              .filter(_.contains(":"))
              .map(_.split(":"))
              .map {
                case Array(ip, port) => new ProxyServer.Builder(ip, port.toInt).build()
              }.toList
            Some(proxies)
          case _ => None
        }
    })
  }

  def getProxiedClients: IO[Resource[IO, List[Client[IO]]]] = {
    getProxies.map(_.toList.flatten.traverse { proxy =>
      AsyncHttpClient.resource(Dsl.config().setSslContext(sslContext).setProxyServer(proxy).setMaxRequestRetry(0).build())
    })
  }

  def request[R](uri: Uri, client: Client[IO])(implicit decoder: EntityDecoder[IO, R]): IO[Option[R]] = {
    val acceptHeader = headers.Accept(org.http4s.MediaType.application.json)

    val request = org.http4s.Request[IO](
      Method.GET,
      uri = uri,
      headers = Headers.of(acceptHeader)
    )

    Logger(logBody = true, logHeaders = true)(client)
      .expectOption[R](request)
  }
}


//  implicit val decodeRunescapeItem: Decoder[RunescapeItem] = (c: HCursor) => {
//    for {
//      item <- c.downField("item").as[String]
//      icon <- c.downField("icon").as[String]
//      icon_large <- c.downField("icon_large").as[String]
//      itemType <- c.downField("type").as[String]
//      typeIcon <- c.downField("typeIcon").as[String]
//      name <- c.downField("name").as[String]
//      members <- c.downField("members").as[Boolean]
//      trend <- c.downField("trend").as[String]
//      price <- c.downField("price").as[String]
//      change <- c.downField("change").as[String]
//      current <- c.downField("current").as[List[String]]
//      today <- c.downField("today").as[List[String]]
//      day30 <- c.downField("day30").as[List[String]]
//      day90 <- c.downField("day90").as[List[String]]
//      day180 <- c.downField("day180").as[List[String]]
//    } yield RunescapeItem(item, icon, icon_large, itemType, typeIcon, name, members, trend, price, change, current, today, day30, day90, day180)
//  }
