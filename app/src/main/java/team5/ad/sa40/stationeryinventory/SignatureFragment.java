package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.API.UploadAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignatureFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    //UI attribute
    @Bind(R.id.signArea) LinearLayout signArea;

    //attribute
    private Bitmap mBitmap;
    private String uniqueId;
    public String current = null;
    private File mypath;
    JSONCollectionPoint selected_colPt;
    View mView;
    public static String tempDir;
    signature mSignature;
    String abc;

    JSONDisbursement dis;
    int RepID;
    public SignatureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        Bundle bundle = this.getArguments();
        dis = (JSONDisbursement)bundle.getSerializable("disbursement");
        selected_colPt = (JSONCollectionPoint) bundle.getSerializable("collection");
        RepID = bundle.getInt("RepID");

        getActivity().setTitle("Disbursement List Detail");

        mSignature = new signature(SignatureFragment.this.getActivity(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        signArea.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mView = signArea;

//        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
//        ContextWrapper cw = new ContextWrapper(SignatureFragment.this.getActivity().getApplicationContext());
//        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);
//        prepareDirectory();
//        uniqueId = getTodaysDate() + Setup.user.getEmpID();
//        current = uniqueId + ".png";
//        mypath = new File(directory, current);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_requestcart_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_request_done) {
            //save image
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    //***************************************************************************************************************

    void save(){
        boolean error = captureSignature();
        if(!error){
            try {
                mView.setDrawingCacheEnabled(true);
                mBitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(mBitmap);
                mView.draw(canvas);
                this.SaveImage(mBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", "print");
            }
        }

    }
    private String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void SaveImage(Bitmap finalBitmap) {

//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/signatures");
//        myDir.mkdirs();
//        String fname = String.valueOf(dis.getDisID())+".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();

        JsonObject object = new JsonObject();
        String img_str = BitMapToString(mBitmap);
        object.addProperty("imageStr", img_str);
        object.addProperty("Filename", dis.getDisID());

        System.out.print("File "+ object.toString());
        Log.e("Img_File", object.get("Filename").toString());

        UploadAPI uploadAPI = restAdapter.create(UploadAPI.class);
        uploadAPI.uploadSignature(object, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Img_URL is:", s);

                DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
                disbursementAPI.completeDisbursement(dis.getDisID(), new Callback<Boolean>() {
                    @Override
                    public void success(Boolean aBoolean, Response response) {
                        Log.e("Reach near alert", "Alert");
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Success")
                                .setMessage("The disbursement process is completed!")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        android.support.v4.app.Fragment frag = new ClerkDisList();
                                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", error.toString());
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Img_Upload Error", error.toString());
                new AlertDialog.Builder(getActivity())
                        .setTitle("Fail")
                        .setMessage("The disbursement process cannot be completed!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private boolean captureSignature() {

        boolean error = false;
        String errorMessage = "";

        if(error){
            Toast toast = Toast.makeText(SignatureFragment.this.getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 105, 50);
            toast.show();
        }

        return error;
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));

    }


    private boolean prepareDirectory() {
        try {
            if (makedirs()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SignatureFragment.this.getActivity(), "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    @Override
    public void doBack() {
        android.support.v4.app.Fragment frag = new ClerkDisListDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("disbursement", dis);
        bundle.putSerializable("collection", selected_colPt);
        frag.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
    }

    //***************************************************************************************************************
    //***************************************************************************************************************
    //signature class implementation start here
    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = MediaStore.Images.Media.insertImage(SignatureFragment.this.getActivity().getContentResolver(), mBitmap, "title", null);
                Log.v("log_tag", "url: " + url);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }
            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
            lastTouchX = eventX;
            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY)

        {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }
            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }

    }
}
