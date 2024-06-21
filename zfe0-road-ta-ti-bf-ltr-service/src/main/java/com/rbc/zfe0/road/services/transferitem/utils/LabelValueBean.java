package com.rbc.zfe0.road.services.transferitem.utils;

/**
 * Generic label/value bean for use in options.
 *
 * @author Tim Tracy
 */
public class LabelValueBean implements Comparable {
    String label = null;
    String value = null;

    public static final LabelValueBean SELECT = new LabelValueBean("Select", "");
    public static final LabelValueBean NA = new LabelValueBean("NA", "NA");
    public static final LabelValueBean LOOKUP = new LabelValueBean("Lookup", "_LKP");

    // Allow parameterless construction
    public LabelValueBean() {
    }

    public LabelValueBean(String labelIn, String valueIn) {
        this.label = labelIn;
        this.value = valueIn;
    }

    public LabelValueBean(String labelAndValueIn) {
        this.label = labelAndValueIn;
        this.value = labelAndValueIn;
    }


    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object objIn) {

        LabelValueBean compareIn = (LabelValueBean) objIn;

        return this.getLabel().compareToIgnoreCase(compareIn.getLabel());

    }
}
