package tkach.tutorial;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Transfer implements Callable<Boolean> {
	
	private Account accountFrom;
	private Account accountTo;
	private int amount;
	
	private final static long waitTimeForLock = 1500;
	
	public Transfer(Account from, Account to, int amount) {
		accountFrom = from;
		accountTo = to;
		this.amount = amount;
	}

	@Override
	public Boolean call() throws Exception {
		
		if (accountFrom.getBalance() < amount) {
			throw new InsufficientFundsException("Not enought funds to transfer");
		}
		
		// Random delay for simulating possible deadlock
		Thread.sleep((long) (Math.random()*1500));
		
		if (accountFrom.getLock().tryLock(waitTimeForLock, TimeUnit.MILLISECONDS)) {
			printLog(" INFO - Locked ", accountFrom);
			try {
				printLog(" INFO - Trying to lock ", accountTo);
				if (accountTo.getLock().tryLock(waitTimeForLock, TimeUnit.MILLISECONDS)) {
					printLog(" INFO - Locked ", accountTo);
					try {
						// Do transfer
						System.out.println("Process transfer operation...");
						accountFrom.withdraw(amount);
						// Random delay for transfer
						Thread.sleep((long) (Math.random()*4500D));
						accountTo.deposit(amount);
						System.out.println(
								"The transfer is successful.\n" + "Account status after transfer operation:\n" + accountFrom
										+ " = " + accountFrom.getBalance() + ", " + 
										accountTo + " = " + accountTo.getBalance() + "\n");
						return true;
					} finally {
						accountTo.getLock().unlock();
					}
				} else {
					printLog(" ERROR - Cannot get a lock for ", accountTo);
					accountTo.incFailedTransferCount();
					return false;
				}
			} finally {
				accountFrom.getLock().unlock();

			}
		} else {
			printLog(" ERROR - Cannot get a lock for ", accountFrom);
			accountFrom.incFailedTransferCount();
			return false;
		}
	}
	
	/**
	 * Simple logging method for printing information on the console
	 * @param message Information to be printed
	 * @param acc Account instance information belong to
	 */
	private static void printLog(String message, Account acc) {
		System.out.println(LocalDateTime.now() + " ["+Thread.currentThread().getName()+"]" + message + acc);
	}

}
