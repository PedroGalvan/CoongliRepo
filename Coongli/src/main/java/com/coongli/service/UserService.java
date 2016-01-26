package com.coongli.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.coongli.domain.Action;
import com.coongli.security.Authority;
import com.coongli.domain.Goal;
import com.coongli.domain.Invitation;
import com.coongli.domain.Invoice;
import com.coongli.domain.Mesage;
import com.coongli.domain.Messagefolder;
import com.coongli.domain.Resourcecategory;
import com.coongli.domain.Session;
import com.coongli.domain.User;
import com.coongli.forms.RegisterUserForm;
import com.coongli.repository.AuthorityRepository;
import com.coongli.repository.PersistentTokenRepository;
import com.coongli.repository.UserRepository;
import com.coongli.security.LoginService;
import com.coongli.security.SecurityUtils;
import com.coongli.security.UserAccount;
import com.coongli.service.util.RandomUtil;
import com.coongli.web.rest.dto.ManagedUserDTO;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

 	// Supporting services
 	// ------------------------------------------------------
 	@Autowired
 	private MessagefolderService messageFolderService;

    
    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }
/*
    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserDTO managedUserDTO) {
        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("en"); // default language is English
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
        if (managedUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserDTO.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    /*
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    /*
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
  */  
 // Simple CRUD methods -----------------------------------------------------
 	
    public User create() {
		User result;
		UserAccount userAccount;
		Authority authority;
		Collection<Authority> authorities;
		Collection<Messagefolder> messageFolders;
		Collection<Mesage> receivedMesages;
		Collection<Mesage> sentMesages;
		Collection<Invitation> receivedInvitations;
		Collection<Invitation> sentInvitations;
		Collection<Resourcecategory> resourceCategories;
		Collection<Invoice> invoices;
		Collection<Goal> goals;
		Collection<Action> actions;
		Collection<Session> sessions;

		resourceCategories = new ArrayList<Resourcecategory>();
		invoices = new ArrayList<Invoice>();
		goals = new ArrayList<Goal>();
		actions = new ArrayList<Action>();
		sessions = new ArrayList<Session>();
		messageFolders = new ArrayList<Messagefolder>();
		receivedMesages = new ArrayList<Mesage>();
		sentMesages = new ArrayList<Mesage>();
		receivedInvitations = new ArrayList<Invitation>();
		sentInvitations = new ArrayList<Invitation>();
		authority = new Authority();
		authorities = new ArrayList<Authority>();
		userAccount = new UserAccount();
		result = new User();

		authority.setAuthority(Authority.USER);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		result.setMessagefolders(messageFolders);
		result.setSentmesages(sentMesages);
		result.setReceivedmesages(receivedMesages);
		result.setSentinvitations(sentInvitations);
		result.setReceivedinvitations(receivedInvitations);
		result.setResourcecategories(resourceCategories);
		result.setInvoices(invoices);
		result.setGoals(goals);
		result.setActions(actions);
		result.setSessions(sessions);
		result.setActivated(false);

		return result;
	}

	public User save(User user) {
		Assert.notNull(user);
		User result;
		Md5PasswordEncoder encoder;
		String password;
		String repeatPassword;
		UserAccount userAccount;
		Collection<Messagefolder> messageFolders;
		Messagefolder inbox, outbox, trashbox;

		messageFolders = new ArrayList<Messagefolder>();
		inbox = messageFolderService.create("Inbox");
		outbox = messageFolderService.create("Outbox");
		trashbox = messageFolderService.create("Trashbox");
		messageFolders.add(inbox);
		messageFolders.add(outbox);
		messageFolders.add(trashbox);
		user.setMessagefolders(messageFolders);

		userAccount = user.getUserAccount();
		encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(userAccount.getPassword(), null);
		repeatPassword = encoder.encodePassword(
				userAccount.getRepeatPassword(), null);

		Assert.isTrue(password.equals(repeatPassword));

		userAccount.setPassword(password);
		userAccount.setRepeatPassword(repeatPassword);

		user.setUserAccount(userAccount);

		result = userRepository.save(user);
		inbox.setActor(result);
		outbox.setActor(result);
		trashbox.setActor(result);
		messageFolderService.save(inbox);
		messageFolderService.save(outbox);
		messageFolderService.save(trashbox);

		return result;
	}

    
 	public User findOne(Integer pilgrimId) {
 		Assert.notNull(pilgrimId);
 		User result;

 		result = userRepository.findOne(pilgrimId);

 		return result;
 	}

 	// Other business methods
 	// ----------------------------------------------------

 	public User findOneByPrincipal() {
 		User result;

 		result = userRepository.findOneByPrincipal(LoginService.getPrincipal()
 				.getId());

 		return result;
 	}

 	public Collection<User> findAll() {
 		Collection<User> result;

 		result = userRepository.findAll();

 		return result;
 	}

 	public User findByUserAccount(UserAccount userAccount) {
 		Assert.notNull(userAccount);
 		User result;

 		result = userRepository.findByUserAccount(userAccount.getId());

 		return result;
 	}
	
 	public User activate(int userId) {
 		User result, user;

 		user = findOne(userId);
 		if (user.getActivated()) {
 			user.setActivated(false);
 		} else {
 			user.setActivated(true);
 		}
 		result = userRepository.save(user);

 		return result;
 	}

 	public Collection<User> findAllToInvite(Session session) {
 		Collection<User> result, aux, aux2;

 		aux = session.getUsers();
 		aux2 = userRepository.findUsersBySession(session.getId());
 		result = findAll();
 		result.removeAll(aux);
 		result.removeAll(aux2);

 		return result;
 	}

 	public Collection<User> findAllByKeyWord(String kw) {
 		Assert.notNull(kw);
 		Collection<User> result;
 		if (kw.equals("")) {
 			result = findAll();
 		} else {
 			result = userRepository.findAllByKeyWord(kw);
 		}

 		return result;
 	}

 	public User reconstruct(RegisterUserForm registerUserForm) {
 		Assert.notNull(registerUserForm);
 		User user;
 		UserAccount us;

 		if (registerUserForm.getId() == 0) {
 			user = create();
 		} else {
 			user = findOne(registerUserForm.getId());
 		}

 		us = user.getUserAccount();

 		us.setUsername(registerUserForm.getUserName());
 		us.setPassword(registerUserForm.getPassword());
 		us.setRepeatPassword(registerUserForm.getRepeatPassword());
 		user.setName(registerUserForm.getName());
 		user.setSurname(registerUserForm.getSurname());
 		user.setEmail(registerUserForm.getEmail());
 		user.setPhone(registerUserForm.getPhone());
 		user.setAddress(registerUserForm.getAddress());
 		user.setCity(registerUserForm.getCity());
 		user.setNationality(registerUserForm.getNationality());
 		user.setDni(registerUserForm.getDni());
 		user.setBirthdate(registerUserForm.getBirthDate());
 		user.setActivated(false);

 		return user;
 	}

 	public RegisterUserForm fragment(User user) {
 		Assert.notNull(user);

 		RegisterUserForm result;

 		result = new RegisterUserForm();

 		result.setUserName(user.getUserAccount().getUsername());
 		result.setPassword(user.getUserAccount().getPassword());
 		result.setRepeatPassword(user.getUserAccount().getRepeatPassword());
 		result.setName(user.getName());
 		result.setSurname(user.getSurname());
 		result.setEmail(user.getEmail());
 		result.setPhone(user.getPhone());
 		result.setNationality(user.getNationality());
 		result.setAddress(user.getAddress());
 		result.setCity(user.getCity());
 		result.setDni(user.getDni());
 		result.setBirthDate(user.getBirthdate());

 		return result;

 	}

 	public User addResourceCategory(User user, Resourcecategory resourceCategory) {
 		Assert.notNull(resourceCategory);
 		Assert.notNull(user);
 		Assert.isTrue(!resourceCategory.getUsers().contains(user));
 		User result;
 		Collection<Resourcecategory> resourceCategories;

 		resourceCategories = user.getResourcecategories();
 		resourceCategories.add(resourceCategory);
 		user.setResourcecategories(resourceCategories);

 		result = userRepository.save(user);
 		return result;
 	}

 	public User deleteResourceCategory(User user,
 			Resourcecategory resourceCategory) {
 		Assert.notNull(resourceCategory);
 		Assert.notNull(user);
 		Assert.isTrue(resourceCategory.getUsers().contains(user));
 		User result;
 		Collection<Resourcecategory> resourceCategories;

 		resourceCategories = user.getResourcecategories();
 		resourceCategories.remove(resourceCategory);
 		user.setResourcecategories(resourceCategories);

 		result = userRepository.save(user);
 		return result;
 	}

 	public void checkIsAdmin() {
 		UserAccount principal;
 		Authority a;
 		a = new Authority();
 		a.setAuthority("ADMIN");
 		principal = LoginService.getPrincipal();

 		Assert.isTrue(principal.getAuthorities().contains(a));
 	}

 	public char calculaLetra(String dni) {
 		String juegoCaracteres = "TRWAGMYFPDXBNJZSQVHLCKET";
 		Integer i = new Integer(dni);
 		int modulo = i % 23;
 		char letra = juegoCaracteres.charAt(modulo);
 		return letra;
 	}

}
