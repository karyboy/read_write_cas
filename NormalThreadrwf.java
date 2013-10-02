//INCLUDE PACKAGENAME HERE


public class NormalThreadrwf extends Thread {
    
    CDLListFineRW<String> cdl;
    int id;
    CDLListFineRW<String>.Cursor cursor;
    public NormalThreadrwf(CDLListFineRW<String> cdl, int id) {
        this.id = id;
        this.cdl = cdl;
        cursor = cdl.reader(cdl.head());
    }

    @Override
    public void run() {

        int offset = id * 2;
        for(int i = 0; i < offset; i++) {
            cursor.next();
        }
        
        cursor.writer().insertBefore("IB - " + id);
        cursor.writer().insertAfter("IA - " + id);
    }

}
