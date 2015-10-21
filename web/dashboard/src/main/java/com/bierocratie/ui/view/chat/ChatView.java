package com.bierocratie.ui.view.chat;

import com.bierocratie.ui.NavigatorUI;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import org.vaadin.chatbox.ChatBox;
import org.vaadin.chatbox.SharedChat;
import org.vaadin.chatbox.client.ChatLine;
import org.vaadin.chatbox.client.ChatUser;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 24/09/15
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
public class ChatView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(ChatView.class);

    private static SharedChat chat = new SharedChat();

    private ChatBox chatbox;

    static {
        chat.addListener(new SentNotifier(chat));
    }

    public ChatView() {
        DashboardMenuBar dashboardMenuBar = new DashboardMenuBar();
        addComponent(dashboardMenuBar);

        chatbox = new ChatBox(chat);
        chatbox.setShowSendButton(false);

        String ipAddress = Page.getCurrent().getWebBrowser().getAddress();
        switch (ipAddress) {
            case "192.168.0.22":
                connectUser("brct");
                break;
            case "192.168.0.25":
                // fe80:0:0:0:6885:32cc:ae19:5a87%2
                connectUser("pir");
                break;
            case "192.168.0.42":
                connectUser("jax");
                break;
            default:
                addLoginForm();
        }

        chatbox.setWidth("100%");
        addComponent(chatbox);
    }

    private void addLoginForm() {
        HorizontalLayout hl = new HorizontalLayout();
        TextField tf = new TextField("Pseudo :");
        Button b = new Button("Se connecter");
        tf.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent focusEvent) {
                b.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            }
        });
        tf.focus();
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                connectUser(tf.getValue());
                b.removeClickShortcut();
            }
        });
        hl.addComponent(tf);
        hl.addComponent(b);
        addComponent(hl);
    }

    private void connectUser(String nickname) {
        ChatUser user = ChatUser.newUser(nickname);
        chatbox.setUser(user);
        chat.addLine(new ChatLine(user.getName()+" a rejoint le chat."));
        chatbox.focusToInputField();

        NavigatorUI ui = (NavigatorUI)(UI.getCurrent());
        chat.addListener(new NotificationListener(user, ui.getTinycon(), ui.getWebNotification()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
