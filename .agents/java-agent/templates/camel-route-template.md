```java
package com.swifttech.edx.ipr.eam.middleware.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swifttech.edx.ipr.eam.model.external.V1UserCheckRequest;
import com.swifttech.edx.ipr.ts.exchangerate.middleware.processor.ExternalApiRequestProcessor;
import com.swifttech.edx.ipr.ts.transaction.middleware.authentication.TokenService;
import com.swifttech.edx.ipr.ts.transaction.middleware.route.exception.BaseRouteBuilder;
import com.swifttech.edx.ipr.ts.transaction.util.DataValidationHelper;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Camel route that orchestrates the UpdateCustomerProfile external API call.
 */
@Component
public class GlobalCustomer_CheckIfUserAccountStatusInV1Route extends BaseRouteBuilder {
    public GlobalCustomer_CheckIfUserAccountStatusInV1Route(
            ExternalApiRequestProcessor externalApiRequestProcessor,
            ObjectMapper objectMapper,
            TokenService tokenService,
            DataValidationHelper dataValidationHelper) {

        super(externalApiRequestProcessor, objectMapper, tokenService   , dataValidationHelper);
    }

    @Override
    public void configure()  {
        super.configure();
        from("direct:GlobalCustomer_CheckCustomer")
                .routeId("GlobalCustomer_CheckCustomer")
                .process(exchange -> {
                    V1UserCheckRequest internal = exchange.getIn().getBody(V1UserCheckRequest.class);
                    exchange.setProperty("externalRequest", internal);
                    exchange.getIn().setBody(internal);
                })

                .marshal().json(JsonLibrary.Jackson)

                .setProperty("httpMethod").constant("POST")
                .setProperty("pathTemplate").constant("/api/customer/CheckCustomer")


                .process(txExternalApiRequestProcessor)

                .toD("${exchangeProperty.targetUrl}")

                .unmarshal().json(JsonLibrary.Jackson, Map.class)
                .process(exchange -> {
                    Map<?, ?> ext = exchange.getIn().getBody(Map.class);
                    exchange.getIn().setBody(ext);
                })

                .log(LoggingLevel.INFO, "Completed route GlobalCustomer_CheckCustomer");
    }
}

```