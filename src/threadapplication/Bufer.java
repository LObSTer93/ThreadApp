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
        //потоку чтения пора читать
        notifyAll();
        if(container==null){
            return null;
        }
        return container.newInstance();
    }
    
    /**
     * Конструктор
     * @param buferSize - размер буфера в байтах
     */
    public Bufer(int buferSize){
        container=new Container(buferSize);
    }
}