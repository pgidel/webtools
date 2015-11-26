package com.bierocratie.ui.view.chat;

import com.porotype.tinycon.Tinycon;
import com.porotype.webnotifications.WebNotification;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.client.ChatLine;
import org.vaadin.chatbox.client.ChatUser;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 28/09/15
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
public class NotificationListener implements SharedChat.ChatListener {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationListener.class);

    private Tinycon tinycon;
    private WebNotification webNotification;

    private int n = 0;
    private ChatUser currentUser;

    public NotificationListener(Tinycon tinycon, WebNotification webNotification) {
        this.tinycon = tinycon;
        this.webNotification = webNotification;
    }

    public void setUser(ChatUser chatUser) {
        this.currentUser = chatUser;
    }

    @Override
    public void lineAdded(ChatLine line) {
        if (line.getUser() == null) {
            return;
        }

        LOG.info("N [{}][{}]", line.getUser().getName(), line.getText());

        if (currentUser.equals(line.getUser())) {
            reset();
        } else {
            webNotification.show(line.getUser().getName(), line.getText());
            tinycon.setBubble(++n);
        }
    }

    public void reset() {
        tinycon.setBubble(0);
        n = 0;
    }

}
