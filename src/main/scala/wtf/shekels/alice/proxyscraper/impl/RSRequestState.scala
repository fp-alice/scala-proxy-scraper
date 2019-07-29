package wtf.shekels.alice.proxyscraper.impl

import cats.effect.IO
import org.http4s.client.Client
import wtf.shekels.alice.proxyscraper.objects.RequestState

case class RSRequestState(override val proxiedClients: List[Client[IO]], index: Int) extends RequestState(proxiedClients)
