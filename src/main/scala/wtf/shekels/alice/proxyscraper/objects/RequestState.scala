package wtf.shekels.alice.proxyscraper.objects

import cats.effect.IO
import org.http4s.client.Client

class RequestState(val proxiedClients: List[Client[IO]] = List.empty)
