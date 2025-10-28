/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import common.*;
import java.util.function.Consumer;

public class MessageHandler {
    private Consumer<Message> callback;
    
    // Constructor với callback
    public MessageHandler(Consumer<Message> callback) {
        this.callback = callback;
    }
    
    public void handleMessage(Message msg) {
        // Gọi callback để xử lý message
        if (callback != null) {
            callback.accept(msg);
        }
    }
}

