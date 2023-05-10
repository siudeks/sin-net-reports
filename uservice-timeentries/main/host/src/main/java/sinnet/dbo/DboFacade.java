package sinnet.dbo;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/** Facade for all DBO operations. */
@Component
@RequiredArgsConstructor
public final class DboFacade {

  @Delegate
  private final DboRemove dboRemove;

  @Delegate
  private final DboGet dboOwned;

  @Delegate
  private final DboUpdate dboUpdate;

  @Delegate
  private final DboCreate dboCreate;

}
