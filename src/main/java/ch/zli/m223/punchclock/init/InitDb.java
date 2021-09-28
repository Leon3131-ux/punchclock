package ch.zli.m223.punchclock.init;

import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitDb {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void initDb(){
        initAdmin();
    }

    private void initAdmin(){
        User user = new User(
                0L,
                "admin",
                bCryptPasswordEncoder.encode("admin")
        );
        userRepository.save(user);
    }

}
