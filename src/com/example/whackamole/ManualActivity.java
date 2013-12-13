package com.example.whackamole;

import com.example.whackamole.R;
import com.example.whackamole.R.drawable;
import com.example.whackamole.R.id;
import com.example.whackamole.R.layout;
import com.example.whackamole.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ManualActivity extends Activity implements OnSeekBarChangeListener {

	
	private TextView name;
	private TextView description;
	private TextView touched;
	private TextView notTouched;
	private ImageView image;
	private int checkMole;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual);
		image = (ImageView) findViewById(R.id.picture);
		System.out.println(findViewById(R.id.nameManual));
		name = (TextView) findViewById(R.id.nameManual);
		description = (TextView) findViewById(R.id.description);
		touched = (TextView) findViewById(R.id.touched);
		notTouched = (TextView) findViewById(R.id.notTouched);
		
		setNormy(false);
		SeekBar levelSelect = (SeekBar) findViewById(R.id.seekBar);
		levelSelect.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            	setMole(checkMole,false);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                // TODO Auto-generated method stub
            	setMole(progress,true);
        		checkMole = progress;

            }
        });
		levelSelect.setProgress(0);
	}
	
	private void setNormy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.normy));
		}
		else{
			name.setText("Normy");
			description.setText("Normy i just an average mole, but don't let him slip away");
			touched.setText("Increase your score by one");
			notTouched.setText("Lose one life");
			
		}
	}
	
	private void setHatty(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.hatty));
		}
		else{
			name.setText("Hatty");
			description.setText("With a great hat, but without the hat hatty is just like normy");
			touched.setText("Needs two touches, increase your score by two");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setTanky(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.tanky));
		}
		else{
			name.setText("Tanky");
			description.setText("Tanky is a very strong mole");
			touched.setText("Need three touches, increase your score by three");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setSpeedy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.speedy));
		}
		else{
			name.setText("Speedy");
			description.setText("Speedy is very fast mole");
			touched.setText("Increase your score by two");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setGoldy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.goldy));
		}
		else{
			name.setText("Goldy");
			description.setText("Goldy is just a rich mole, has a big house and a nice car");
			touched.setText("Increase your score by five");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setSniffy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.sniffy));
		}
		else{
			name.setText("Sniffy");
			description.setText("Sniffy is always very sick, so dont touch him");
			touched.setText("Lose one life");
			notTouched.setText("Increase your score by one");
		}
	}
	
	private void setIcy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.icy));
		}
		else{
			name.setText("icy");
			description.setText("Icy doesn't hybernate in his hole in the winter");
			touched.setText("Freeze the other moles for 2 seconds, increase your score by one");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setBurny(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.burny));
		}
		else{
			name.setText("Burny");
			description.setText("Burny doesn't like the other moles");
			touched.setText("Burn the other moles ( same as touch ), increase your score by one");
			notTouched.setText("Lose one life");
		}
	}
	
	private void setSmogy(boolean onlyPic){
		if(onlyPic){
			image.setImageDrawable( getResources().getDrawable(R.drawable.smogy));
		}
		else{
			name.setText("Smogy");
			description.setText("Smogy * kuch kuch kuch *");
			touched.setText("Blur your screen for 3 seconds , loses one life");
			notTouched.setText("Increase your score by one");
		}
	}
	
	private void setMole(int index, boolean onlyPic){
		switch(index){
		case 0: 
			setNormy(onlyPic);
			break;
		case 1: 
			setHatty(onlyPic);
			break;
		case 2: 
			setTanky(onlyPic);
			break;
		case 3: 
			setSpeedy(onlyPic);
			break;
		case 4: 
			setGoldy(onlyPic);
			break;
		case 5: 
			setSniffy(onlyPic);
			break;
		case 6: 
			setSmogy(onlyPic);
			break;
		case 7: 
			setIcy(onlyPic);
			break;
		case 8: 
			setBurny(onlyPic);
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manual, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		setMole(progress,false);
		checkMole = progress;
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		setMole(checkMole,true);
		
	}

}
