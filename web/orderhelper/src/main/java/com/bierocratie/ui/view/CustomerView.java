package com.bierocratie.ui.view;

import com.bierocratie.email.EmailSender;
import com.bierocratie.model.Customer;
import com.bierocratie.model.security.Role;
import com.bierocratie.security.PasswordGenerator;
import com.bierocratie.security.PasswordHasher;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.OrderMenuBar;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import org.apache.shiro.SecurityUtils;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class CustomerView extends AbstractBasicModelView<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerView.class);

    @Override
    protected Class<Customer> getClazz() {
        return Customer.class;
    }

    @Override
    protected String getTableName() {
        return "Caves & Bars";
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        // FIXME @Inject
        return new OrderMenuBar();
    }

    @Override
    protected void createMultiSelectForm() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected BeanItem<Customer> createCopiedBeanItem(Customer item) {
        Customer copy = new Customer();
        copy.setName(item.getName());
        copy.setAddress(item.getAddress());
        copy.setTelephone(item.getTelephone());
        copy.setEmail(item.getEmail());
        copy.setReceiveDelivery(item.getReceiveDelivery());
        copy.setRole(item.getRole());
        copy.setLogin(item.getLogin() + "-copy");
        try {
            String password = PasswordGenerator.generatePassword();
            copy.setPassword(PasswordHasher.hashPassword(password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
        return new BeanItem<>(copy);
    }

    @Override
    protected void buildAndBind() {
        form.addComponent(binder.buildAndBind("Nom", "name"));
        form.addComponent(binder.buildAndBind("Login", "login"));
        form.addComponent(binder.buildAndBind("Adresse", "address"));
        form.addComponent(binder.buildAndBind("Email", "email"));
        form.addComponent(binder.buildAndBind("Téléphone", "telephone"));
        form.addComponent(binder.buildAndBind("Peut recevoir", "receiveDelivery"));
        OptionGroup roleField = binder.buildAndBind("Rôle", "role", OptionGroup.class);
        roleField.setVisible(SecurityUtils.getSubject().hasRole(Role.ADMIN.name()));
        form.addComponent(roleField);
    }

    @Override
    protected void updateForm(Customer item) {
    }

    @Override
    protected void addDataToItem(Customer item) throws Exception {
        String password = PasswordGenerator.generatePassword();
        form.setData(password);
        item.setPassword(PasswordHasher.hashPassword(password));
    }

    @Override
    protected void preSaveItemProcessing(Customer item) {
    }

    @Override
    protected void postSaveItemProcessing(Customer item) {
        if ((boolean) addButton.getData()) {
            try {
                EmailSender.sendEmailWithCredentials(item.getEmail(), item.getLogin(), (String) form.getData());
            } catch (MessagingException e) {
                Notification.show("Erreur dans l'envoi du mail", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void getMultiFormValues() {
    }

    @Override
    protected void setItemValues(Customer item) {
    }

    @Override
    protected void postSaveItemsProcessing() {
    }

    @Override
    protected BeanItem<Customer> createNewBeanItem() {
        return new BeanItem<>(new Customer());
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"name", "login", "address", "email", "telephone", "receiveDelivery", "role"});
        table.setColumnHeader("name", "Nom");
        table.setColumnHeader("login", "Login");
        table.setColumnHeader("address", "Adresse");
        table.setColumnHeader("email", "Email");
        table.setColumnHeader("telephone", "Téléphone");
        table.setColumnHeader("receiveDelivery", "Peut recevoir");
        table.setColumnHeader("role", "Rôle");
    }

    @Override
    protected boolean isUpdateAuthorized(Customer item) {
        return item.getLogin().equals(SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        if (!SecurityUtils.getSubject().hasRole(Role.ADMIN.name())) {
            addButton.setVisible(false);
        }
    }

}
