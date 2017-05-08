package org.molgenis.data.jobs;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.molgenis.data.MolgenisDataException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

public class JobTest
{
	@Mock
	Callable<Authentication> callable;

	@Mock
	Progress progress;

	@Mock
	Authentication authentication;

	@Mock
	TransactionStatus transactionStatus;

	@Mock
	TransactionOperations transactionOperations;

	@Captor
	ArgumentCaptor<TransactionCallback<Authentication>> actionCaptor;

	// sneaky trick to capture the authentication the job executes under: Store it in the job result
	private Answer<Authentication> authenticationAnswer = (call) -> SecurityContextHolder.getContext()
			.getAuthentication();

	private Job<Authentication> job;
	private Job<Authentication> jobWithoutTransaction;

	@BeforeClass
	public void beforeClass()
	{
		initMocks(this);
		job = new Job<Authentication>(progress, transactionOperations, authentication)
		{
			@Override
			public Authentication call(Progress progress) throws Exception
			{
				return callable.call();
			}
		};

		jobWithoutTransaction = new Job<Authentication>(progress, null, authentication)
		{
			@Override
			public Authentication call(Progress progress) throws Exception
			{
				return callable.call();
			}
		};
	}

	@BeforeMethod
	public void beforeMethod()
	{
		reset(callable, progress, transactionOperations);
	}

	@Test
	public void testTransactionTimeout()
	{
		TransactionException transactionException = new TransactionTimedOutException("Transaction timeout test.");
		when(transactionOperations.execute(any())).thenThrow(transactionException);
		try
		{
			job.call();
			fail("TransactionException should be thrown");
		}
		catch (TransactionException expected)
		{
			assertSame(expected, transactionException);
		}
		verify(transactionOperations).execute(any());
		verify(progress).failed(transactionException);
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}

	@Test
	public void testTransactionalJob() throws Exception
	{
		when(transactionOperations.execute(actionCaptor.capture()))
				.thenAnswer((call) -> actionCaptor.getValue().doInTransaction(transactionStatus));
		when(callable.call()).thenAnswer(authenticationAnswer);
		Authentication result = job.call();

		assertEquals(result, authentication);
		verify(progress).start();
		verify(progress).success();
		verify(transactionOperations).execute(any());
		verify(callable).call();
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}

	@Test
	public void testTransactionOperationsIsCalledWithCorrectAuthentication() throws Exception
	{
		when(transactionOperations.execute(actionCaptor.capture())).thenAnswer(authenticationAnswer);
		Authentication result = job.call();

		assertEquals(result, authentication, "Entire transaction should run with specified authentication, see #6124");
		verify(transactionOperations).execute(any());
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}

	@Test
	public void testNontransactionalJob() throws Exception
	{
		when(callable.call()).thenAnswer(authenticationAnswer);
		Authentication actual = jobWithoutTransaction.call();

		assertSame(actual, authentication, "Job should run with authentication");

		verify(progress).start();
		verify(progress).success();
		verify(callable).call();
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}

	@Test
	public void testTransactionalJobFailure() throws Exception
	{
		when(transactionOperations.execute(actionCaptor.capture()))
				.thenAnswer((call) -> actionCaptor.getValue().doInTransaction(transactionStatus));
		MolgenisDataException mde = new MolgenisDataException();
		when(callable.call()).thenThrow(mde);

		try
		{
			job.call();
			fail("Job call should throw exception if subclass execution fails.");
		}
		catch (JobExecutionException ex)
		{
			assertSame(ex.getCause(), mde);
		}

		verify(transactionOperations).execute(any());
		verify(progress).start();
		verify(callable).call();
		verify(progress).failed(mde);
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}

	@Test
	public void testNontransactionalJobFailure() throws Exception
	{
		MolgenisDataException mde = new MolgenisDataException();
		when(callable.call()).thenThrow(mde);

		try
		{
			jobWithoutTransaction.call();
			fail("Job call should throw exception if subclass execution fails.");
		}
		catch (JobExecutionException ex)
		{
			assertSame(ex.getCause(), mde);
		}

		verify(progress).start();
		verify(callable).call();
		verify(progress).failed(mde);
		verifyNoMoreInteractions(callable, progress, transactionOperations);
	}
}
