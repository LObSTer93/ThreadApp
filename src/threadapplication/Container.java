package threadapplication;

/**
 * Наполнение буфера
 * @author Катышев Никита
 */
public class Container{
    /**
     * буфер
     */
    private final byte[] bufer;
    public byte[] getBufer(){
        return bufer;
    }
    /**
     * Сколько записано в буфере
     */
    private int length=0;
    public int getLength(){
        return length;
    }
    public void setLength(int length){
        this.length=length;
    }
    
    /**
     * Конструктор
     * @param buferSize - сколько байт храним
     */
    public Container(int buferSize){
        bufer=new byte[buferSize];
    }
    
    /**
     * Клонирование объекта
     * @return - клон объекта
     */
    public Container newInstance(){
        Container container=new Container(bufer.length);
        System.arraycopy(bufer, 0, container.getBufer(), 0, length);
        container.length=length;
        return container;
    }
}