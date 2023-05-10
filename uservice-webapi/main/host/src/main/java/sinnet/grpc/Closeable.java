package sinnet.grpc;

import java.util.function.Consumer;

import lombok.Value;
import lombok.experimental.Accessors;

/** 
 * Simple wrapper for non-closeable beans, managed by DI.
 * Allows to close beans whrn ApplicationConext is closing.
 */
public interface Closeable {
  
  static <T> Of<T> of(T item, Consumer<T> onClose)  {
    return new Of<>(item, () -> onClose.accept(item));
  }

  static <T> Of<T> of(T item, AutoCloseable onClose)  {
    return new Of<>(item, onClose);
  }

  /** The wrapper. */
  @Value
  @Accessors(fluent = true)
  class Of<T> implements AutoCloseable {
    private T item;
    private AutoCloseable onClose;

    @Override
    public void close() throws Exception {
      onClose.close();
    }
  }
}
