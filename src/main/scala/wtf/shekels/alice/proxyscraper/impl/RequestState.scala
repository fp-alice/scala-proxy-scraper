package wtf.shekels.alice.proxyscraper.impl

import cats.effect.IO
import org.asynchttpclient.proxy.ProxyServer
import org.http4s.client.Client

case class RequestState(proxiedClients: List[Client[IO]], index: Int) {
}
