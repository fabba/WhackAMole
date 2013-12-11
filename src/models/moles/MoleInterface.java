package models.moles;

import org.andengine.input.touch.TouchEvent;

public interface MoleInterface {
	public void onDie();
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY);
	public void touched();
}
