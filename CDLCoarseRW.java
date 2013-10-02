// Name - Karnesh Mehra
// UB Mail - karneshm@buffalo.edu

public class CDLCoarseRW<T> extends CDLList<T> {

	Element head;
	RWLock rw;

	public CDLCoarseRW(T v) {
		super(v);
		this.head=super.head();
		this.rw=new RWLock();
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
				CDLCoarseRW.this.rw.lockRead();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			this.current=this.current.prev;
			this.updateCurrent();
			try{
				CDLCoarseRW.this.rw.unlockRead();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
		}
		
		public void next() {
			try{
				CDLCoarseRW.this.rw.lockRead();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			this.current=this.current.next;
			this.updateCurrent();
			try{
				CDLCoarseRW.this.rw.unlockRead();
			}
			catch(InterruptedException e){
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
			try{
				CDLCoarseRW.this.rw.lockWrite();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			
			Element ele = new Element(val,this.current,this.current.prev);
			this.current.prev=ele;
			ele.prev.next=ele;
			
			try{
				CDLCoarseRW.this.rw.unlockWrite();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			return true;
		}
		
		public boolean insertAfter(T val) {
			try{
				CDLCoarseRW.this.rw.lockWrite();
			}
			catch(InterruptedException e){
				System.err.println(e.getMessage());
			}
			
			Element ele = new Element(val,this.current.next,this.current);
			this.current.next=ele;
			ele.next.prev=ele;
			
			try{
				CDLCoarseRW.this.rw.unlockWrite();
			}
			catch(InterruptedException e){
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

