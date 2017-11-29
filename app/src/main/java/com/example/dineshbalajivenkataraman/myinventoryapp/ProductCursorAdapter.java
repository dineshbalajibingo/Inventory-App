package com.example.dineshbalajivenkataraman.myinventoryapp;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dineshbalajivenkataraman.myinventoryapp.data.ProductContract.ProductEntry;
public class ProductCursorAdapter extends CursorAdapter {
    ImageView mPhotoImageView;
    Uri productImageUri;
    public ProductCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        final String productName = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME));
        productNameTextView.setText(productName);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        final int productQuantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        quantityTextView.setText(String.valueOf(productQuantity));
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        String price = Float.toString(cursor.getFloat(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE)));
        priceTextView.setText(price);
        mPhotoImageView = (ImageView) view.findViewById(R.id.list_item_photo);
        String productImageString = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PHOTO_URI));
        if (productImageString.equals("no image")) {
            mPhotoImageView.setImageResource(R.drawable.ic_add_a_photo_white);
        } else {
            productImageUri = Uri.parse(productImageString);
            mPhotoImageView.setImageURI(productImageUri);
        }
        Button saleButton = (Button) view.findViewById(R.id.sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ContentResolver resolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                if (productQuantity > 0) {
                    Uri CurrentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    int currentAvailableQuantity = productQuantity;
                    currentAvailableQuantity -= 1;
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, currentAvailableQuantity);
                    resolver.update(
                            CurrentProductUri,
                            values,
                            null,
                            null
                    );
                    context.getContentResolver().notifyChange(CurrentProductUri, null);
                } else {
                    Toast.makeText(v.getContext(), R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
