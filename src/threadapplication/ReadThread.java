package threadapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Поток чтения
 * @author Катышев Никита
 */
public class ReadThread extends Thread{

    //буфер
    private Bufer bufer;

    //главная форма
    private final MainForm mainForm;

    //Файл для чтения
    private File fileIn;

    //собственный буфер потока. Из него потом сливаем в общий буфер
    private byte[] byteBufer;
    
    /**
     * Конструктор
     * @param mainForm - главная форма
     */
    public ReadThread(MainForm mainForm){
        super("ReadThread");
        this.mainForm=mainForm;
    }
    
    /**
     * Инициализация потока
     * @param bufer - буфер
     * @param fileIn - Файл для чтения
     */
    public void init(Bufer bufer, File fileIn){
        this.bufer=bufer;
        this.fileIn=fileIn;
        byteBufer=new byte[mainForm.getBuferSize()];
    }
    
    @Override
    public void run() {
        try{
            try(
                FileInputStream is=new FileInputStream(fileIn);
            ){
                int length;
                while ((length = is.read(byteBufer)) > 0) {
                    bufer.setContainer(byteBufer, length);
                }
                bufer.setContainer(null, -1);
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(mainForm, "Сбой при чтении файла");
            throw new RuntimeException(ex);
        }
    }
}