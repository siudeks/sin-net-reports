package sinnet;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import sinnet.bus.JsonMessage;

public abstract class AskTemplate<ASK extends JsonMessage, REPLY extends JsonMessage> {

    @Autowired
    private EventBus eventBus;

    private final String address;
    private final Class<REPLY> replyClass;

    protected AskTemplate(String address, Class<REPLY> replyClass) {
        this.address = address;
        this.replyClass = replyClass;
    }

    protected final CompletableFuture<REPLY> ask(ASK ask) {
        var result = new CompletableFuture<REPLY>();
        var query = ask.json();
        eventBus
            .request(address, query)
            .onComplete(it -> {
                if (it.succeeded()) {
                    var body = it.result().body();
                    var reply = JsonObject.mapFrom(body).mapTo(replyClass);
                    result.completeAsync(() -> reply);
                } else {
                    result.completeExceptionally(it.cause());
                }
            });
        return result;
    }
}
