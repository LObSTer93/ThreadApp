package threadapplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Поток записи
 * @author Катышев Никита
 */
public class WriteThread extends Thread{
    
    //буфер
    private Bufer bufer;

    //главная форма
    private final MainForm mainForm;

    //Файл для записи
    private File fileOut;
    
    //Наполнение буфера
    private Container container;
    
    /**
     * Конструктор
     * @param mainForm - главная форма
     */
    public WriteThread(MainForm mainForm){
        super("WriteThread");
        this.mainForm=mainForm;
    }
    
    /**
     * Инициализация потока
     * @param bufer - буфер
     * @param fileOut - Файл для чтения
     */
    public void init(Bufer bufer, File fileOut){
        this.bufer=bufer;
        this.fileOut=fileOut;
    }
            
    @Override
    public void run() {
            try{
                try(
                    FileOutputStream os=new FileOutputStream(fileOut);
                ){
                    while((container=bufer.getContainer())!=null){
                        os.write(container.getBufer(), 0, container.getLength());
                    }
                    mainForm.finish();
                }
            }catch(IOException ex){
                JOptionPane.showMessageDialog(mainForm, "Сбой при начале работы записи");
                throw new RuntimeException(ex);
            }
    }
}