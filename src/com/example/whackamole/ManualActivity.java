package com.example.whackamole;

import com.example.whackamole.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Activity for the game manual, contains descriptions of each mole.
 */
public class ManualActivity extends Activity {

	private TextView name;
	private TextView description;
	private TextView touched;
	private TextView notTouched;
	private ImageView image;
	private int checkMole;
	private float onDownX;
	private float onDownY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		OnTouchListener myOnTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				float dx, dy;
				
				switch(event.getAction()) {
				    case(MotionEvent.ACTION_DOWN):
				        onDownX = event.getX();
				        onDownY = event.getY();
				        break;
				    
				    case(MotionEvent.ACTION_UP):
				        dx = event.getX() - onDownX;
				        dy = event.getY() - onDownY;
				        
				        // Use dx and dy to determine the direction
				        if(Math.abs(dx) <= Math.abs(dy)) {
				        	setNextMole(dy > 0);
				        }
				        break;	      
				}
				
			return true;  
			}		
		};
		
		setContentView(R.layout.activity_manual);
		image = (ImageView)findViewById(R.id.picture);
		name = (TextView)findViewById(R.id.nameManual);
		description = (TextView)findViewById(R.id.description);
		touched = (TextView)findViewById(R.id.touched);
		notTouched = (TextView)findViewById(R.id.notTouched);
		checkMole = 0;
		setMole(0);
		this.findViewById(android.R.id.content).setOnTouchListener(myOnTouchListener);
	}
	 
	private void setNextMole(boolean down) {
		if (down && checkMole < 8) checkMole++;
		else if (!down && checkMole > 0)checkMole--;
		setMole(checkMole);		
	}
	
	private void setNormy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.normy));
		name.setText("Normy");
		description.setText("Normy is just a normal everyday mole, but don't let him slip away!");
		touched.setText("Increase your score by one");
		notTouched.setText("Lose one life");
	}
	
	private void setHatty() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.hatty));
		name.setText("Hatty");
		description.setText("Wears a great hat, double tap!");
		touched.setText("Needs two touches, increase your score by two");
		notTouched.setText("Lose one life");
	}
	
	private void setTanky() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.tanky));
		name.setText("Tanky");
		description.setText("Tanky is a very strong mole.");
		touched.setText("Need three touches, increase your score by three");
		notTouched.setText("Lose one life");
	}
	
	private void setSpeedy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.speedy));
		name.setText("Speedy");
		description.setText("Speedy is a very fast mole.");
		touched.setText("Increase your score by two");
		notTouched.setText("Lose one life");
	}
	
	private void setGoldy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.goldy));
		name.setText("Goldy");
		description.setText("Goldy is a rich mole, has a big house and a nice car.");
		touched.setText("Increase your score by five");
		notTouched.setText("Lose one life");
	}
	
	private void setSniffy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.sniffy));
		name.setText("Sniffy");
		description.setText("Sniffy is always very sick, so dont touch him.");
		touched.setText("Lose one life");
		notTouched.setText("Increase your score by one");
	}
	
	private void setIcy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.icy));
		name.setText("icy");
		description.setText("Icy doesn't hybernate in his hole in the winter.");
		touched.setText("Freeze the other moles for 2 seconds, increase your score by one");
		notTouched.setText("Lose one life");
	}
	
	private void setBurny() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.burny));
		name.setText("Burny");
		description.setText("Burny doesn't like the other moles.");
		touched.setText("Burn the other moles ( same as touch ), increase your score by one");
		notTouched.setText("Lose one life");
	}
	
	private void setSmogy() {
		image.setImageDrawable( getResources().getDrawable(R.drawable.smogy));
		name.setText("Smogy");
		description.setText("Smogy, beware the smog.");
		touched.setText("Blur your screen for 3 seconds if touched, increases your score by 2.");
		notTouched.setText("Lose one life.");
	}
	
	private void setMole(int index){
		switch(index){
		case 0: 
			setNormy();
			break;
		case 1: 
			setHatty();
			break;
		case 2: 
			setTanky();
			break;
		case 3: 
			setSpeedy();
			break;
		case 4: 
			setGoldy();
			break;
		case 5: 
			setSniffy();
			break;
		case 6: 
			setSmogy();
			break;
		case 7: 
			setIcy();
			break;
		case 8: 
			setBurny();
			break;
		}
	}
	
}
