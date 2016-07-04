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
    private final Bufer bufer;

    //главная форма
    private final MainForm mainForm;

    //Файл для чтения
    private final File fileIn;

    //собственный буфер потока. Из него потом сливаем в общий буфер
    private final byte[] byteBufer;
    
    /**
     * Конструктор
     * @param mainForm - главная форма
     * @param bufer - буфер
     * @param fileIn - Файл для чтения
     */
    public ReadThread(MainForm mainForm, Bufer bufer, File fileIn){
        super("ReadThread");
        this.mainForm=mainForm;
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
                while (((length = is.read(byteBufer))>0) && !mainForm.isIOEx()) {
                    bufer.setContainer(byteBufer, length);
                }
                if(mainForm.isIOEx()){
                    mainForm.finish();
                }else{
                    bufer.setContainer(null, -1);
                }
            }
        }catch(IOException ex){
            JOptionPane.showMessageDialog(mainForm, "Сбой при чтении файла!");
            mainForm.setIsIOEx();
            synchronized(bufer){
                bufer.setIsFull(true);
                bufer.notifyAll();
            }
        }
    }
}