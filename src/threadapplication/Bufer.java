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
     * @throws java.lang.InterruptedException - при попытке заснуть поток
     */
    public synchronized void setContainer(byte[] bufer, int length) throws InterruptedException{
        /**
         * Усыпляем поток чтения, пока не запишутся предыдущие данные 
         */
        while(isFull || mainForm.getStopRead()){
            wait();
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
     * @throws java.lang.InterruptedException - при попытке заснуть поток
     */
    public synchronized Container getContainer() throws InterruptedException{
        /**
         * Усыпляем поток записи, пока не прочтётся следующая порция информации
         */
        while(!isFull || mainForm.getStopWrite()){
            wait();
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