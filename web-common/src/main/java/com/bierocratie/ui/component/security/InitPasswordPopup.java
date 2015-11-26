package com.bierocratie.ui.component.security;

import com.bierocratie.db.persistence.PersistenceManager;
import com.bierocratie.db.security.AccountDAO;
import com.bierocratie.email.EmailSender;
import com.bierocratie.model.security.Account;
import com.bierocratie.security.PasswordGenerator;
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
public class InitPasswordPopup extends Window implements Button.ClickListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4015721148541162682L;

	private static final Logger LOG = LoggerFactory.getLogger(InitPasswordPopup.class);

    private static final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    private TextField loginVerificationField = new TextField("Saisissez votre login");
    private TextField emailVerificationField = new TextField("Saisissez votre adresse email");
    private Button reinitializePasswordButton = new Button("Réinitialiser", this);

    // FIXME
    //@Inject
    private AccountDAO accountDAO = new AccountDAO();

    public InitPasswordPopup() {
        super("Réinitialisation de mot de passe");

        VerticalLayout content = new VerticalLayout();
        setContent(content);

        loginVerificationField.focus();
        reinitializePasswordButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        content.addComponent(loginVerificationField);
        content.addComponent(emailVerificationField);
        content.addComponent(reinitializePasswordButton);
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        String username = loginVerificationField.getValue();
        String email = emailVerificationField.getValue();

        // TODO Séparer la partie DB & Security
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Account item = accountDAO.getAccountByLoginAndEmail(username, email);

            String password = PasswordGenerator.generatePassword();
            item.setPassword(PasswordHasher.hashPassword(password));
            entityManager.persist(item);
            transaction.commit();

            EmailSender.sendEmailWithCredentials(item.getEmail(), item.getLogin(), password);
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
