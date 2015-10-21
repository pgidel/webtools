package com.bierocratie.ui.view;

import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.component.Table;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 27/04/14
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractBasicModelView<T> extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractBasicModelView.class);

    protected abstract String getTableName();

    protected abstract void setTableColumns();

    protected abstract Class<T> getClazz();

    protected abstract void buildAndBind();

    protected abstract BeanItem<T> createNewBeanItem();

    protected abstract BeanItem<T> createCopiedBeanItem(T item);

    protected abstract void updateForm(T item);

    protected abstract boolean isUpdateAuthorized(T item);

    protected abstract AbstractMenuBar getMenuBar();

    protected abstract void createMultiSelectForm();

    protected BeanFieldGroup<T> binder;
    protected Button deleteButton;
    protected Button cancelButton;
    protected Button addButton;
    protected Button saveButton;
    protected Button copyButton;
    protected FormLayout form;
    protected FormLayout multiSelectForm;
    protected Table table;

    // FIXME Injecter
    protected String persistenceUnitName = "dashboard";
    protected JPAContainer<T> entities;

    private List<Object> selectedItemIds = new ArrayList<>();

    public AbstractBasicModelView() {
        AbstractMenuBar menuBar = getMenuBar();
        if (menuBar != null) {
            menuBar.setSizeFull();
            addComponent(menuBar);
        }

        entities = JPAContainerFactory.make(getClazz(), persistenceUnitName);

        table = new Table(getTableName(), entities);
        table.setImmediate(true);
        table.setSelectable(true);
        table.setBuffered(false);

        setTableColumns();

        List<Object> sortableColumns = new ArrayList<>();
        for (Object propertyId : table.getVisibleColumns()) {
            if (entities.getSortableContainerPropertyIds().contains(propertyId)) {
                sortableColumns.add(propertyId);
            }
        }

        boolean[] booleans = new boolean[sortableColumns.size()];
        Arrays.fill(booleans, true);
        table.sort(sortableColumns.toArray(), booleans);

        form = new FormLayout();
        form.setVisible(false);

        binder = new BeanFieldGroup<T>(getClazz());

        buildAndBind();

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
        copyButton = new Button("Dupliquer", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                prepareFormToAddCopiedItem();
            }
        });

        form.addComponent(saveButton);
        form.addComponent(cancelButton);
        form.addComponent(deleteButton);
        form.addComponent(copyButton);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedItemIds = new ArrayList<>();
                if (table.isMultiSelect()) {
                    Collection<Object> itemIds = (Collection<Object>) event.getProperty().getValue();
                    selectedItemIds.addAll(itemIds);
                    //Collection<Object> itemIds = (Collection<Object>) table.getValue();
                    List<EntityItem<T>> entityItems = new ArrayList<>();
                    for (Object itemId : itemIds) {
                        entityItems.add(entities.getItem(itemId));
                    }
                    if (entityItems.size() == 1) {
                        processValueChange(entityItems.get(0));
                    } else {
                        processValueChange(entityItems);
                    }
                } else {
                    Object itemId = event.getProperty().getValue();
                    selectedItemIds.add(itemId);
                    //Object itemId = table.getValue();
                    EntityItem<T> entityItem = entities.getItem(itemId);
                    processValueChange(entityItem);
                }
            }
        });

        addComponent(addButton);

        multiSelectForm = new FormLayout();
        createMultiSelectForm();
        multiSelectForm.setVisible(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(table);
        horizontalLayout.addComponent(form);
        horizontalLayout.addComponent(multiSelectForm);
        addComponent(horizontalLayout);
    }

    protected abstract void addDataToItem(T item) throws Exception;

    protected void deleteItem() {
        form.setVisible(false);
        table.setEnabled(true);

        binder.discard();

        /*if (table.isMultiSelect()) {
            for (Object itemId : (Collection<Object>)table.getValue()) {
                entities.removeItem(itemId);
                entities.commit();
            }
        } else {
            entities.removeItem(table.getValue());
            entities.commit();
        }*/
        for (Object itemId : selectedItemIds) {
            entities.removeItem(itemId);
        }
        entities.commit();

        table.setValue(null);
        Notification.show("Supprimé");
    }

    protected void cancel() {
        form.setVisible(false);
        table.setEnabled(true);

        table.setValue(null);

        binder.discard();

        Notification.show("Annulé");
    }

    protected void saveItem() {
        T item = binder.getItemDataSource().getBean();

        try {
            form.setVisible(false);
            table.setEnabled(true);

            binder.commit();

            preSaveProcessing(item);

            if (table.getValue() == null) {
                try {
                    addDataToItem(item);
                    entities.addEntity(item);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            } else {
                try {
                    entities.addEntity(item);
                } catch (PersistenceException e) {
                    LOG.error(e.getMessage(), e);
                    Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }

            entities.commit();
            table.refreshRowCache();

            postSaveProcessing(item);

            table.setValue(null);

            Notification.show("Sauvegardé");
        } catch (FieldGroup.CommitException e) {
            LOG.error(e.getMessage(), e);
            Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    protected void prepareFormToAddCopiedItem() {
        copyButton.setData(true);

        binder.discard();

        // TODO Améliorer l'instanciation en utilisant une Factory
        T item = binder.getItemDataSource().getBean();
        binder.setItemDataSource(createCopiedBeanItem(item));

        deleteButton.setEnabled(false);
        copyButton.setEnabled(false);

        focusOnFirstComponent();

        form.setVisible(true);
        table.setEnabled(false);
    }

    protected void processValueChange(List<EntityItem<T>> entityItems) {
        form.setVisible(false);
        if (!entityItems.isEmpty()) {
            multiSelectForm.setVisible(true);
        }
    }

    protected void processValueChange(EntityItem<T> entityItem) {
        multiSelectForm.setVisible(false);

        if (entityItem != null) {
            addButton.setData(false);

            T item = entityItem.getEntity();
            binder.setItemDataSource(new BeanItem<T>(item));
            form.setVisible(true);

            updateForm(item);

            deleteButton.setEnabled(true);
            copyButton.setEnabled(true);

            focusOnFirstComponent();
        } else {
            updateForm(null);
        }
    }

    private void focusOnFirstComponent() {
        Component firstComponent = form.getComponent(0);
        if (firstComponent != null && firstComponent instanceof Field) {
            ((Field) firstComponent).focus();
        }
    }

    protected abstract void preSaveProcessing(T item);
    protected abstract void postSaveProcessing(T item);

    protected void prepareFormToAddNewItem() {
        addButton.setData(true);

        binder.discard();

        // TODO Améliorer l'instanciation en utilisant une Factory
        binder.setItemDataSource(createNewBeanItem());

        deleteButton.setEnabled(false);
        copyButton.setEnabled(false);

        focusOnFirstComponent();

        form.setVisible(true);
        table.setValue(null);
        table.setEnabled(false);
    }

    public JPAContainer<T> getEntities() {
        return entities;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

}
