package sinnet.read;

import java.util.UUID;

import io.vavr.collection.List;

public interface UsersRepositoryEx {
  
  List<UserModel> search(UUID projectId);
}
