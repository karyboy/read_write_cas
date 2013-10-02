// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

public class CDLListFineRW<T> extends CDLList<T> {

	Element head;

	public CDLListFineRW(T v) { 
		super(v);
		this.head=super.head();
	}
		
	public Cursor reader(Element from) {
		return new Cursor(from);
	}
	
	public class Cursor extends CDLList<T>.Cursor {
		Element current;
		Writer riter;

		public Cursor(Element from){
			this.current=from;
			this.riter=new Writer(this.current);
		}

		public Element current() {
			return this.current;
		}
		
		public void previous() {
			try{
				this.current.rw.lockRead();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			
			this.current=this.current.prev;
			this.updateCurrent();
			
			try{
				this.current.next.rw.unlockRead();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
		}
		
		public void next() {
			try{
				this.current.rw.lockRead();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
				
				this.current=this.current.next;
				this.updateCurrent();
			
			try{
				this.current.prev.rw.unlockRead();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}	
		}

		public void updateCurrent(){
			this.riter.updateCurrent(this.current);
		}
		public Writer writer() {
			return this.riter;
		}
	}
	
	public class Writer extends CDLList<T>.Writer {
		public Element current;

		public Writer(Element current){
			this.current=current;
		}
		

		public boolean insertBefore(T val) {
			
			while(this.current.next.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;
			
			try{
				this.current.rw.lockWrite();
				this.current.lock_count++;
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
					
			while(this.current.prev.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;

			try{
				if(this.current!=this.current.next ){
					this.current.next.rw.lockWrite();
					this.current.lock_count++;
				}
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			
			try{
				if(this.current!=this.current.prev && this.current.next!=this.current.prev){
				this.current.prev.rw.lockWrite();
				this.current.lock_count++;
				}
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
					
			Element ele = new Element(val,this.current,this.current.prev);
			this.current.prev=ele;
			ele.prev.next=ele;
			this.current.lock_count=0;
				
			if(this.current!=this.current.prev && this.current.next!=this.current.prev)
				try{
					this.current.prev.prev.rw.unlockWrite();
				}catch(InterruptedException e){
					System.err.println(e.getMessage());
				}
				
			if(this.current!=this.current.next )
				try{
					this.current.next.rw.unlockWrite();
				}catch(InterruptedException e){
					System.err.println(e.getMessage());
				}
			try{
				this.current.rw.unlockWrite();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
				
			return true;
			
			
		}
		
		public boolean insertAfter(T val) {
			
			while(this.current.next.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;
						
			try{
				this.current.rw.lockWrite();
				this.current.lock_count++;
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
				
			
			while(this.current.prev.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;
			
			try{
				if(this.current!=this.current.next ){
					this.current.next.rw.lockWrite();
					this.current.lock_count++;
				}
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}		
			
			try{
				if(this.current!=this.current.prev && this.current.next!=this.current.prev){
					this.current.prev.rw.lockWrite();
					this.current.lock_count++;
				}
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			
			Element ele = new Element(val,this.current.next,this.current);
			this.current.next=ele;
			ele.next.prev=ele;
			this.current.lock_count=0;
				
			if(this.current!=this.current.prev && this.current.next!=this.current.prev)
				try{
					this.current.prev.rw.unlockWrite();
				}catch(InterruptedException e){
					System.err.println(e.getMessage());
				}
				
			if(this.current!=this.current.next )
				try{
					this.current.next.next.rw.unlockWrite();
				}catch(InterruptedException e){
					System.err.println(e.getMessage());
				}
			try{
				this.current.rw.unlockWrite();
			}catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
		
			return true;
				
		}

		public boolean updateCurrent(Element current){
			this.current=current;
			return true;
		}
	}
	
	
}

