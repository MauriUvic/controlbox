package cat.uvic.teknos.dam.controlbox.jdbc.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoadDatabaseExtension
        implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    /** gatekeeper to prevent multiple Threads within the same routine */
    private static final Lock LOCK = new ReentrantLock();
    /** volatile boolean to tell other threads, when unblocked, whether they should try attempt start-up.  Alternatively, could use AtomicBoolean. */
    private static volatile boolean started = false;

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        LOCK.lock();
        try {
            if (!started) {
                started = true;
                SchemaLoader.loadScript("/schema.sql");
                SchemaLoader.loadScript("/data.sql");
                //context.getRoot().getStore(GLOBAL).put("any unique name", this);
            }
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void close() {
    }
}
