package vn.sapo.jeager.config;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class JaegerConfiguration {

    @Value("${spring.application.name}")
    private String SERVICE_NAME;

    @Value("${jaeger.host}")
    private String JAEGER_HOST;

    @Value("${jaeger.port}")
    private int JAEGER_PORT;

    @Bean
    public Tracer tracer() {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration
            .fromEnv()
            .withType(ConstSampler.TYPE)
            .withParam(1);
        Configuration.SenderConfiguration senderConfig = Configuration.SenderConfiguration.fromEnv()
            .withAgentHost(JAEGER_HOST)
            .withAgentPort(JAEGER_PORT);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration
            .fromEnv()
            .withSender(senderConfig);
        Configuration config = new Configuration(SERVICE_NAME)
            .withSampler(samplerConfig)
            .withReporter(reporterConfig);
        return config.getTracer();
    }
}
