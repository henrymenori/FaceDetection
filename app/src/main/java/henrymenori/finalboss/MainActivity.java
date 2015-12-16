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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // attribute
    ImageView   imageTop;
    TextView    edgeDetectionOrder, edgeDetectionMethod, blurringMethod;
    Bitmap      bmp, bmp1;
    int         height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageTop            = (ImageView)findViewById(R.id.imageTop);
        blurringMethod      = (TextView)findViewById(R.id.txtBlurringMethod);
        edgeDetectionOrder  = (TextView)findViewById(R.id.txtEdgeDetectionOrder);
        edgeDetectionMethod = (TextView)findViewById(R.id.txtEdgeDetectionMethod);

        blurringMethod.setText("Median");

        edgeDetectionOrder.setText("Orde Nol");
        edgeDetectionMethod.setText("Homogen");
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
    //   Button Method
    // -------------------------

    public void convertToGrayscaleImage(View view) {
        convertToGrayscale(bmp1);
        setImage(imageTop, bmp1);
    }

    public void reverseImage(View view) {
        reverse(bmp1);
        setImage(imageTop, bmp1);
    }

    public void equalizeImage(View view) {
        equalize(bmp1);
        setImage(imageTop, bmp1);
    }

    public void thresholdingImage(View view) {
        thresholding(bmp1);
        setImage(imageTop, bmp1);
    }

    public void blurringImage(View view) {
        if(blurringMethod.getText().equals("Median")) {
            medianBlurring(bmp1);
        }
        else if(blurringMethod.getText().equals("Gauss Filter")) {
            gaussFilter(bmp1);
        }
        else {
            fourierTransform(bmp1);
        }
        setImage(imageTop, bmp1);
    }

    public void changeBlurringMethod(View view) {
        if(blurringMethod.getText().equals("Median")) {
            blurringMethod.setText("Gauss Filter");
        }
        else if(blurringMethod.getText().equals("Gauss Filter")) {
            blurringMethod.setText("Fourier Transform");
        }
        else {
            blurringMethod.setText("Median");
        }
    }

    public void edgeDetectionImage(View view) {
        if(edgeDetectionMethod.getText().equals("Homogen")) {
            zeroOrderHomogen(bmp1);
        }
        else if(edgeDetectionMethod.getText().equals("Difference")) {
            zeroOrderDifference(bmp1);
        }
        else if(edgeDetectionMethod.getText().equals("Sobel")) {
            firstOrderSobel(bmp1);
        }
        else if(edgeDetectionMethod.getText().equals("Robert")) {
            firstOrderRobert(bmp1);
        }
        else if(edgeDetectionMethod.getText().equals("Prewitt")) {
            secondOrderPrewitt(bmp1);
        }
        else if(edgeDetectionMethod.getText().equals("Kirsch")) {
            secondOrderKirsch(bmp1);
        }
        else {
            secondOrderRobinson(bmp1);
        }
        setImage(imageTop, bmp1);
    }

    public void changeEdgeDetectionOrder(View view) {
        if(edgeDetectionOrder.getText().equals("Orde Nol")) {
            edgeDetectionOrder.setText("Orde Satu");
            edgeDetectionMethod.setText("Sobel");
        }
        else if(edgeDetectionOrder.getText().equals("Orde Satu")) {
            edgeDetectionOrder.setText("Orde Dua");
            edgeDetectionMethod.setText("Prewitt");
        }
        else {
            edgeDetectionOrder.setText("Orde Nol");
            edgeDetectionMethod.setText("Homogen");
        }
    }

    public void changeEdgeDetectionMethod(View view) {
        if(edgeDetectionMethod.getText().equals("Homogen")) {
            edgeDetectionMethod.setText("Difference");
        }
        else if(edgeDetectionMethod.getText().equals("Difference")) {
            edgeDetectionMethod.setText("Homogen");
        }
        else if(edgeDetectionMethod.getText().equals("Sobel")) {
            edgeDetectionMethod.setText("Robert");
        }
        else if(edgeDetectionMethod.getText().equals("Robert")) {
            edgeDetectionMethod.setText("Sobel");
        }
        else if(edgeDetectionMethod.getText().equals("Prewitt")) {
            edgeDetectionMethod.setText("Kirsch");
        }
        else if(edgeDetectionMethod.getText().equals("Kirsch")) {
            edgeDetectionMethod.setText("Robinson");
        }
        else {
            edgeDetectionMethod.setText("Prewitt");
        }
    }

    public void faceDetectionImage(View view) {
        kMeans(bmp1);
        setImage(imageTop, bmp1);
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
    //   Basic Function
    // -------------------------

    public boolean isBorder(int x, int y) {
        return (x == 0 || x == width - 1 || y == 0 || y == height - 1);
    }

    public int[] getNeighbourPixelGrayscale(Bitmap bm, int x, int y) {
        int[]   value;

        value = new int[9];
        value[0] = getPixelGrayscale(bm, x    , y - 1);
        value[1] = getPixelGrayscale(bm, x + 1, y - 1);
        value[2] = getPixelGrayscale(bm, x + 1, y    );
        value[3] = getPixelGrayscale(bm, x + 1, y + 1);
        value[4] = getPixelGrayscale(bm, x    , y + 1);
        value[5] = getPixelGrayscale(bm, x - 1, y + 1);
        value[6] = getPixelGrayscale(bm, x - 1, y    );
        value[7] = getPixelGrayscale(bm, x - 1, y - 1);
        value[8] = getPixelGrayscale(bm, x, y);

        return value;
    }

    public double getRange(Integer[] p, Integer[] q) {
        return Math.sqrt(Math.pow(p[0] - q[0], 2) + Math.pow(p[1] - q[1], 2));
    }

    public Integer[] getSentroid(List<Integer[]> data) {
        int sum_x = 0;
        int sum_y = 0;

        for(int i = 0; i < data.size(); i++) {
            sum_x = sum_x + data.get(i)[0];
            sum_y = sum_y + data.get(i)[1];
        }

        return new Integer[]{sum_x / (data.size() - 1), sum_y / (data.size() - 1)};
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

    public void reverse(Bitmap bm) {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                setPixelGrayscale(bm, i, j, 255 - getPixelGrayscale(bm, i, j));
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

    public void thresholding(Bitmap bm) {
        int threshold = 254;

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(getPixelGrayscale(bm, i, j) < threshold) {
                    setPixelGrayscale(bm, i, j, 0);
                }
                else {
                    setPixelGrayscale(bm, i, j, 255);
                }
            }
        }
    }

    public void medianBlurring(Bitmap bm) {
        int[]   neighbour;
        Bitmap  temp;

        temp = imageCopy(bm);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);

                    Arrays.sort(neighbour);
                    setPixelGrayscale(bm, i, j, neighbour[4]);
                }
            }
        }
    }

    public void gaussFilter(Bitmap bm) {
        double[]    matrix = {0.061, 0.242, 0.383, 0.242, 0.061};
        double      value;
        Bitmap      temp;

        temp = imageCopy(bm);
        for(int i = 2; i < width - 2; i++) {
            for(int j = 2; j < height - 2; j++) {
                value = 0;
                for(int k = 0; k < 5; k++) {
                    for(int l = 0; l < 5; l++) {
                        value = value + matrix[k] * matrix[l] * getPixelGrayscale(temp, i + k - 2, j + l - 2);
                    }
                }
                setPixelGrayscale(bm, i, j, (int) value);
            }
        }
    }

    public void fourierTransform(Bitmap bm) {

    }


    // -------------------------
    //   Edge Detection Method
    // -------------------------

    public void zeroOrderHomogen(Bitmap bm) {
        int[]   neighbour;
        int     value, max;
        Bitmap  temp;

        temp = imageCopy(bm);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                max = 0;
                if (!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    value = getPixelGrayscale(temp, i, j);

                    for (int k = 0; k < 8; k++) {
                        if (max < Math.abs(value - neighbour[k])) {
                            max = Math.abs(value - neighbour[k]);
                        }
                    }
                }
                setPixelGrayscale(bm, i, j, max);
            }
        }
    }

    public void zeroOrderDifference(Bitmap bm) {
        int[]   neighbour;
        int     max;
        Bitmap  temp;

        temp = imageCopy(bm);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                max = 0;
                if (!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    for (int k = 0; k < 4; k++) {
                        if (max < Math.abs(neighbour[k] - neighbour[k + 4])) {
                            max = Math.abs(neighbour[k] - neighbour[k + 4]);
                        }
                    }
                }
                setPixelGrayscale(bm, i, j, max);
            }
        }
    }

    public void firstOrderSobel(Bitmap bm) {
        int[]   neighbour;
        int     value, valuex, valuey;
        Bitmap  temp;

        // sobel operator
        int[] gx = {0, 1, 2, 1, 0, -1, -2, -1};
        int[] gy = {-2, -1, 0, 1, 2, 1, 0, -1};

        temp = imageCopy(bm);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                value = 0;
                if(!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    valuex    = 0;
                    valuey    = 0;

                    for(int k = 0; k < 8; k++) {
                        valuex = valuex + gx[k] * neighbour[k];
                        valuey = valuey + gy[k] * neighbour[k];
                    }

                    value = (int) Math.sqrt(Math.pow(valuex, 2) + Math.pow(valuey, 2));

                    if(value > 255) {
                        value = 255;
                    }
                }
                setPixelGrayscale(bm, i, j, value);
            }
        }
    }

    public void firstOrderRobert(Bitmap bm) {
        int     value, valuex, valuey;
        Bitmap  temp;

        temp = imageCopy(bm);
        for(int i = 0; i < width - 1; i++) {
            for(int j = 0; j < height - 1; j++) {
                if(i == width - 1 || j == height - 1) {
                    value = 0;
                }
                else {
                    valuex = getPixelGrayscale(temp, i, j) - getPixelGrayscale(temp, i + 1, j + 1);
                    valuey = getPixelGrayscale(temp, i + 1, j) - getPixelGrayscale(temp, i, j + 1);
                    value = (int) Math.sqrt(Math.pow(valuex, 2) + Math.pow(valuey, 2));

                    if(value > 255) {
                        value = 255;
                    }
                }
                setPixelGrayscale(bm, i, j, value);
            }
        }
    }

    public void secondOrderPrewitt(Bitmap bm) {
        int[]   neighbour;
        int     value, valuex, valuey;
        Bitmap  temp;

        // prewitt operator
        int[] gx = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] gy = {-1, -1, 0, 1, 1, 1, 0, -1};

        temp = imageCopy(bm);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                value = 0;
                if(!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    valuex    = 0;
                    valuey    = 0;

                    for(int k = 0; k < 8; k++) {
                        valuex = valuex + gx[k] * neighbour[k];
                        valuey = valuey + gy[k] * neighbour[k];
                    }

                    value = (int) Math.sqrt(Math.pow(valuex, 2) + Math.pow(valuey, 2));

                    if(value > 255) {
                        value = 255;
                    }
                }
                setPixelGrayscale(bm, i, j, value);
            }
        }
    }

    public void secondOrderKirsch(Bitmap bm) {
        int[]   neighbour;
        int     value, max;
        Bitmap  temp;

        // kirsch operator
        int[] g = {5, 5, -3, -3, -3, -3, -3, 5};

        temp = imageCopy(bm);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                max = 0;
                if(!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    max       = 0;

                    for(int k = 0; k < 8; k++) {
                        value = 0;
                        for(int l = 0; l < 8; l++) {
                            value = value + neighbour[l] * g[(l + k) % 8];
                        }

                        if(value > max) {
                            max = value;
                        }
                    }

                    if(max > 255) {
                        max = 255;
                    }
                }
                setPixelGrayscale(bm, i, j, max);
            }
        }
    }

    public void secondOrderRobinson(Bitmap bm) {
        int[]   neighbour;
        int     value, max;
        Bitmap  temp;

        // robinson operator
        int[] g = {0, 1, 2, 1, 0, -1, -2, -1};

        temp = imageCopy(bm);
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                max = 0;
                if(!isBorder(i, j)) {
                    neighbour = getNeighbourPixelGrayscale(temp, i, j);
                    max       = 0;

                    for(int k = 0; k < 8; k++) {
                        value = 0;
                        for(int l = 0; l < 8; l++) {
                            value = value + neighbour[l] * g[(l + k) % 8];
                        }

                        if(value > max) {
                            max = value;
                        }
                    }

                    if(max > 255) {
                        max = 255;
                    }
                }
                setPixelGrayscale(bm, i, j, max);
            }
        }
    }


    // -------------------------
    //   Face Detection Method
    // -------------------------

    public void kMeans(Bitmap bm) {
        List<Integer[]>     data;
        List<Integer[]>[]   cluster;
        int                 k, idx;
        int[]               result;
        double              min, range, error;
        boolean             konvergen;
        Integer[]           sentroid;

        k = 4;

        // load all black pixel to data
        data = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(getPixelGrayscale(bm, i, j) == 0) {
                    Integer[] e = {i, j};
                    data.add(e);
                }
            }
        }

        // set seed for each cluster
        cluster = new List[k];
        for(int i = 0; i < k; i++) {
            cluster[i] = new ArrayList<>();
            cluster[i].add(data.get(data.size() * i / k));
        }

        cluster[0].add(new Integer[]{(width - 1) / 4, (height - 1) / 4});
        cluster[1].add(new Integer[]{(width - 1) / 4 * 3, (height - 1) / 4});
        cluster[2].add(new Integer[]{(width - 1) / 4 * 2, (height - 1) / 4 * 2});
        cluster[3].add(new Integer[]{(width - 1) / 4 * 2, (height - 1) / 4 * 3});

        // classify each data to cluster
        konvergen = false;
        result    = new int[k];

        while(!konvergen) {
            for(int i = 0; i < data.size(); i++) {
                min = -1.0;
                idx = 0;
                for(int j = 0; j < k; j++) {
                    range = getRange(data.get(i), cluster[j].get(0));
                    if(min == -1 || min > range) {
                        min = range;
                        idx = j;
                    }
                }
                cluster[idx].add(data.get(i));

                if(idx == 0) {
                    setPixelColor(bm, data.get(i)[0], data.get(i)[1], 255, 0, 0);
                }
                else if(idx == 1) {
                    setPixelColor(bm, data.get(i)[0], data.get(i)[1], 0, 255, 0);
                }
                else if(idx == 2) {
                    setPixelColor(bm, data.get(i)[0], data.get(i)[1], 0, 0, 255);
                }
                else {
                    setPixelColor(bm, data.get(i)[0], data.get(i)[1], 0, 0, 0);
                }
            }

            error = 0;
            for(int i = 0; i < k; i++) {
                error = error + Math.abs(result[i] - cluster[i].size() + 1);
            }

            if(error > 0) {
                for(int i = 0; i < k; i++) {
                    result[i]   = cluster[i].size() - 1;
                    sentroid    = getSentroid(cluster[i]);
                    cluster[i]  = new ArrayList<>();

                    cluster[i].add(sentroid);
                }
            }
            else {
                konvergen = true;
            }
        }
    }
}
