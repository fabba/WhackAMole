package com.example.whackamole;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class MoleNormy {

 private Bitmap bitmap; // the actual bitmap
 private int x;   // the X coordinate
 private float y;   // the Y coordinate
 private float transY;
 private long currentTime;
 private float appearanceTime = 100;
 private boolean touched; // if droid is touched/picked up

 public MoleNormy(Bitmap bitmap, int x, float y, long currentTime) {
  this.currentTime = currentTime;
  this.bitmap = bitmap;
  this.x = x;
  this.y = y;
  transY = -(50/(appearanceTime/2));
 }

 public Bitmap getBitmap() {
  return bitmap;
 }
 
 public long getTime(){
	 return (long) (this.currentTime + appearanceTime * 100);
 }
 
 public void setBitmap(Bitmap bitmap) {
  this.bitmap = bitmap;
 }
 public int getX() {
  return x;
 }
 public void setX(int x) {
  this.x = x;
 }
 public float getY() {
  return y;
 }
 public void setY(int y) {
  this.y = y;
 }

 public boolean isTouched() {
  return touched;
 }

 public void setTouched(boolean touched) {
  this.touched = touched;
 }

 public void draw(Canvas canvas) {
	
  if( y < 20){
	  transY = -transY;
  }
  y = y + transY;

  canvas.drawBitmap(bitmap, x, y, null);
 }

 public void handleActionDown(int eventX, int eventY) {
  if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
   if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
    // droid touched
    setTouched(true);
   } else {
    setTouched(false);
   }
  } else {
   setTouched(false);
  }

 }
}
