// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

class RWLock{
	private final java.util.concurrent.atomic.AtomicInteger lock;
		
	public RWLock(){
		this.lock=new java.util.concurrent.atomic.AtomicInteger(1);
	}
	
	public void lockRead(){
		while(this.lock.compareAndSet(1,2)==false || this.lock.get()<1)
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		this.lock.incrementAndGet();
		
	}
	
	public void unlockRead(){
		if (this.lock.get()>1)
			this.lock.decrementAndGet();
		else if(this.lock.get()<-1)
			this.lock.incrementAndGet();
		else
			this.lock.set(1);
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	public void lockWrite(){
		while(this.lock.compareAndSet(1,0)==false){
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
	}
	
	public void unlockWrite(){
		this.lock.set(1);
		synchronized (this) {
			this.notifyAll();
		}
	}
}