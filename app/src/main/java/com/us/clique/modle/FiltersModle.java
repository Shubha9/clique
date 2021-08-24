package com.us.clique.modle;

public class FiltersModle {
    String name;
    private boolean isSelected;


    public FiltersModle(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
