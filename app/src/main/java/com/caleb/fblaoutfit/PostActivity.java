package com.caleb.fblaoutfit;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.net.Uri;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Plugins.ImageOrientationUtil;


public class PostActivity extends AppCompatActivity {

    //variables
    Uri fileUri;//path to image
    Calendar c = Calendar.getInstance();//get system time information
    SimpleDateFormat df = null;//tells app how to format date and time set to "yyyy-MM-dd HH:mm:ss"
    String formattedDate = null;//holds formatted date and time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        fileUri = launchCamera();//start camera and get uri (image location)
        final ImageView CamView = (ImageView) findViewById(R.id.CameraView90);//find imageview so it can be accessed in class

        //take new picture if old one is clicked on
        CamView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                fileUri = launchCamera();
            }
        });
    }

    public Uri launchCamera() {
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);//set date and time format
        formattedDate = df.format(c.getTime());//form
        /*if(fileUri != null) {
            Toast.makeText(PostActivity.this, "Saved to: " + fileUri.toString(), Toast.LENGTH_LONG).show();
        }*/

        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//set up camera app open intent
        //variables
        File image;
        File imagesFolder;
        //create folder to put image in
        imagesFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "/MyImage");//name folder and directory
        imagesFolder.mkdirs(); //create folder of this name at this directory
        //name image taken from camera in this folder
        image = new File(imagesFolder, "image_001" + formattedDate + ".jpg");//name file
        Uri uriSavedImage = Uri.fromFile(image);//convert file to uri
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);//open camera app
        startActivityForResult(imageIntent, 0);//save image
        return uriSavedImage;//return uri
    }

    //following 6 functions called on button click, sends string parameter to postTweet
    public void buttonOnClickOutfitProfessional(View v) {
        PostTweet("Does this outfit look professional? #FBLAoutfit");

    }

    public void buttonOnClickFBLAappropriate(View v){
         PostTweet("Is this outfit appropriate for FBLA? #FBLAoutfit");
    }

    public void buttonOnClickOutfitFBLAdresscode(View v){
          PostTweet("Does this outfit follow the FBLA dress code? #FBLAoutfit");
    }

    public void buttonOnClickFBLAhowtomakebetter(View v){
         PostTweet("What should I do to make this outfit better? #FBLAoutfit");
    }

    public void buttonOnClickStylish(View v){
        PostTweet("Does this outfit look stylish? #FBLAoutfit");
    }

    public void buttonOnClickThoughts(View v){
        PostTweet("What do you think about this outfit? #FBLAoutfit");
    }


    public void PostTweet(String question){
        //using passed string and fileUri variable, start tweet builder
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .image(fileUri)
                .text(question);
        builder.show();
    }

    //Updates image view when app returns from camera app, fixes a bug caused by updating before image taken

    int Rotation;
    @Override
    protected void onResume() {
        super.onResume();//Detect if camera closed
        activityResumed();//run when camera close detected
        //call other functions here if necessary
    }

    protected void activityResumed() {
        setRotationVariables(fileUri);//check rotation needed for image to be right side up
        //set up imageviews
        final ImageView CamView90 = (ImageView) findViewById(R.id.CameraView90);
        final ImageView CamView270 = (ImageView) findViewById(R.id.CameraView270);
        final ImageView CamView180 = (ImageView) findViewById(R.id.CameraView180);
        final ImageView CamView0 = (ImageView) findViewById(R.id.CameraView0);
        //clear imageviews
        CamView90.setImageResource(0);
        CamView270.setImageResource(0);
        CamView180.setImageResource(0);
        CamView0.setImageResource(0);
        //set to imageview based on rotation needed
        if (Rotation == 270)
            CamView90.setImageURI(fileUri);//update CamView to show new image
        else if (Rotation == 90)
            CamView270.setImageURI(fileUri);//update CamView to show new image
        else if (Rotation == 180)
            CamView180.setImageURI(fileUri);//update CamView to show new image
        else
            CamView0.setImageURI(fileUri);//update CamView to show new image
    }

    private void setRotationVariables(Uri uri)
    {
        Rotation = ImageOrientationUtil.getExifRotation(ImageOrientationUtil
                .getFromMediaUri(
                        this,
                        getContentResolver(),
                        uri));
    }
}


