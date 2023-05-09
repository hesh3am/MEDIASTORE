package com.example.hesham.mediastore.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hesham on 16-Sep-18.
 */
public class ProductContract {
    public static final String PRODUCT_AUTHORITY_BY = "com.example.hesham.mediastore";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PRODUCT_AUTHORITY_BY);
    public static final String PATH_PRODUCT_TABLE = "product";

    public ProductContract() {
    }

    public final static class ProductEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_TABLE);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PRODUCT_AUTHORITY_BY + "/" + PATH_PRODUCT_TABLE;
        public static final String MEDIASTORE_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PRODUCT_AUTHORITY_BY + "/" + PATH_PRODUCT_TABLE;
        public final static String TABLE_NAME = "product";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "product_name";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_PHONE = "supplier_phone_number";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_PRICE = "price";
        public final static int UNKNOWN = 0;
        public final static int AMAZON = 1;
        public final static int Jumia = 2;
        public final static int Souq = 3;

        public static boolean ValidName(int supplierName) {
            if (supplierName == UNKNOWN || supplierName == AMAZON || supplierName == Jumia || supplierName == Souq) {
                return true;
            }
            return false;
        }
    }
}