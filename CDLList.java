
public class CDLList<T> {

	Element head;
	
	public CDLList(T v) {
		this.head= new Element(v);
		this.head.assignNext(this.head);
		this.head.assignPrev(this.head);
	}
	
	public class Element {
		T value;
		Element next;
		Element prev;
		boolean lock=false;
		int lock_count=0;
		RWLock rw;

		public Element(T value,Element next,Element prev){
			this.value=value;
			this.next=next;
			this.prev=prev;
			this.rw=new RWLock();
		}

		public Element(T value){
			this.value=value;
			this.next=this;
			this.prev=this;
			this.rw=new RWLock();
		}

		
		public T value() {
			return this.value;
		}

		public void assignNext(Element next){
			this.next=next;
		}

		public void assignPrev(Element prev){
			this.prev=prev;
		}


	}

	public Element head() {
		return this.head;
	}
	
	public Cursor reader(Element from) {
		System.out.println("in that  cursor ");
		return new Cursor(from);
	}
	
	public class Cursor {
		Element current;
		Writer riter;

		public Cursor(Element from){
			this.current=from;
			this.riter=new Writer(this.current);
		}
		
		public Cursor(){
			
		}

		public Element current() {
			return this.current;
		}
		
		public void previous() {
			this.current=this.current.prev;
			this.updateCurrent();
		}
		
		public void next() {
			this.current=this.current.next;
			this.updateCurrent();
		}

		public void updateCurrent(){
			this.riter.updateCurrent(this.current);
		}
		public Writer writer() {
			return this.riter;
		}
	}
	
	public class Writer {
		public Element current;

		public Writer(Element current){
			this.current=current;
		}
		
		public Writer(){
			
		}

		public boolean insertBefore(T val) {
			Element ele = new Element(val,this.current,this.current.prev);
			this.current.prev=ele;
			ele.prev.next=ele;
			return true;
		}
		
		public boolean insertAfter(T val) {
			Element ele = new Element(val,this.current.next,this.current);
			this.current.next=ele;
			ele.next.prev=ele;
			return true;
		}

		public boolean updateCurrent(Element current){
			this.current=current;
			return true;
		}
	}

	public void print(){
		Cursor c=this.reader(this.head());
		System.out.println(c.current().value());
		c.next();
		int i=0;
		while(c.current().value()!=this.head().value()){i++;
			System.out.println(c.current().value());
			c.next();
		}
		System.out.println("--"+i);
	}

}
