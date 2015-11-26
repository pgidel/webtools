package com.bierocratie.ui.view.security;

import com.bierocratie.ui.component.security.InitPasswordPopup;
import com.bierocratie.ui.component.security.ModifyPasswordPopup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 12/04/14
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLoginView extends VerticalLayout implements View, Button.ClickListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1327352315857102965L;
	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoginView.class);

    protected abstract void addAuthorizedViewsToNavigator();

    protected abstract void navigate();

    private TextField usernameField = new TextField("Nom");
    private PasswordField passwordField = new PasswordField("Mot de passe");
    private Button loginButton = new Button("Se connecter", this);

    private Button reinitPasswordButton = new Button("Réinitialiser votre mot de passe", new Button.ClickListener() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 7921267770334948691L;

		@Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            UI.getCurrent().addWindow(new InitPasswordPopup());
        }
    });

    private Button modifyPasswordButton = new Button("Modifier votre mot de passe", new Button.ClickListener() {
        /**
		 * 
		 */
		private static final long serialVersionUID = -5904207588273946633L;

		@Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            UI.getCurrent().addWindow(new ModifyPasswordPopup());
        }
    });


    public AbstractLoginView() {
        usernameField.focus();
        addComponent(usernameField);
        addComponent(passwordField);
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(loginButton);

        reinitPasswordButton.setStyleName(BaseTheme.BUTTON_LINK);
        addComponent(reinitPasswordButton);
        modifyPasswordButton.setStyleName(BaseTheme.BUTTON_LINK);
        addComponent(modifyPasswordButton);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        // TODO Supprimer
        if ("moustache".equals(usernameField.getValue())) {
            passwordField.setValue("l62f2i");
        } else if ("cab".equals(usernameField.getValue())) {
            passwordField.setValue("d3nqp8");
        } else if (usernameField.getValue() == null || "".equals(usernameField.getValue())) {
            usernameField.setValue("bierocratie");
            passwordField.setValue("brct");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(usernameField.getValue(), passwordField.getValue());
        try {
            SecurityUtils.getSubject().login(token);

            addAuthorizedViewsToNavigator();
            navigate();

            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
        } catch (AuthenticationException e) {
            LOG.error(e.getMessage());
            usernameField.setValue("");
            passwordField.setValue("");
            usernameField.focus();
            Notification.show("Login ou mot de passe invalide", Notification.Type.WARNING_MESSAGE);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            usernameField.setValue("");
            passwordField.setValue("");
            usernameField.focus();
        }
    }

}
