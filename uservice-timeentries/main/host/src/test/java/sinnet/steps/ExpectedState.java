package sinnet.steps;

import java.util.Optional;

import org.springframework.stereotype.Component;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import io.vavr.control.Option;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import sinnet.grpc.common.EntityId;
import sinnet.grpc.timeentries.LocalDate;
import sinnet.models.ProjectId;
import sinnet.models.ValEmail;
import sinnet.models.ValName;

/**
 * Represents single endpoin connection, where someone logged in and will be the
 * same till the end of the session.
 */
@Component
public class ExpectedState {

  @Getter
  LocalDate todayAsDto = LocalDate.newBuilder().setMonth(1).setDay(1).setYear(2020).build();

  private Queue<AppEvent> appEvents = Queue.empty();

  sealed interface AppEvent {
  }

  record ProjectUpdated(@Nonnull ValName projectAlias, ValName projectName) implements AppEvent {
  }

  record ProjectCreated(@Nonnull ValName projectAlias, ProjectId projectId) implements AppEvent {
  }

  record TimeentryCreated(EntityId id, LocalDate when) implements AppEvent {
  }

  record KnownUser(@Nonnull ValName userAlias, ValEmail email) implements AppEvent { }

  record ActiveUser(@Nonnull ValName userAlias) implements AppEvent { }


  private void on(AppEvent appEvent) {
    appEvents = appEvents.append(appEvent);
  }

  AppState buildExpectedState() {
    return appEvents.foldLeft(new AppState(), this::applyEvent);
  }

  AppState applyEvent(AppState facts, AppEvent event) {
    
    var users = facts.users();
    var projects = facts.projects();
    var timeentries = facts.timeentries();
    var currentUser = facts.currentUser();
    var activeProject = facts.activeProject();

    if (event instanceof ProjectUpdated e) {
      var state = facts.projects.get(e.projectAlias).get();
      var alias = e.projectAlias();
      state = new ProjectState(state.id(), e.projectName());
      projects = projects.put(alias, state);
    } else if (event instanceof ProjectCreated e) {
      var alias = e.projectAlias();
      var state = new ProjectState(e.projectId, null);
      projects = projects.put(alias, state);
      // to simplify tests: lastly creaated project is the current one
      activeProject = alias;
    } else if (event instanceof TimeentryCreated e) {
      var id = e.id();
      var state = new TimeentryState(e.when());
      timeentries = timeentries.put(id, state);
    } else if (event instanceof KnownUser e) {
      var userAlias = e.userAlias;
      var userEmail = e.email;
      var maybeUser = ((Option<ValEmail>) users.get(userAlias)).toJavaOptional();
      if (maybeUser.isPresent()) {
        currentUser = userAlias;
      } else {
        users = users.put(userAlias, userEmail);
        currentUser = userAlias;
      }
    }
    else if (event instanceof ActiveUser e) {
      var userAlias = e.userAlias;
      var user = users.get(userAlias).get();
      currentUser = userAlias;
    } else {
      throw new IllegalStateException();
    }
    return new AppState(users, projects, timeentries, currentUser, activeProject);
  }

  public void onKnownSession(ValName userAlias, ValEmail email) {
    on(new KnownUser(userAlias, email));
  }

  public void onProjectUpdated(@NonNull ValName projectAlias, ValName projectName) {
    on(new ProjectUpdated(projectAlias, projectName));
  }

  public void onProjectCreated(@NonNull ValName projectAlias, ProjectId projectId) {
    on(new ProjectCreated(projectAlias, projectId));
  }

  public void onTimeentryCreated(EntityId id, LocalDate when) {
    on(new TimeentryCreated(id, when));
  }

  public void onActiveUser(ValName userAlias) {
    on(new ActiveUser(userAlias));
  }

  record AppState(
    Map<ValName, ValEmail> users,
    Map<ValName, ProjectState> projects,
    Map<EntityId, TimeentryState> timeentries,
    ValName currentUser,
    ValName activeProject) {
    
    public AppState() {
      this(HashMap.empty(), HashMap.empty(), HashMap.empty(), ValName.empty(), ValName.empty());
    }

    public Optional<ProjectState> getProjectId(@NonNull ValName projectAlias) {
      return this.projects.get(projectAlias).toJavaOptional();
    }

    public Optional<ValEmail> user(ValName alias) {
      return this.users.get(alias).toJavaOptional();
    }

    public EntityId latestTimeentryId() {
      return this.timeentries.last()._1;
    }

    public TimeentryState latestTimeentry() {
      return this.timeentries.last()._2;
    }
  }
  
  record ProjectState(ProjectId id, ValName name) {}
  record TimeentryState(LocalDate when) {}


}
