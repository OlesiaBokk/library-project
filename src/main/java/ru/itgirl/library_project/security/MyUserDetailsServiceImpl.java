package ru.itgirl.library_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.model.entity.MyUser;
import ru.itgirl.library_project.repository.MyUserRepository;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    private final MyUserRepository myUserRepository;

    @Autowired
    public MyUserDetailsServiceImpl(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return MyUserDetailsImpl.build(myUser);
    }
}
