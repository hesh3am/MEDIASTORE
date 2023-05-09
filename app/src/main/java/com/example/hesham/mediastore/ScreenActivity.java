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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesham.mediastore.data.ProductContract.ProductEntry;
/**
 * Created by Hesham on 18-Sep-18.
 */
public class ScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PRODUCT_LOADER = 0;
    private Uri ProductUri;
    private TextView ProductNameText;
    private TextView ProductPriceText;
    private TextView ProductQuantityText;
    private TextView ProductSupplieName;
    private TextView ProductPhoneText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ProductNameText = findViewById(R.id.product_name_view_text);
        ProductPriceText = findViewById(R.id.product_price_view_text);
        ProductQuantityText = findViewById(R.id.product_quantity_view_text);
        ProductSupplieName = findViewById(R.id.product_supplier_name_view_text);
        ProductPhoneText = findViewById(R.id.product_supplier_phone_number_view_text);
        Intent intent = getIntent();
        ProductUri = intent.getData();
        if (ProductUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            final int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
            int priceIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int quantityIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int PhoneIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PHONE);
            String currentName = cursor.getString(nameIndex);
            final int currentPrice = cursor.getInt(priceIndex);
            final int currentQuantity = cursor.getInt(quantityIndex);
            int currentSupplierName = cursor.getInt(supplierNameIndex);
            final int currentSupplierPhone = cursor.getInt(PhoneIndex);
            ProductNameText.setText(currentName);
            ProductPriceText.setText(Integer.toString(currentPrice));
            ProductQuantityText.setText(Integer.toString(currentQuantity));
            ProductPhoneText.setText(Integer.toString(currentSupplierPhone));
            switch (currentSupplierName) {
                case ProductEntry.AMAZON:
                    ProductSupplieName.setText(getText(R.string.amazon));
                    break;
                case ProductEntry.Jumia:
                    ProductSupplieName.setText(getText(R.string.Jumia));
                    break;
                case ProductEntry.Souq:
                    ProductSupplieName.setText(getText(R.string.Souq));
                    break;
                default:
                    ProductSupplieName.setText(getText(R.string.unknown));
                    break;
            }
            Button productDecreaseButton = findViewById(R.id.decrease);
            productDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(idColumnIndex, currentQuantity);
                }
            });
            Button productDeleteButton = findViewById(R.id.delete);
            productDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteConfirmation();
                }
            });
            Button productIncreaseButton = findViewById(R.id.increase);
            productIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(idColumnIndex, currentQuantity);
                }
            });
            Button phoneButton = findViewById(R.id.call);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(currentSupplierPhone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });
        }
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
                ProductUri,
                projection,
                null,
                null,
                null);
    }
    public void decreaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_change), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.quantity_finish), Toast.LENGTH_SHORT).show();
        }
    }
    public void increaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.quantity_change), Toast.LENGTH_SHORT).show();
        }
    }
    private void updateProduct(int productQuantity) {
        if (ProductUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_QUANTITY, productQuantity);
        if (ProductUri == null) {
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_Failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_Successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowEffect = getContentResolver().update(ProductUri, values, null, null);
            if (rowEffect == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_Successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void deleteProduct() {
        if (ProductUri != null) {
            int rowsDeleted = getContentResolver().delete(ProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    private void DeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.deleteproduct, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
    public void onLoaderReset(Loader<Cursor> loader) {}
}