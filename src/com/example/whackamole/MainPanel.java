package com.example.whackamole;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainPanel extends SurfaceView implements
  SurfaceHolder.Callback {

	 private static final String TAG = MainPanel.class.getSimpleName();
	private MainThread thread;
	private MoleNormy normy;
	private Bitmap back;

	 public MainPanel(Context context) {
	  super(context);
	  // adding the callback (this) to the surface holder to intercept events
	  getHolder().addCallback(this);
	  // create droid and load bitmap
	  

	  // create the game loop thread
	  thread = new MainThread(getHolder(), this);

	  // make the GamePanel focusable so it can handle events
	  setFocusable(true);
	 }

	 @Override
	 public void surfaceChanged(SurfaceHolder holder, int format, int width,
	   int height) {
	 }

	 @Override
	 public void surfaceCreated(SurfaceHolder holder) {
	  // at this point the surface is created and
	  // we can safely start the game loop
	  normy = new MoleNormy(BitmapFactory.decodeResource(getResources(), R.drawable.normy), 50, 50,System.currentTimeMillis());
	  Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.ground1);
	  float scale = (float)background.getHeight()/(float)getHeight();
	  int newWidth = Math.round(background.getWidth()/scale);
      int newHeight = Math.round(background.getHeight()/scale);
	  back = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);
	  thread.setRunning(true);
	  thread.start();
	 }

	 @Override
	 public void surfaceDestroyed(SurfaceHolder holder) {
	  Log.d(TAG, "Surface is being destroyed");
	  // tell the thread to shut down and wait for it to finish
	  // this is a clean shutdown
	  boolean retry = true;
	  while (retry) {
	   try {
	    thread.join();
	    retry = false;
	   } catch (InterruptedException e) {
	    // try again shutting down the thread
	   }
	  }
	  Log.d(TAG, "Thread was shut down cleanly");
	 }

	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	  if (event.getAction() == MotionEvent.ACTION_DOWN) {
	   // delegating event handling to the droid
	   normy.handleActionDown((int)event.getX(), (int)event.getY());
	   System.out.println((int)event.getX());
	   System.out.println((int)event.getY());
	   // check if in the lower part of the screen we exit
	   if (event.getY() > getHeight() - 50) {
	    thread.setRunning(false);
	    ((Activity)getContext()).finish();
	   } else {
	    Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
	   }
	  }  if (event.getAction() == MotionEvent.ACTION_UP) {
	   // touch was released
	   if (normy.isTouched()) {
	    normy.setTouched(false);
	   }
	  }
	  return true;
	 }

	 private void keepTrackofMoles(Canvas canvas){
		 if(normy.getTime() > System.currentTimeMillis()){
			 System.out.println(normy.getY());
			 normy.draw(canvas);
		 }
	 }
	 @Override
	 protected void onDraw(Canvas canvas) {
	 	canvas.drawBitmap(back, 0, 0, null); // draw the background
	 	keepTrackofMoles(canvas);
	 }
	}