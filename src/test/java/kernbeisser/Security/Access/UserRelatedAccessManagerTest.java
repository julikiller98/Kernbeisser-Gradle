package kernbeisser.Security.Access;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import kernbeisser.DBEntities.Article;
import kernbeisser.DBEntities.User;
import kernbeisser.DBEntities.UserSettingValue;
import kernbeisser.Exeptions.PermissionKeyRequiredException;
import org.junit.jupiter.api.Test;

class UserRelatedAccessManagerTest {

  User mocked = mock(User.class);

  @Test
  void userRelatedAccess() {
    Access.runWithAccessManager(
        AccessManager.NO_ACCESS_CHECKING,
        () -> {
          when(mocked.getPermissions()).thenReturn(new HashSet<>());
          UserRelatedAccessManager accessManager = new UserRelatedAccessManager(mocked);
          Access.runWithAccessManager(
              accessManager,
              () -> {
                assertDoesNotThrow(() -> new UserSettingValue(mocked).getValue());
                assertThrows(PermissionKeyRequiredException.class, () -> new Article().getName());
              });
        });
  }
}
