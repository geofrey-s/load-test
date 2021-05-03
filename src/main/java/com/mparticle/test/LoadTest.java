package com.mparticle.test;

import com.mparticle.test.Errors.HttpMethodNotSupportedException;
import kong.unirest.*;

import java.util.concurrent.CompletableFuture;

import static com.mparticle.test.CliArgumentsParser.CliOption.*;
import static com.mparticle.test.Constants.*;

public class LoadTest implements TestRunner {
    private String path;
    private HttpMethod httpMethod;
    private String jsonPayload;

    @Override
    public TestRunner setup(CliArgumentsParser cliOptions) {
        Unirest.config()
                .defaultBaseUrl(cliOptions.getString(HOST))
                .addDefaultHeader(AUTH_KEY_HEADER, cliOptions.getString(AUTH_KEY))
                .automaticRetries(false)
                .cacheResponses(false)
                .socketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                .instrumentWith(MetricsCollector::profiler);

        path = cliOptions.getString(PATH);
        httpMethod = HttpMethod.from(cliOptions.getString(METHOD));
        jsonPayload = cliOptions.getString(PAYLOAD);

        MetricsCollector.cliOptions = cliOptions;
        return this;
    }

    @Override
    public void execute() {
        CompletableFuture.runAsync(() -> {
            while (true) {
                switch (httpMethod) {
                    case GET:
                        Unirest.get(path).asStringAsync();
                        break;
                    case POST:
                        Unirest.post(path)
                                .body(jsonPayload)
                                .asStringAsync();
                        break;
                    case PUT:
                    case PATCH:
                    case DELETE:
                    default:
                        throw new HttpMethodNotSupportedException(httpMethod.toString());
                }

            }
        });

        this.running();
    }

    @Override
    public void stop() {
        System.out.println("Stopped");
        Unirest.shutDown();
    }
}
