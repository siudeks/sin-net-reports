package onlexnet.sinnet.actests.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.vavr.collection.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import onlexnet.sinnet.actests.api.AppApiStateful;
import onlexnet.sinnet.actests.api.SessionExpectedState;

@RequiredArgsConstructor
public final class Sessions {

  /** Context of the user, wh lastly did some external operation. You may recognise such user as they are mentioned (...) as [username] (...)  */
  @Getter
  private UserContext activeUser = null;


  private final String sinnetappHost;

  private HashMap<UserEmail, UserContext> users = HashMap.empty();

  public UserContext active;

  public void given(UserEmail userEmail) {
    var state = new SessionExpectedState(userEmail.getEmail());
    var statefulAppApi = new AppApiStateful(sinnetappHost, state);
    var active = new UserContext(state, statefulAppApi);
    users = users.put(userEmail, active);
    activeUser = active;
  }

  /** Return context of already defined user, otherwise throws a exception */
  public UserContext tryGet(UserEmail userEmail) {
    var active = users.get(userEmail).get();
    activeUser = active;
    return activeUser;
  }

  /**
   * Gets API context created with JWT token for given user, or create it if not exists.
   * @param userEmail
   * @return
   */
  public UserContext getOrCreate(UserEmail userEmail) {
    var active = users.get(userEmail).getOrElse(() -> {
      given(userEmail);
      return users.get(userEmail).get();
    });
    activeUser = active;
    return activeUser;
  }

  @Data
  @AllArgsConstructor
  public static class UserContext {
    public SessionExpectedState state;
    public AppApiStateful appApi;
  }

}
