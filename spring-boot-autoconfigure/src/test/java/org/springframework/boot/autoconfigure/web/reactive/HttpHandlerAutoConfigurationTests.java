/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.web.reactive;

import org.junit.Test;

import org.springframework.boot.test.context.ContextLoader;
import org.springframework.boot.test.context.ReactiveWebContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HttpHandlerAutoConfiguration}.
 *
 * @author Brian Clozel
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 */
public class HttpHandlerAutoConfigurationTests {

	private final ReactiveWebContextLoader contextLoader = ContextLoader.reactiveWeb()
			.autoConfig(HttpHandlerAutoConfiguration.class);

	@Test
	public void shouldNotProcessIfExistingHttpHandler() {
		this.contextLoader.config(CustomHttpHandler.class).load(context -> {
			assertThat(context.getBeansOfType(HttpHandler.class)).hasSize(1);
			assertThat(context.getBean(HttpHandler.class))
					.isSameAs(context.getBean("customHttpHandler"));
		});
	}

	@Test
	public void shouldConfigureHttpHandlerAnnotation() {
		this.contextLoader.autoConfig(WebFluxAutoConfiguration.class).load(context -> {
			assertThat(context.getBeansOfType(HttpHandler.class).size()).isEqualTo(1);
		});
	}

	@Configuration
	protected static class CustomHttpHandler {

		@Bean
		public HttpHandler customHttpHandler() {
			return (serverHttpRequest, serverHttpResponse) -> null;
		}

		@Bean
		public RouterFunction<ServerResponse> routerFunction() {
			return RouterFunctions.route(RequestPredicates.GET("/test"),
					(serverRequest) -> null);
		}

	}

}
