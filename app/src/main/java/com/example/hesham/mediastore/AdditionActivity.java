package com.example.hesham.mediastore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hesham.mediastore.data.ProductContract.ProductEntry;
/**
 * Created by Hesham on 16-Sep-18.
 */
public class AdditionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PRODUCT_MEDIA_LOADER = 0;
    private Uri mediaProductUri;
    private EditText mediaNameEditText;
    private EditText mediaPriceEditText;
    private EditText mediaQuantityEditText;
    private Spinner mediaSupplieName;
    private EditText mediaPhoneEditText;
    private int mediaSuppliesName = ProductEntry.UNKNOWN;
    private boolean mediaStoreChange = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mediaStoreChange = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);
        Intent intent = getIntent();
        mediaProductUri = intent.getData();
        if (mediaProductUri == null) {
            setTitle(getString(R.string.Add_product));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(PRODUCT_MEDIA_LOADER, null, this);
        }
        mediaNameEditText = findViewById(R.id.name_edit_text);
        mediaPriceEditText = findViewById(R.id.price_edit_text);
        mediaQuantityEditText = findViewById(R.id.quantity_edit_text);
        mediaSupplieName = findViewById(R.id.supplier_name_spinner);
        mediaPhoneEditText = findViewById(R.id.phone_edit_text);
        mediaNameEditText.setOnTouchListener(mTouchListener);
        mediaPriceEditText.setOnTouchListener(mTouchListener);
        mediaQuantityEditText.setOnTouchListener(mTouchListener);
        mediaSupplieName.setOnTouchListener(mTouchListener);
        mediaPhoneEditText.setOnTouchListener(mTouchListener);
        mediaSpinner();
    }
    private void mediaSpinner() {
        ArrayAdapter productSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_supplier_options, android.R.layout.simple_spinner_item);
        productSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mediaSupplieName.setAdapter(productSupplieNameSpinnerAdapter);
        mediaSupplieName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.amazon))) {
                        mediaSuppliesName = ProductEntry.AMAZON;
                    } else if (selection.equals(getString(R.string.Jumia))) {
                        mediaSuppliesName = ProductEntry.Jumia;
                    } else if (selection.equals(getString(R.string.Souq))) {
                        mediaSuppliesName = ProductEntry.Souq;
                    } else {
                        mediaSuppliesName = ProductEntry.UNKNOWN;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mediaSuppliesName = ProductEntry.UNKNOWN;
            }
        });
    }
    private void saveProductMediaStore() {
        String mediaNameString = mediaNameEditText.getText().toString().trim();
        String mediaPriceString = mediaPriceEditText.getText().toString().trim();
        String mediaQuantityString = mediaQuantityEditText.getText().toString().trim();
        String mediaPhoneString = mediaPhoneEditText.getText().toString().trim();
        if (mediaProductUri == null) {
            if (TextUtils.isEmpty(mediaNameString)) {
                Toast.makeText(this, getString(R.string.name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaPriceString)) {
                Toast.makeText(this, getString(R.string.price), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mediaSuppliesName == ProductEntry.UNKNOWN) {
                Toast.makeText(this, getString(R.string.suppliertybe), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaPhoneString)) {
                Toast.makeText(this, getString(R.string.phonenumber), Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME, mediaNameString);
            values.put(ProductEntry.COLUMN_PRICE, mediaPriceString);
            values.put(ProductEntry.COLUMN_QUANTITY, mediaQuantityString);
            values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mediaSuppliesName);
            values.put(ProductEntry.COLUMN_PHONE, mediaPhoneString);
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_Failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_Successful), Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            if (TextUtils.isEmpty(mediaNameString)) {
                Toast.makeText(this, getString(R.string.name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaPriceString)) {
                Toast.makeText(this, getString(R.string.price), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mediaSuppliesName == ProductEntry.UNKNOWN) {
                Toast.makeText(this, getString(R.string.suppliertybe), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mediaPhoneString)) {
                Toast.makeText(this, getString(R.string.phonenumber), Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME, mediaNameString);
            values.put(ProductEntry.COLUMN_PRICE, mediaPriceString);
            values.put(ProductEntry.COLUMN_QUANTITY, mediaQuantityString);
            values.put(ProductEntry.COLUMN_SUPPLIER_NAME, mediaSuppliesName);
            values.put(ProductEntry.COLUMN_PHONE, mediaPhoneString);
            int rowsAffected = getContentResolver().update(mediaProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveProductMediaStore();
                return true;
            case android.R.id.home:
                if (!mediaStoreChange) {
                    NavUtils.navigateUpFromSameTask(AdditionActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AdditionActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!mediaStoreChange) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_PHONE
        };
        return new CursorLoader(this,
                mediaProductUri,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mediaNameEditText.setText("");
        mediaPriceEditText.setText("");
        mediaQuantityEditText.setText("");
        mediaPhoneEditText.setText("");
        mediaSupplieName.setSelection(0);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.editiing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PHONE);
            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);
            mediaNameEditText.setText(currentName);
            mediaPriceEditText.setText(Integer.toString(currentPrice));
            mediaQuantityEditText.setText(Integer.toString(currentQuantity));
            mediaPhoneEditText.setText(Integer.toString(currentSupplierPhone));
            switch (currentSupplierName) {
                case ProductEntry.AMAZON:
                    mediaSupplieName.setSelection(1);
                    break;
                case ProductEntry.Jumia:
                    mediaSupplieName.setSelection(2);
                    break;
                case ProductEntry.Souq:
                    mediaSupplieName.setSelection(3);
                    break;
                default:
                    //UNKNOWN
                    mediaSupplieName.setSelection(0);
                    break;
            }
        }
    }
}