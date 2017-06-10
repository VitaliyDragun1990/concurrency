package tkach.tutorial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrate transfer operation using ExecutorService and
 * Callable objects.
 * @author Vitaliy
 *
 */
public class Operation3 {
	
	private static Account accountFrom = new Account(2500, "Account_1");
	private static Account accountTo = new Account(2500, "Account_2");

	public static void main(String[] args) throws InterruptedException {
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(new Transfer(accountFrom, accountTo, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountFrom, accountTo, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountTo, accountFrom, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountTo, accountFrom, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountFrom, accountTo, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountFrom, accountTo, (int)(Math.random()*2500D)));
		transfers.add(new Transfer(accountTo, accountFrom, (int)(Math.random()*2500D)));
		
		ScheduledExecutorService exc = Executors.newScheduledThreadPool(3);
		
exc.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.err.println("\n"+accountFrom + " fail transfer count: " + 
						accountFrom.getFailedTransferCount() + "\n" + accountTo +
						" failt transfer count: " + accountTo.getFailedTransferCount()+"\n");
				
			}
		}, 1, 1, TimeUnit.SECONDS);
		
		List<Future<Boolean>> results = exc.invokeAll(transfers); 
		
		
		exc.awaitTermination(5, TimeUnit.SECONDS);
		exc.shutdown();
		
		for (Future<Boolean> f : results) {
			try {
				System.out.println("Transfer result: " + f.get());	
			} catch (ExecutionException e) {
				System.out.println(e);
			}
			
		}

	}

}
