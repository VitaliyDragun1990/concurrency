package tkach.tutorial;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	
	private int balance;
	private String accountName;
	private Lock lock;
	private AtomicInteger failCounter;
	
	public Account(int initialBalance, String accountName) {
		this.balance = initialBalance;
		this.accountName = accountName;
		lock = new ReentrantLock();
		failCounter = new AtomicInteger(0);
	}
	
	public void withdraw(int amount) {
		balance -= amount;
	}
	
	public void deposit(int amount) {
		balance += amount;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public String toString() {
		return accountName;
	}
	
	public Lock getLock() {
		return lock;
	}
	
	public void incFailedTransferCount() {
		failCounter.incrementAndGet();
	}
	
	public int getFailedTransferCount() {
		return failCounter.get();
	}

}
