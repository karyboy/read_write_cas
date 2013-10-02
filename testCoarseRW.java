import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class testCoarseRW{
	@Test
    public void test1(){
//        USE THIS FOR COARSELIST AND FINELIST WITH MODIFICATIONS
        CDLCoarseRW<String> list = new CDLCoarseRW<String>("hi");
        CDLCoarseRW<String>.Element head = list.head();
        CDLCoarseRW<String>.Cursor c = list.reader(list.head());
        
        for(int i = 74; i >= 65; i--) {
            char val = (char) i;
            c.writer().insertAfter("" + val);
        }
        
        List<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            NormalThreadrwc nt = new NormalThreadrwc(list, i);
            threadList.add(nt);
        }
            
	RandomThreadrwc rt = new RandomThreadrwc(list);
	threadList.add(rt);
	
        try {
            for(Thread t : threadList){
            	t.start();
            }
            for (Thread t : threadList) {
            	t.join();
            }
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        list.print();
//    YOU MAY WANT TO INCLUDE A PRINT METHOD TO VIEW ALL THE ELEMENTS
//        list.print();
        
    }
	
}