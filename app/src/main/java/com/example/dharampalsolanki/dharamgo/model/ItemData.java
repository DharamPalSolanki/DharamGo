package com.example.dharampalsolanki.dharamgo.model;

/**
 * Created by DharampalSolanki on 3/30/2017.
 */

public class ItemData {
    String name;
    String endDate;
    String icon;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    String count;
public ItemData(){


}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
