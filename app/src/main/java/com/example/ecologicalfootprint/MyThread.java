package com.example.ecologicalfootprint;

import android.os.Message;
import android.os.Handler;
public class MyThread extends Thread{
    Handler handler;
    private char[] textToView;
    private String text;
    public MyThread(String text, Handler handler){
        this.handler = handler;
        this.text = text;
        this.textToView = new char[text.length()];
    }

    @Override
    public void run() {
        super.run();
        char[] textchars = text.toCharArray();

        for(int i = 0; i < textchars.length;i++){
            char ch = textchars[i];
            textToView[i] = ch;

            Message msg = new Message();
            msg.obj = textToView;
            handler.sendMessage(msg);

            try{
                switch(ch){
                    case ',': Thread.sleep(200); break;
                    case '!': Thread.sleep(300); break;
                    case '.': Thread.sleep(300); break;
                    case '?': Thread.sleep(300); break;
                    default:  Thread.sleep(100); break;
                }
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
