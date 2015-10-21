package com.bierocratie.ui.view.chat;

import com.bierocratie.ui.NavigatorUI;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.client.ChatLine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 28/09/15
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */
public class SentNotifier implements SharedChat.ChatListener {

    private static final Logger LOG = LoggerFactory.getLogger(SentNotifier.class);

    private final static long DELAY = 60*1000;

    private final static SimpleDateFormat longFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
    private final Timer timer = new Timer();
    private final SharedChat chat;
    private int n = 0;

    public SentNotifier(SharedChat chat) {
        this.chat = chat;
    }

    synchronized private int increments() {
        return ++n;
    }

    synchronized private int decrements() {
        return --n;
    }

    @Override
    public void lineAdded(ChatLine line) {
        if (line.getUser() == null) {
            return;
        }

        LOG.info("S [{}][{}]", line.getUser().getName(), line.getText());

        increments();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (decrements() == 0) {
                    String date = longFormat.format(new Date(new Date().getTime()-DELAY));
                    chat.addLine("Envoyé le " + date);
                }
            }}, DELAY);
    }


}
