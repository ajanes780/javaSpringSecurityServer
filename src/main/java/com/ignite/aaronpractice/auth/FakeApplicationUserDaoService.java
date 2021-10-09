package com.ignite.aaronpractice.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ignite.aaronpractice.security.ApplicationUserRole.*;


@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUsersDao {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return  getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }


    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(STUDENT.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "James Bond", true, true,
                        true, true),


                new ApplicationUser(ADMIN.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "Linda", true, true,
                        true, true),


                new ApplicationUser( ADMINTRAINEE.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "Tom", true, true,
                        true, true)
        );

        return applicationUsers;
    }
}
