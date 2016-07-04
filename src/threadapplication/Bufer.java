package threadapplication;

/**
 * Класс для взаимодействия между потоками чтения и записи
 * @author Катышев Никита
 */
public class Bufer {
    /**
     * является ли буфер заполненным.
     * Заполнен после чтения и пуст после записи
     */
    private boolean isFull=false;
    public void setIsFull(boolean isFull){
        this.isFull=isFull;
    }
    
    //Наполнение буфера
    private Container container;
    /**
     * заполняет буфер
     * @param bufer - что записали
     * @param length - сколько байт записали
     */
    public synchronized void setContainer(byte[] bufer, int length){
        /**
         * Усыпляем поток чтения, пока не запишутся предыдущие данные 
         */
        while(isFull){
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        isFull=true;
        if(length!=-1){
            System.arraycopy(bufer, 0, container.getBufer(), 0, length);
            container.setLength(length);
            mainForm.setBuferContent(bufer);
        }else{
            container=null;
        }
        //потоку записи пора писать
        notifyAll();
    }
    /**
     * Возвращает наполнение буфера
     * @return - буфер
     */
    public synchronized Container getContainer(){
        /**
         * Усыпляем поток записи, пока не прочтётся следующая порция информации
         */
        while(!isFull){
            try {
                wait();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        isFull=false;
        mainForm.setBuferContent(null);
        //потоку чтения пора читать
        notifyAll();
        if(container==null){
            return null;
        }
        return container.newInstance();
    }
    
    private final MainForm mainForm;
    
    /**
     * Конструктор
     * @param mainForm - главная форма
     * @param buferSize - размер буфера в байтах
     */
    public Bufer(MainForm mainForm, int buferSize){
        this.mainForm=mainForm;
        container=new Container(buferSize);
    }
}