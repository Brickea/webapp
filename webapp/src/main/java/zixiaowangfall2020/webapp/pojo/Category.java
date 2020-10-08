package zixiaowangfall2020.webapp.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class Category {
    @Null(message = "Id shall not be defined by request!")
    String categoryId;
    @NotNull(message = "category name shall not be blank!")
    String category;

    public Category() {
    }

    public Category(String category){
        this.category = category;
    }

    public Category(String categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
