package com.example.hesham.mediastore;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.hesham.mediastore.data.ProductContract.ProductEntry;

/**
 * Created by Hesham on 17-Sep-18.
 */

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        TextView productNameTextView = view.findViewById(R.id.name_product_text_view);
        TextView productPriceTextView = view.findViewById(R.id.product_price_text_view);
        TextView productQuantityTextView = view.findViewById(R.id.product_quantity_text_view);
        Button productSaleButton = view.findViewById(R.id.sale_button);
        final int columnIdIndex = cursor.getColumnIndex(ProductEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);
        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaStoreActivity Activity = (MediaStoreActivity) context;
                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });
        productNameTextView.setText(productID + " ) " + productName);
        productPriceTextView.setText(context.getString(R.string.price) + " : " + productPrice + "  " + context.getString(R.string.price));
        productQuantityTextView.setText(context.getString(R.string.quantity) + " : " + productQuantity);
        Button productEditButton = view.findViewById(R.id.edit_button);
        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AdditionActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Long.parseLong(productID));
                intent.setData(currentProdcuttUri);
                context.startActivity(intent);
            }
        });
    }
}