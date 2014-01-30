
class RWLock{
	private final java.util.concurrent.atomic.AtomicInteger lock;
		
	public RWLock(){
		this.lock=new java.util.concurrent.atomic.AtomicInteger(1);
	}
	
	public void lockRead(){
		//System.out.println("lock read");
		int tmp=this.lock.get();
		boolean b=false;
		while((b=this.lock.compareAndSet(1,2))==false || (tmp=this.lock.get())<1){
			synchronized (this) {
				try {
					if(this.lock.get()!=1){
						System.out.println("no read --"+ this.lock.get());
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("got lock --"+this.lock.get());
		if(b==false){
			this.lock.compareAndSet(tmp, this.lock.get()+1);
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++"+ this.lock.get());
		}
	}
	
	public void unlockRead(){
		int tmp,tmp1;
		if ((tmp=this.lock.get())>1){
			//System.out.println("decing -- "+tmp);
			boolean ans=this.lock.compareAndSet(tmp,this.lock.get()-1);
			//if(ans==false)
			//System.out.println("****************************"+ans+" -- "+this.lock.get());
			if(this.lock.compareAndSet(1,1)){
				synchronized (this) {
					this.notifyAll();
				}
			}
			else{
				System.out.println("couldnt unlock +1 --"+this.lock.get());
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
			else{
				System.out.println("couldnt unlock  --"+this.lock.get());
			}
			//System.out.println("lt1 unlock read  --"+this.lock.get());
		}
		
	}
	
	public void lockWrite(){
		//System.out.println("lock write");
		int tmp;
		while(this.lock.compareAndSet(1,0)==false){
			if((tmp=this.lock.get())>1){
				//System.out.println("minusing -- "+this.lock.get());
				this.lock.compareAndSet(tmp,-1*this.lock.get());
				//System.out.println("minused == "+this.lock.get());
			}
			synchronized (this) {
				try {
					if(this.lock.get()!=1){
						System.out.println("no write --"+this.lock.get());
						this.wait();
					}
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
			if(this.lock.get()==1)
				this.notifyAll();
		}
	}
}