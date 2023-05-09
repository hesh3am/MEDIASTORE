package com.example.hesham.mediastore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.hesham.mediastore.data.ProductContract.ProductEntry;
/**
 * Created by Hesham on 17-Sep-18.
 */
public class ProductProvider extends ContentProvider {
    private static final int MEDIASTORE_PROUDCTS_LIMIT = 100;
    private static final int MEDIA_STORE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ProductContract.PRODUCT_AUTHORITY_BY, ProductContract.PATH_PRODUCT_TABLE, MEDIASTORE_PROUDCTS_LIMIT);
        sUriMatcher.addURI(ProductContract.PRODUCT_AUTHORITY_BY, ProductContract.PATH_PRODUCT_TABLE + "/#", MEDIA_STORE_ID);
    }
    private ProductDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper((getContext()));
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIASTORE_PROUDCTS_LIMIT:
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEDIA_STORE_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIASTORE_PROUDCTS_LIMIT:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIASTORE_PROUDCTS_LIMIT:
                return ProductEntry.CONTENT_LIST_TYPE;
            case MEDIA_STORE_ID:
                return ProductEntry.MEDIASTORE_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIASTORE_PROUDCTS_LIMIT:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case MEDIA_STORE_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private Uri insertProduct(Uri uri, ContentValues values) {
        String nameProduct = values.getAsString(ProductEntry.COLUMN_NAME);
        if (nameProduct == null) {
            throw new IllegalArgumentException("name requires");
        }
        Integer priceProduct = values.getAsInteger(ProductEntry.COLUMN_PRICE);
        if (priceProduct != null && priceProduct < 0) {
            throw new IllegalArgumentException(" price requires ");
        }
        Integer quantityProduct = values.getAsInteger(ProductEntry.COLUMN_QUANTITY);
        if (quantityProduct != null && quantityProduct < 0) {
            throw new IllegalArgumentException(" quantity requires ");
        }
        Integer supplierName = values.getAsInteger(ProductEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null || !ProductEntry.ValidName(supplierName)) {
            throw new IllegalArgumentException(" requires gender");
        }
        Integer supplierPhone = values.getAsInteger(ProductEntry.COLUMN_PHONE);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException(" Phone requires ");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("message:", "Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIASTORE_PROUDCTS_LIMIT:
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEDIA_STORE_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProductEntry.COLUMN_NAME)) {
            String nameProduct = values.getAsString(ProductEntry.COLUMN_NAME);
            if (nameProduct == null) {
                throw new IllegalArgumentException(" name requires");
            }
        }
        if (values.containsKey(ProductEntry.COLUMN_PRICE)) {
            Integer priceProduct = values.getAsInteger(ProductEntry.COLUMN_PRICE);
            if (priceProduct != null && priceProduct < 0) {
                throw new
                        IllegalArgumentException(" price requires ");
            }
        }
        if (values.containsKey(ProductEntry.COLUMN_QUANTITY)) {
            Integer quantityProduct = values.getAsInteger(ProductEntry.COLUMN_QUANTITY);
            if (quantityProduct != null && quantityProduct < 0) {
                throw new
                        IllegalArgumentException(" quantity requires ");
            }
        }
        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_NAME)) {
            Integer supplierName = values.getAsInteger(ProductEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null || !ProductEntry.ValidName(supplierName)) {
                throw new IllegalArgumentException(" Name requires ");
            }
        }
        if (values.containsKey(ProductEntry.COLUMN_PHONE)) {
            Integer supplierPhone = values.getAsInteger(ProductEntry.COLUMN_PHONE);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException(" Phone requires ");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}