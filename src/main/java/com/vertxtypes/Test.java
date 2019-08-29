package com.vertxtypes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class Test extends AbstractVerticle{
	
	
	@Override
	public void start(Future<Void> future) {
		try {
			Router router = Router.router(vertx);
			router.route("/*").handler(BodyHandler.create().setMergeFormAttributes(true));
			vertx.createHttpServer()
				.requestHandler(router::accept).listen(8000);
			router.route().handler(StaticHandler.create());
//			router.route().handler(CookieHandler.create());
			router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
			router.route().handler(CorsHandler.create("*"));
			router.get("/test").handler(req -> {
				System.out.println(req.request().absoluteURI());
				req.response().setStatusCode(200).end("Success!!!!");
			});
			
			router.errorHandler(500, err -> {
				System.out.println(err.failure().getMessage());
				System.out.println(err.request().absoluteURI());
			});
			
			
		}catch (Exception e) {
			future.fail(e);
		}
	}
	
	public static void main(String[] args) {
		AbstractVerticle verticle = new Test();
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(verticle);
	}

}
