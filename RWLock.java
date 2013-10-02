// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

class RWLock{
	private final java.util.concurrent.atomic.AtomicInteger lock;
		
	public RWLock(){
		this.lock=new java.util.concurrent.atomic.AtomicInteger(1);
	}
	
	public void lockRead(){
		//System.out.println("lock read");
		while(this.lock.compareAndSet(1,2)==false || this.lock.get()<1){
			//System.out.println("no read");
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("got lock");
		if(this.lock.get()>2)
			this.lock.incrementAndGet();
	}
	
	public void unlockRead(){
		if (this.lock.get()>1){
			int tmp=this.lock.decrementAndGet();
			//System.out.println("greater than 1--"+tmp);
		}
		else if(this.lock.get()<-1){
			//System.out.println("less than -1");
			if(this.lock.incrementAndGet()==-1)
				this.lock.set(1);
		}
		else
			//System.out.println("elsing");;//this.lock.set(1);
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	public void lockWrite(){
		//System.out.println("lock write");
		while(this.lock.compareAndSet(1,0)==false){
			//System.out.println("no write --"+this.lock.get());
			if(this.lock.get()>0)
				this.lock.set(-1*this.lock.get());
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("got write");
	}
	
	public void unlockWrite(){
		this.lock.set(1);
		//System.out.println("unlock write");
		synchronized (this) {
			this.notifyAll();
		}
	}
}