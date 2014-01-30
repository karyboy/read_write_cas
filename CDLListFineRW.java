
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
			this.current.rw.lockRead();
			
			this.current=this.current.prev;
			this.updateCurrent();
			
			this.current.next.rw.unlockRead();
			
		}
		
		public void next() {
			this.current.rw.lockRead();
			
			this.current=this.current.next;
			this.updateCurrent();
			
			this.current.prev.rw.unlockRead();
				
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
			
			this.current.rw.lockWrite();
			this.current.lock_count++;
					
			while(this.current.prev.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;

			if(this.current!=this.current.next ){
				this.current.next.rw.lockWrite();
				this.current.lock_count++;
			}
			
			if(this.current!=this.current.prev && this.current.next!=this.current.prev){
				this.current.prev.rw.lockWrite();
				this.current.lock_count++;
			}
			
					
			Element ele = new Element(val,this.current,this.current.prev);
			this.current.prev=ele;
			ele.prev.next=ele;
			this.current.lock_count=0;
				
			if(this.current!=this.current.prev && this.current.next!=this.current.prev)
				this.current.prev.prev.rw.unlockWrite();
				
			if(this.current!=this.current.next )
				this.current.next.rw.unlockWrite();
				
			this.current.rw.unlockWrite();
				
			return true;
			
			
		}
		
		public boolean insertAfter(T val) {
			
			while(this.current.next.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;
						
			this.current.rw.lockWrite();
			this.current.lock_count++;
				
			
			while(this.current.prev.lock_count>0 && this.current!=this.current.next && this.current.next!=this.current.prev)
				;
			
			if(this.current!=this.current.next ){
				this.current.next.rw.lockWrite();
				this.current.lock_count++;
			}
			
			if(this.current!=this.current.prev && this.current.next!=this.current.prev){
				this.current.prev.rw.lockWrite();
				this.current.lock_count++;
			}
			
			Element ele = new Element(val,this.current.next,this.current);
			this.current.next=ele;
			ele.next.prev=ele;
			this.current.lock_count=0;
				
			if(this.current!=this.current.prev && this.current.next!=this.current.prev)
				this.current.prev.rw.unlockWrite();
				
			if(this.current!=this.current.next )
				this.current.next.next.rw.unlockWrite();
			
			this.current.rw.unlockWrite();
			
			return true;
				
		}

		public boolean updateCurrent(Element current){
			this.current=current;
			return true;
		}
	}
	
	
}

