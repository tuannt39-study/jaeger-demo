package vn.sapo.jeager.controller;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HelloController {

    private final RestTemplate restTemplate;
    private final Tracer tracer;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hi")
    public String hi() {
        try {
            Span span = tracer.buildSpan("hihihi").start();

            Span hihi1 = tracer.buildSpan("hihihi1").asChildOf(span).start();
            Thread.sleep(1000);
            ResponseEntity<String> response1 = restTemplate.getForEntity("http://localhost:8088/api/hello", String.class);
            String aaa1 = "hi1" + response1.getBody();
            hihi1.finish();

            Span hihi2 = tracer.buildSpan("hihihi2").asChildOf(span).start();
            Thread.sleep(1000);
            ResponseEntity<String> response2 = restTemplate.getForEntity("http://localhost:8088/api/hello", String.class);
            String aaa2 = "hi2" + response2.getBody();
            hihi2.finish();

            span.finish();
            return aaa1 + aaa2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "hi";
    }
}
