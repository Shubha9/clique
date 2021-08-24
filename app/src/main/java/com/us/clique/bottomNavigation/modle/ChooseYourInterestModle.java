package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChooseYourInterestModle {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("category")
        @Expose
        private List<Category> category = null;

        public List<Category> getCategory() {
            return category;
        }

        public void setCategory(List<Category> category) {
            this.category = category;
        }

        public class Category {

            @SerializedName("category_name")
            @Expose
            private String categoryName;
            @SerializedName("category_id")
            @Expose
            private String categoryId;
            @SerializedName("category_image")
            @Expose
            private String categoryImage;
            @SerializedName("category_image_path")
            @Expose
            private String categoryImagePath;
            @SerializedName("category_image_pic")
            @Expose
            private String categoryImagePic;
            @SerializedName("sub_cat")
            @Expose
            private List<SubCat> subCat = null;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryImage() {
                return categoryImage;
            }

            public void setCategoryImage(String categoryImage) {
                this.categoryImage = categoryImage;
            }

            public String getCategoryImagePath() {
                return categoryImagePath;
            }

            public void setCategoryImagePath(String categoryImagePath) {
                this.categoryImagePath = categoryImagePath;
            }

            public String getCategoryImagePic() {
                return categoryImagePic;
            }

            public void setCategoryImagePic(String categoryImagePic) {
                this.categoryImagePic = categoryImagePic;
            }

            public List<SubCat> getSubCat() {
                return subCat;
            }

            public void setSubCat(List<SubCat> subCat) {
                this.subCat = subCat;
            }

        }    }
        public class SubCat {

            @SerializedName("sk_sub_category_id")
            @Expose
            private String skSubCategoryId;
            @SerializedName("sub_category_name")
            @Expose
            private String subCategoryName;
            @SerializedName("sub_category_status")
            @Expose
            private String subCategoryStatus;
            @SerializedName("categoryId")
            @Expose
            private String categoryId;
            @SerializedName("categoryName")
            @Expose
            private String categoryName;

            public String getSkSubCategoryId() {
                return skSubCategoryId;
            }

            public void setSkSubCategoryId(String skSubCategoryId) {
                this.skSubCategoryId = skSubCategoryId;
            }

            public String getSubCategoryName() {
                return subCategoryName;
            }

            public void setSubCategoryName(String subCategoryName) {
                this.subCategoryName = subCategoryName;
            }

            public String getSubCategoryStatus() {
                return subCategoryStatus;
            }

            public void setSubCategoryStatus(String subCategoryStatus) {
                this.subCategoryStatus = subCategoryStatus;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }
        }
    }