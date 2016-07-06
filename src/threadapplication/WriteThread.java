package threadapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Поток записи
 * @author Катышев Никита
 */
public class WriteThread extends Thread{
    
    //буфер
    private final Bufer bufer;

    //главная форма
    private final MainForm mainForm;

    //Файл для записи
    private final File fileOut;
    
    //Наполнение буфера
    private Container container;
    
    /**
     * Конструктор
     * @param mainForm - главная форма
     * @param bufer - буфер
     * @param fileOut - Файл для чтения
     */
    public WriteThread(MainForm mainForm, Bufer bufer, File fileOut){
        super("WriteThread");
        this.mainForm=mainForm;
        this.bufer=bufer;
        this.fileOut=fileOut;
    }
    
    @Override
    public void run() {
        try{
            try(
                FileOutputStream os=new FileOutputStream(fileOut);
            ){
                while((container=bufer.getContainer())!=null && !mainForm.isError()){
                    os.write(container.getBufer(), 0, container.getLength());
                    while(mainForm.getStopWrite()){
                        synchronized(bufer){
                            bufer.wait();
                        }
                    }
                }
                mainForm.finish();
            }catch(InterruptedException e){
                JOptionPane.showMessageDialog(mainForm, "Сбой при работе с потоком записи!");
                throw e;
            }catch(FileNotFoundException e){
                JOptionPane.showMessageDialog(mainForm, "Не найден файл для записи!");
                throw e;
            }catch(IOException e){
                JOptionPane.showMessageDialog(mainForm, "Сбой при чтении файла!");
                throw e;
            }catch(Exception e){
                JOptionPane.showMessageDialog(mainForm, "Неизвестная ошибка!");
                throw e;
            }
        }catch(Exception e){
            mainForm.setIsError(false, e);
        }
    }
}