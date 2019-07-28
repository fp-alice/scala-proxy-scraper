package wtf.shekels.alice.proxyscraper.impl

import org.asynchttpclient.proxy.ProxyServer

case class RequestState(proxies: List[ProxyServer], index: Int) {
}
