package com.bierocratie.ui.component.security;

import com.bierocratie.db.persistence.PersistenceManager;
import com.bierocratie.db.security.AccountDAO;
import com.bierocratie.email.EmailSender;
import com.bierocratie.model.security.Account;
import com.bierocratie.security.PasswordHasher;
import com.vaadin.event.ShortcutAction;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.ui.*;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/05/14
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class ModifyPasswordPopup extends Window implements Button.ClickListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8626828010142708237L;

	private static final Logger LOG = LoggerFactory.getLogger(ModifyPasswordPopup.class);

    private static final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    private TextField loginVerificationField = new TextField("Saisissez votre login");
    private PasswordField oldPasswordField = new PasswordField("Saisissez votre ancien mot de passe");
    private PasswordField newPasswordField = new PasswordField("Saisissez votre nouveau mot de passe");
    private PasswordField newPasswordVerificationField = new PasswordField("Saisissez à nouveau votre nouveau mot de passe");
    private Button modifyPasswordButton = new Button("Modifier", this);

    // FIXME
    //@Inject
    private AccountDAO accountDAO = new AccountDAO();

    public ModifyPasswordPopup() {
        super("Modification de mot de passe");

        VerticalLayout content = new VerticalLayout();
        setContent(content);

        loginVerificationField.focus();
        modifyPasswordButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        content.addComponent(loginVerificationField);
        content.addComponent(oldPasswordField);
        content.addComponent(newPasswordField);
        content.addComponent(newPasswordVerificationField);
        content.addComponent(modifyPasswordButton);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        if (!newPasswordField.getValue().equals(newPasswordVerificationField.getValue())) {
            Notification.show("Les mots de passe ne correspondent pas", Notification.Type.WARNING_MESSAGE);
            return;
        } else if ("".equals(newPasswordField.getValue()) || newPasswordField.getValue() == null) {
            Notification.show("Vous n'avez pas saisi de mot de passe", Notification.Type.WARNING_MESSAGE);
            return;
        } else {
            modifyPasswordButton.setComponentError(null);
        }

        String username = loginVerificationField.getValue();
        String oldPassword = oldPasswordField.getValue();
        String newPassword = newPasswordField.getValue();

        // TODO Séparer la partie DB & Security
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Account item = accountDAO.getAccountByLoginAndEmail(username, oldPassword);

            item.setPassword(PasswordHasher.hashPassword(newPassword));
            entityManager.persist(item);
            transaction.commit();

            EmailSender.sendEmailWithCredentials(item.getEmail(), item.getLogin(), newPassword);
        } catch (SQLException | MessagingException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }

        close();
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

}
