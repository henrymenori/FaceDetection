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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // attribute
    ImageView   imageTop;
    TextView    edgeDetectionOrder, edgeDetectionMethod, blurringMethod, txtresult;
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
        txtresult           = (TextView)findViewById(R.id.txtResult);

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
        thresholding(bmp1);
        reverse(bmp1);
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
        int     value, max, multiply;
        Bitmap  temp;

        temp = imageCopy(bm);
        multiply = 8;
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
                if(max * multiply > 255) {
                    max = 255;
                }
                else {
                    max = max * multiply;
                }
                setPixelGrayscale(bm, i, j, max);
            }
        }
    }

    public void zeroOrderDifference(Bitmap bm) {
        int[]   neighbour;
        int     max, multiply;
        Bitmap  temp;

        temp = imageCopy(bm);
        multiply = 8;
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
                if(max * multiply > 255) {
                    max = 255;
                }
                else {
                    max = max * multiply;
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

                    if(value * 5 > 255) {
                        value = 255;
                    }
                    else {
                        value = value * 5;
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

        // get mouth side
        int max_m, min_m, max_index, min_index;

        max_m = cluster[3].get(0)[0];
        min_m = cluster[3].get(0)[0];
        max_index = 0;
        min_index = 0;

        for(int i = 1; i < cluster[3].size(); i++) {
            if(max_m < cluster[3].get(i)[0]) {
                max_m = cluster[3].get(i)[0];
                max_index = i;
            }
            if(min_m > cluster[3].get(i)[0]) {
                min_m = cluster[3].get(i)[0];
                min_index = i;
            }
        }
        Integer[][] input = {
                cluster[0].get(0),
                cluster[1].get(0),
                cluster[2].get(0),
                cluster[3].get(min_index),
                cluster[3].get(max_index)
        };

        txtresult.setText("Result : " + faceRatio(input));
    }

    public void colorMap(Bitmap bm) {
        int[]   map = {233, 200, 191};
        int[]   pixel;
        int     range;

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                pixel = getPixelColor(bm, i, j);
                range = Math.abs(pixel[0] - map[0]) +
                        Math.abs(pixel[1] - map[1]) +
                        Math.abs(pixel[2] - map[2]);
                if(range < 90) {
                    setPixelColor(bm, i, j, 255, 0, 255);
                }
                else {
                    setPixelColor(bm, i, j, 0, 255, 0);
                }
            }
        }
    }

    public String faceRatio(Integer[][] input) {
        double[][] data = {
            {237.93, 246.01, 208.6,  213.6},
            {252.49, 252.75, 214.96, 219.27},
            {279.30, 292.96, 254.15, 267.23},
            {302.18, 295.42, 219.31, 211.824},
            {274.42, 253.56, 187.20, 217.71},
            {363.27, 352.28, 321.30, 324.15},
            {258.44, 278.99, 254,	 263,53},
            {332,	 322.64, 274.4,	 291.67},
            {704.7,	 769.3,	 887.5,	 852.2},
            {674.9,	 654.9,	 683.4,	 639},
            {312.8,	 305.8,	 247.8,	 242.8},
            {250.60, 258.36, 229.02, 201.24},
            {295.66, 291.67, 197.99, 235.22},
            {249.83, 245.35, 237.66, 249.04},
            {282,	 275,	 247,	 247},
            {266.11, 284.55, 263.11, 260.72},
            {247.88, 207.39, 240,83, 243.32},
            {215.23, 198.07, 241.48, 274.54},
            {218.19, 188.86, 221.27, 260.40},
            {234.80, 199.29, 219.92, 268.50},
            {604.15, 557.04, 667.87, 680.87},
            {316.65, 286.05, 172.54, 257.41},
            {313.96, 293.47, 250.06, 268.60},
            {335.57, 341.56, 464.13, 389.50},
            {525.13, 555.11, 430.20, 428.70},
            {76.00,	 72.25,	 57.14,	 58.80},
            {236.18, 235.93, 202.50, 212.44},
            {247.29, 247.66, 216.89, 213.64},
            {243.31, 237.12, 171.84, 172.63}
        };

        double[]    range = new double[4];
        double      difference, minimum;
        int         index;
        String      result;

        minimum = -1.0;
        index   = 0;
        for(int i = 0; i < data.length; i++) {
            range[0] = getRange(input[0],input[2]);
            range[1] = getRange(input[1],input[2]);
            range[2] = getRange(input[3],input[2]);
            range[3] = getRange(input[4], input[2]);

            difference =    Math.abs(range[0] / range[1] - data[i][0] / data[i][1]) +
                            Math.abs(range[2] / range[3] - data[i][2] / data[i][3]);

            if(minimum > difference || minimum == -1) {
                minimum = difference;
                index = i;
            }
        }

        if(index == 0) result = "Stephen";
        else if(index == 1) result = "Lingga";
        else if(index == 2) result = "Zaky";
        else if(index == 3) result = "Andre";
        else if(index == 4) result = "Yafi";
        else if(index == 5) result = "Rama";
        else if(index == 6) result = "Kevin Yudi";
        else if(index == 7) result = "Gilang";
        else if(index == 8) result = "Riady";
        else if(index == 9) result = "Ucup";
        else if(index == 10) result = "Winson";
        else if(index == 11) result = "Afik";
        else if(index == 12) result = "Mamat";
        else if(index == 13) result = "Luthfi";
        else if(index == 14) result = "Dariel";
        else if(index == 15) result = "Fahmi";
        else if(index == 16) result = "Ahmad";
        else if(index == 17) result = "Bagas";
        else if(index == 18) result = "Adhika";
        else if(index == 19) result = "Fauzan";
        else if(index == 20) result = "Khaidzir";
        else if(index == 21) result = "Ilmi";
        else if(index == 22) result = "Ichwan";
        else if(index == 23) result = "Luqman";
        else if(index == 24) result = "Tony";
        else if(index == 25) result = "Menori";
        else if(index == 26) result = "Eldwin";
        else result = "Atia";

        return result;
    }
}
