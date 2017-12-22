package sec.project.config;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Users;
import sec.project.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private Map<String, String> accountDetails;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        this.accountDetails = new TreeMap<>();
		String username = "john";
		String password = "john";
		this.accountDetails.put(username, passwordEncoder.encode(password));
		userRepository.save(new Users(username, password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!this.accountDetails.containsKey(username)) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                this.accountDetails.get(username),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
