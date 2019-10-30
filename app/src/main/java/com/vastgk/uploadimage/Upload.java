package com.vastgk.uploadimage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.*;

public class Upload extends Activity {
   private ArrayList<Uri> imgList;
  private  String uploadServerUrl="http://192.168.43.78/upload.php";
   private int count=0;
 private   Context mcontext;

    public Upload(ArrayList<Uri> imgList,Context mcontext) {
        this.imgList = imgList;
        this.mcontext=mcontext;
    }

    public void     startUpload() {
        Toast.makeText(mcontext, "Starting Upload", Toast.LENGTH_SHORT).show();
        if (imgList.size()<=0)
        {
            Toast.makeText(mcontext, "Please Select Images First", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Uri uri:imgList)
        {

            count++;


            Random random=new Random();
            String fileName="Test"+random.nextDouble()*random.nextInt(100)+".jpg";


            Myasync myasync=new Myasync(uri,fileName);
           myasync.execute();






        }
        if (count==imgList.size())
        {
            Toast.makeText(mcontext, "Full List Uploaded", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(mcontext, ""+count+"/"+imgList.size()+" img uploaded Successfully", Toast.LENGTH_SHORT).show();
        }





    }



private  class  Myasync extends AsyncTask<Void, Float, Void>
{
    private Uri img_uri;
   private String img_data,img_name;
   private  boolean isSuccess=false;

    public Myasync(Uri img_uri, String img_name) {
        this.img_uri = img_uri;
        this.img_name = img_name;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isSuccess)
            count++;
    }

    @Override
    protected Void doInBackground(Void ... voids) {

        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(),img_uri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        img_data= Base64.encodeToString(byteArray,Base64.DEFAULT);


        RequestQueue requestQueue= Volley.newRequestQueue(mcontext);
        StringRequest stringRequest=new StringRequest(Method.POST,uploadServerUrl,s->{
            Log.d(TAG, "doInBackground: "+s);
            Toast.makeText(mcontext, s.toString(), Toast.LENGTH_SHORT).show();

isSuccess=true;

        },e->{
            Log.d(TAG, "doInBackground: e"+e);
isSuccess=false;
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("image_str",img_data);
                param.put("image_name",img_name);
                return  param;
            }
        };


requestQueue.add(stringRequest);

return null;
    }


}



}
