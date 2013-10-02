//INCLUDE PACKAGENAME HERE

public class RandomThreadrwf extends Thread {

    CDLListFineRW<String> cdl;
    CDLListFineRW<String>.Cursor cursor;
    public RandomThreadrwf(CDLListFineRW<String> cdl) {
        this.cdl = cdl;
    }
    
    public void run() {
        cursor = cdl.reader(cdl.head());
        for(int i = 0;i < 10;i++) {
            double temp = java.lang.Math.random();
            int rand = (int)(temp*10)%4;


            switch(rand) {
            case 0:
                cursor.next();// Go to the next 
                break;
            case 1:
                cursor.previous();
                break;    
            case 2:
                cursor.writer().insertBefore("Random-Before");
                break;
            case 3:
                cursor.writer().insertBefore("Random-After");
                break;
            default:
                break;
            }
            yield();
        }
    }
}
