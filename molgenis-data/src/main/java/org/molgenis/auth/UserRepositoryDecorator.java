package org.molgenis.auth;

import com.google.common.collect.Iterators;
import org.molgenis.data.AbstractRepositoryDecorator;
import org.molgenis.data.DataService;
import org.molgenis.data.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.molgenis.auth.AuthorityMetaData.ROLE;
import static org.molgenis.auth.UserAuthorityMetaData.USER;
import static org.molgenis.auth.UserAuthorityMetaData.USER_AUTHORITY;
import static org.molgenis.security.core.utils.SecurityUtils.AUTHORITY_SU;

public class UserRepositoryDecorator extends AbstractRepositoryDecorator<User>
{
	private static final int BATCH_SIZE = 1000;

	private final Repository<User> decoratedRepository;
	private final UserAuthorityFactory userAuthorityFactory;
	private final DataService dataService;
	private final PasswordEncoder passwordEncoder;

	public UserRepositoryDecorator(Repository<User> decoratedRepository, UserAuthorityFactory userAuthorityFactory,
			DataService dataService, PasswordEncoder passwordEncoder)
	{
		this.decoratedRepository = requireNonNull(decoratedRepository);
		this.userAuthorityFactory = requireNonNull(userAuthorityFactory);
		this.dataService = requireNonNull(dataService);
		this.passwordEncoder = requireNonNull(passwordEncoder);
	}

	@Override
	protected Repository<User> delegate()
	{
		return decoratedRepository;
	}

	@Override
	public void add(User entity)
	{
		encodePassword(entity);
		decoratedRepository.add(entity);
		addSuperuserAuthority(entity);
	}

	@Override
	public void update(User entity)
	{
		updatePassword(entity);
		decoratedRepository.update(entity);
		updateSuperuserAuthority(entity);
	}

	@Override
	public Integer add(Stream<User> entities)
	{
		AtomicInteger count = new AtomicInteger();
		Iterators.partition(entities.iterator(), BATCH_SIZE).forEachRemaining(users ->
		{
			users.forEach(this::encodePassword);

			Integer batchCount = decoratedRepository.add(users.stream());
			count.addAndGet(batchCount);

			users.forEach(this::addSuperuserAuthority);
		});
		return count.get();
	}

	@Override
	public void update(Stream<User> entities)
	{
		entities = entities.map(entity ->
		{
			updatePassword(entity);
			return entity;
		});
		decoratedRepository.update(entities);
	}

	private void updatePassword(User user)
	{
		User currentUser = findOneById(user.getId());

		String currentPassword = currentUser.getPassword();
		String password = user.getPassword();
		//password is updated
		if (!currentPassword.equals(password))
		{
			password = passwordEncoder.encode(user.getPassword());
		}
		user.setPassword(password);
	}

	private void encodePassword(User user)
	{
		String password = user.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
	}

	private void addSuperuserAuthority(User user)
	{
		Boolean isSuperuser = user.isSuperuser();
		if (isSuperuser != null && isSuperuser)
		{
			UserAuthority userAuthority = userAuthorityFactory.create();
			userAuthority.setUser(user);
			userAuthority.setRole(AUTHORITY_SU);

			getUserAuthorityRepository().add(userAuthority);
		}
	}

	private void updateSuperuserAuthority(User user)
	{
		Repository<UserAuthority> userAuthorityRepo = getUserAuthorityRepository();
		UserAuthority suAuthority = userAuthorityRepo.query().eq(USER, user).and().eq(ROLE, AUTHORITY_SU).findOne();

		Boolean isSuperuser = user.isSuperuser();
		if (isSuperuser != null && isSuperuser)
		{
			if (suAuthority == null)
			{
				UserAuthority userAuthority = userAuthorityFactory.create();
				userAuthority.setUser(user);
				userAuthority.setRole(AUTHORITY_SU);
				userAuthorityRepo.add(userAuthority);
			}
		}
		else
		{
			if (suAuthority != null)
			{
				userAuthorityRepo.deleteById(suAuthority.getId());
			}
		}
	}

	private Repository<UserAuthority> getUserAuthorityRepository()
	{
		return dataService.getRepository(USER_AUTHORITY, UserAuthority.class);
	}
}
