package com.bierocratie.ui.view.accounting;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.model.accounting.Invoice;
import com.bierocratie.model.accounting.Tva;
import com.bierocratie.ui.component.AbstractMenuBar;
import com.bierocratie.ui.view.AbstractBasicModelView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

@SuppressWarnings("serial")
public class InvoiceView extends AbstractBasicModelView<Invoice> {

    private Category category;

    private Field dateField;
    private Field monthField;
    private Field supplierField;
    private Field amountField;
    private ComboBox categoryComboBox;
    private ComboBox tvaComboBox;

    private ComboBox categoryForMultiInvoicesComboBox;

    private BudgetYear currentBudgetYear;

    private InvoiceByCategoryView invoiceByCategoryView;

    public InvoiceView(Category category, BudgetYear currentBudgetYear, InvoiceByCategoryView invoiceByCategoryView) {
        this.category = category;
        this.currentBudgetYear = currentBudgetYear;
        this.invoiceByCategoryView = invoiceByCategoryView;

        table.sort(new Object[]{"date", "month"}, new boolean[]{false, false});
        table.setMultiSelect(true);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);

        enter(null);
    }

    @Override
    protected Class<Invoice> getClazz() {
        return Invoice.class;
    }

    @Override
    protected String getTableName() {
        return null;
    }

    @Override
    protected AbstractMenuBar getMenuBar() {
        return null;
    }

    @Override
    protected void buildAndBind() {
        dateField = binder.buildAndBind("Date", "date");
        form.addComponent(dateField);
        monthField = binder.buildAndBind("Mois", "month");
        form.addComponent(monthField);
        supplierField = binder.buildAndBind("Fournisseur", "supplier");
        form.addComponent(supplierField);
        amountField = binder.buildAndBind("Montant", "amount");
        form.addComponent(amountField);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Category> categories = JPAContainerFactory.make(Category.class, "dashboard");
        categoryComboBox = new ComboBox("Catégorie");
        categoryComboBox.setContainerDataSource(categories);
        categoryComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        categoryComboBox.setItemCaptionPropertyId("name");
        categoryComboBox.setConverter(new SingleSelectConverter<Category>(categoryComboBox));
        categoryComboBox.setFilteringMode(FilteringMode.CONTAINS);
        categoryComboBox.setImmediate(true);
        categoryComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    long categoryId = (long) event.getProperty().getValue();
                    tvaComboBox.setValue(categories.getItem(categoryId).getEntity().getDefaultTva().getId());
                }
            }
        });
        binder.bind(categoryComboBox, "category");
        form.addComponent(categoryComboBox);

        // FIXME pb accès persistenceUnitName
        //final JPAContainer<Supplier> suppliers = JPAContainerFactory.make(Supplier.class, persistenceUnitName);
        final JPAContainer<Tva> tvas = JPAContainerFactory.make(Tva.class, "dashboard");
        tvaComboBox = new ComboBox("TVA");
        tvaComboBox.setContainerDataSource(tvas);
        tvaComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tvaComboBox.setItemCaptionPropertyId("rate2String");
        tvaComboBox.setConverter(new SingleSelectConverter<Tva>(tvaComboBox));
        tvaComboBox.setTextInputAllowed(false);
        tvaComboBox.setImmediate(true);
        binder.bind(tvaComboBox, "tva");
        form.addComponent(tvaComboBox);
    }

    private JPAContainer<Category> categories;

    @Override
    protected void createMultiSelectForm() {
        categories = JPAContainerFactory.make(Category.class, "dashboard");
        categoryForMultiInvoicesComboBox = new ComboBox("Catégorie");
        categoryForMultiInvoicesComboBox.setContainerDataSource(categories);
        categoryForMultiInvoicesComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        categoryForMultiInvoicesComboBox.setItemCaptionPropertyId("name");
        categoryForMultiInvoicesComboBox.setConverter(new SingleSelectConverter<Category>(categoryForMultiInvoicesComboBox));
        categoryForMultiInvoicesComboBox.setFilteringMode(FilteringMode.CONTAINS);
        multiSelectForm.addComponent(categoryForMultiInvoicesComboBox);
    }

    private Category newCategory;

    @Override
    protected void getMultiFormValues() {
        if (categoryForMultiInvoicesComboBox.getValue() != null) {
            long categoryId = (long) categoryForMultiInvoicesComboBox.getValue();
            newCategory = categories.getItem(categoryId).getEntity();
        }
    }

    @Override
    protected void setItemValues(Invoice item) {
        if (newCategory != null) {
            item.setCategory(newCategory);
            if (item.getTva() == null) {
                item.setTva(newCategory.getDefaultTva());
            }
        }
    }

    @Override
    protected void postSaveItemsProcessing() {
        if (newCategory != null) {
            // FIXME
            invoiceByCategoryView.getInvoiceViewByCategoryMap().get(newCategory.getName()).getEntities().refresh();
        }
        newCategory = null;
    }

    @Override
    protected BeanItem<Invoice> createCopiedBeanItem(Invoice item) {
        Invoice copy = new Invoice();

        dateField.setEnabled(true);
        monthField.setEnabled(true);
        supplierField.setEnabled(true);
        amountField.setEnabled(true);

        copy.setDate(item.getDate());
        copy.setMonth(item.getMonth());
        copy.setSupplier(item.getSupplier());
        copy.setAmount(item.getAmount());
        copy.setCategory(item.getCategory());
        copy.setTva(item.getTva());
        copy.setImported(false);

        return new BeanItem<>(copy);
    }

    @Override
    protected void updateForm(Invoice item) {
        dateField.setEnabled(!item.isImported());
        monthField.setEnabled(!item.isImported());
        supplierField.setEnabled(!item.isImported());
        amountField.setEnabled(!item.isImported());
        copyButton.setVisible(!item.isImported());
    }

    @Override
    protected boolean isUpdateAuthorized(Invoice item) {
        return true;
    }

    @Override
    protected BeanItem<Invoice> createNewBeanItem() {
        Invoice invoice = new Invoice();

        dateField.setEnabled(true);
        monthField.setEnabled(true);
        supplierField.setEnabled(true);
        amountField.setEnabled(true);

        invoice.setCategory(category);
        invoice.setTva(category.getDefaultTva());

        return new BeanItem<>(invoice);
    }

    @Override
    protected void setTableColumns() {
        table.setVisibleColumns(new String[]{"month", "date", "supplier", "amount", "category", "tva"});
        table.setColumnHeader("date", "Date");
        table.setColumnHeader("month", "Mois");
        table.setColumnHeader("supplier", "Fournisseur");
        table.setColumnHeader("amount", "Montant");
        table.setColumnHeader("category", "Catégorie");
        table.setColumnHeader("tva", "TVA");
    }

    @Override
    protected void addDataToItem(Invoice item) throws Exception {
    }

    @Override
    protected void preSaveItemProcessing(Invoice item) {
    }

    @Override
    protected void postSaveItemProcessing(Invoice item) {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        entities.addContainerFilter(new Compare.GreaterOrEqual("d", currentBudgetYear.getFirstMonth() + "00"));
        entities.addContainerFilter(new Compare.Equal("category", category));
    }

}
