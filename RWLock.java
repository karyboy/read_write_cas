// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

class RWLock{
	private final java.util.concurrent.atomic.AtomicInteger lock;
		
	public RWLock(){
		this.lock=new java.util.concurrent.atomic.AtomicInteger(1);
	}
	
	public void lockRead(){
		//System.out.println("lock read");
		int tmp=this.lock.get();
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
		int tmp,tmp1;
		if ((tmp=this.lock.get())>1){
			System.out.println("decing -- "+tmp);
			
			boolean ans=this.lock.compareAndSet(tmp,this.lock.get()-1);
			System.out.println("decedd "+ans+" -- "+this.lock.get());
			if(this.lock.compareAndSet(1,1)){
				synchronized (this) {
					this.notifyAll();
				}
			}
			//System.out.println("gt1 unlock read  --"+this.lock.get());
		}
	   if((tmp1=this.lock.get())<-1){
			this.lock.compareAndSet(tmp1,this.lock.get()+1);
			if(this.lock.compareAndSet(-1,1)){
				synchronized (this) {
					this.notifyAll();
				}
			}
			//System.out.println("lt1 unlock read  --"+this.lock.get());
		}
		
	}
	
	public void lockWrite(){
		//System.out.println("lock write");
		int tmp;
		while(this.lock.compareAndSet(1,0)==false){
			if((tmp=this.lock.get())>1){
				System.out.println("minusing -- "+this.lock.get());
				this.lock.compareAndSet(tmp,-1*this.lock.get());
				System.out.println("minused == "+this.lock.get());
			}
			synchronized (this) {
				try {
					System.out.println("no write --"+this.lock.get());
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