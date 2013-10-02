// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

class RWLock{
	private final java.util.concurrent.atomic.AtomicInteger lock;
		
	public RWLock(){
		this.lock=new java.util.concurrent.atomic.AtomicInteger(1);
	}
	
	public void lockRead(){
		//System.out.println("lock read");
		//int tmp=this.lock.get();
		boolean b=false;
		while((b=this.lock.compareAndSet(1,2))==false || this.lock.get()<1){
			synchronized (this) {
				try {
					System.out.println("no read --"+ this.lock.get());
					if(this.lock.get()!=1)
						this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("got lock --"+this.lock.get());
		if(b==false){
			this.lock.incrementAndGet();
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++"+ this.lock.get());
		}
	}
	
	public void unlockRead(){
		if (this.lock.get()>1){
			if(this.lock.decrementAndGet()==1){
				this.lock.set(1);
				synchronized (this) {
					this.notifyAll();
				}
			}
			//System.out.println("gt1 unlock read  --"+this.lock.get());
		}
		else if(this.lock.get()<-1){
			if(this.lock.incrementAndGet()==-1){
				this.lock.set(1);
				synchronized (this) {
					this.notifyAll();
				}
			}
			//System.out.println("lt1 unlock read  --"+this.lock.get());
		}
		else
			System.out.println("elsing");
	}
	
	public void lockWrite(){
		//System.out.println("lock write");
		while(this.lock.compareAndSet(1,0)==false){
			System.out.println("no write --"+this.lock.get());
			if(this.lock.get()>1)
				this.lock.set(-1*this.lock.get());
			synchronized (this) {
				try {
					if(this.lock.get()!=1)
						this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("got write  "+this.lock.get());
	}
	
	public void unlockWrite(){
		this.lock.set(1);
		//System.out.println("unlock write");
		synchronized (this) {
			this.notifyAll();
		}
	}
}