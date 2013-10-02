//INCLUDE PACKAGENAME HERE


public class NormalThreadrwc extends Thread {
    
    CDLCoarseRW<String> cdl;
    int id;
    CDLCoarseRW<String>.Cursor cursor;
    public NormalThreadrwc(CDLCoarseRW<String> cdl, int id) {
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
