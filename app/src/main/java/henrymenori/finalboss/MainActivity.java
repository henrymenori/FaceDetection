package henrymenori.finalboss;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // attribute
    ImageView   imageTop;
    Bitmap      bmp, bmp1;
    int         height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageTop = (ImageView)findViewById(R.id.imageTop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // -------------------------
    //   Load Image Method
    // -------------------------

    public void loadImage(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                imageTop.setImageBitmap(BitmapFactory.decodeFile(imageString));

                // Get Image Attribute
                bmp = ((BitmapDrawable)imageTop.getDrawable()).getBitmap();
                bmp1 = bmp.copy(Bitmap.Config.ARGB_8888, true);
                height = bmp1.getHeight();
                width = bmp1.getWidth();
            }
        }
        catch(Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    // -------------------------
    //   Basic Method
    // -------------------------

    public int[] getPixelColor(Bitmap bm, int x, int y) {
        int clr = bm.getPixel(x, y);
        return new int[] {
                (clr & 0x00ff0000) >> 16,
                (clr & 0x0000ff00) >> 8,
                clr & 0x000000ff
        };
    }

    public int getPixelGrayscale(Bitmap bm, int x, int y) {
        return bm.getPixel(x, y) & 0x000000ff;
    }

    public void setPixelColor(Bitmap bm, int x, int y, int r, int g, int b) {
        int c = Color.argb(255, r, g, b);
        bm.setPixel(x, y, c);
    }

    public void setPixelGrayscale(Bitmap bm, int x, int y, int grayscale) {
        int c = Color.argb(255, grayscale, grayscale, grayscale);
        bm.setPixel(x, y, c);
    }

    public Bitmap imageCopy(Bitmap bm) {
        return bm.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void setImage(ImageView iv, Bitmap bm) {
        iv.setImageBitmap(bm);
    }


    // -------------------------
    //   Pre-process Method
    // -------------------------

    public void convertToGrayscale(Bitmap bm) {
        int[]   color;
        int     grayscale;

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                color       = getPixelColor(bm, i, j);
                grayscale   = (color[0] + color[1] + color[2]) / 3;

                setPixelGrayscale(bm, i, j, grayscale);
            }
        }
    }

    public void equalize(Bitmap bm) {
        int[]   color, lookup;
        int     base, total;

        // count color frequencies
        color = new int[256];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                color[getPixelGrayscale(bm, i, j)]++;
            }
        }

        // build lookup table
        base    = 0;
        total   = width * height;
        lookup  = new int[256];
        for(int i = 0; i < 256; i++) {
            base        = base + color[i];
            lookup[i]   = 255 * base / total;
        }

        // transform image
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                setPixelGrayscale(bm, i, j, lookup[getPixelGrayscale(bm, i, j)]);
            }
        }
    }
}
