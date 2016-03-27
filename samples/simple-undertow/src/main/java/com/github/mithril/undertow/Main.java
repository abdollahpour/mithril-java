package com.github.mithril.undertow;

import com.wpic.mithril.Mithril;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import static com.wpic.mithril.Mithril.m;

public class Main {

    public static void main(final String[] args) {
        final Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        final Mithril mithril = m("html",
                            m("body",
                                m("h1", "Request URI:"),
                                m("p", exchange.getRequestPath())
                            )
                        );

                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                        exchange.getResponseSender().send(mithril.toHtml());
                    }
                }).build();
        server.start();
    }

}