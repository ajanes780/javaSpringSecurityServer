package com.ignite.aaronpractice.auth;

import java.util.Optional;


public interface ApplicationUsersDao {
      Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
