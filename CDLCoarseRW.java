
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
			CDLCoarseRW.this.rw.lockRead();
			this.current=this.current.prev;
			this.updateCurrent();
			CDLCoarseRW.this.rw.unlockRead();
		}
		
		public void next() {
			CDLCoarseRW.this.rw.lockRead();
			this.current=this.current.next;
			this.updateCurrent();
			CDLCoarseRW.this.rw.unlockRead();
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
			CDLCoarseRW.this.rw.lockWrite();
			
			Element ele = new Element(val,this.current,this.current.prev);
			this.current.prev=ele;
			ele.prev.next=ele;
			
			CDLCoarseRW.this.rw.unlockWrite();
			return true;
		}
		
		public boolean insertAfter(T val) {
			CDLCoarseRW.this.rw.lockWrite();
			
			Element ele = new Element(val,this.current.next,this.current);
			this.current.next=ele;
			ele.next.prev=ele;
			
			CDLCoarseRW.this.rw.unlockWrite();
			
			return true;
		}

		public boolean updateCurrent(Element current){
			this.current=current;
			return true;
		}
	}
	
	
}

