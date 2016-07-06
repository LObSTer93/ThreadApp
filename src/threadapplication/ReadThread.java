package threadapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                while (((length = is.read(byteBufer))>0) && !mainForm.isError()) {
                    while(mainForm.getStopRead()){
                        synchronized(bufer){
                            bufer.wait();
                        }
                    }
                    bufer.setContainer(byteBufer, length);
                }
                if(mainForm.isError()){
                    mainForm.finish();
                }else{
                    bufer.setContainer(null, -1);
                }
            }catch(InterruptedException e){
                JOptionPane.showMessageDialog(mainForm, "Сбой при работе с потоком чтения!");
                throw e;
            }catch(FileNotFoundException e){
                JOptionPane.showMessageDialog(mainForm, "Не найден файл для чтения!");
                throw e;
            }catch(IOException e){
                JOptionPane.showMessageDialog(mainForm, "Сбой при чтении файла!");
                throw e;
            }catch(Exception e){
                JOptionPane.showMessageDialog(mainForm, "Неизвестная ошибка!");
                throw e;
            }
        }catch(Exception e){
            mainForm.setIsError(true, e);
        }
    }
}