package iftm.pdm.velo.ui.list_adapters;

import java.util.Objects;

class ItemList<T> {

    T item;
    boolean isExpanded;
    boolean isSelected;

    ItemList(T item) {
        this.item = item;
        this.isExpanded = false;
        this.isSelected = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemList<?> itemList = (ItemList<?>) o;
        return Objects.equals(item, itemList.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

}
