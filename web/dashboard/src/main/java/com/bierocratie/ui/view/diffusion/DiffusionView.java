package com.bierocratie.ui.view.diffusion;

import com.bierocratie.bean.DiffusionBean;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
public class DiffusionView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(DiffusionView.class);

    public DiffusionView() {
        DashboardMenuBar menuBar = new DashboardMenuBar();
        addComponent(menuBar);
    }

    /*@Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    } */

    protected BeanFieldGroup<DiffusionBean> binder;
    protected Button deleteButton;
    protected Button cancelButton;
    protected Button addButton;
    protected Button saveButton;
    protected FormLayout form;
    protected Table table;

    private JPAContainer<DiffusionBean> entities;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        table = new Table("Diffusions", entities);
        table.setImmediate(true);
        table.setSelectable(true);
        table.setVisibleColumns(new String[]{"message", "date"});
        table.setColumnHeader("message", "Message");
        table.setColumnHeader("date", "Date");

        form = new FormLayout();
        form.setVisible(false);

        binder = new BeanFieldGroup<DiffusionBean>(DiffusionBean.class);
        form.addComponent(binder.buildAndBind("Message", "message"));

        deleteButton = new Button("Supprimer", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                deleteItem();
            }
        });
        cancelButton = new Button("Annuler", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cancel();
            }
        });
        addButton = new Button("Ajouter", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                prepareFormToAddNewItem();
            }
        });
        saveButton = new Button("Sauvegarder", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveItem();
            }
        });

        form.addComponent(saveButton);
        form.addComponent(cancelButton);
        form.addComponent(deleteButton);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object itemId = event.getProperty().getValue();
                EntityItem<DiffusionBean> entityItem = entities.getItem(itemId);
                processValueChange(entityItem);
            }
        });

        addComponent(addButton);
        addComponent(table);
        addComponent(form);
    }

    private void prepareFormToAddNewItem() {
        addButton.setData(true);

        binder.discard();

        binder.setItemDataSource(new BeanItem<DiffusionBean>(new DiffusionBean()));

        deleteButton.setEnabled(false);

        form.setVisible(true);
        table.select(null);
        table.setEnabled(false);
    }

    private void processValueChange(EntityItem<DiffusionBean> entityItem) {
        if (entityItem != null) {
            addButton.setData(false);

            DiffusionBean item = entityItem.getEntity();
            binder.setItemDataSource(new BeanItem<DiffusionBean>(item));
            form.setVisible(true);

            deleteButton.setEnabled(true);
        }
    }

    private void deleteItem() {
        form.setVisible(false);
        table.setEnabled(true);

        binder.discard();

        entities.removeItem(table.getValue());
        entities.commit();

        Notification.show("Supprimé");
    }

    private void cancel() {
        form.setVisible(false);
        table.setEnabled(true);

        binder.discard();

        Notification.show("Annulé");
    }

    private void saveItem() {
        DiffusionBean item = binder.getItemDataSource().getBean();
        try {
            form.setVisible(false);
            table.setEnabled(true);

            binder.commit();

            if (table.getValue() == null) {
                try {
                    entities.addEntity(item);
                } catch (Exception e) {
                    Notification.show("Erreur lors de l'ajout des données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    LOG.error(e.getMessage(), e);
                }
            } else {
                entities.addEntity(item);
            }

            entities.commit();
            table.refreshRowCache();

            Notification.show("Sauvegardé");
        } catch (FieldGroup.CommitException e) {
            Notification.show("Erreur lors de la validation du formulaire", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }
    }

}
