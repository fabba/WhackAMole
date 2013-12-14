package models.levels;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import models.moles.MoleModel;

public class LocationModel {
	private float x, y, beginY;
	private ArrayList<MoleModel> moles;
	private MoleModel activeMole;
	private int activeMoleIndex;
	private float timeOffset;
	private long startTime;
	
	public LocationModel(float x, float y) {
		this(x, y, y);
	}
	
	public LocationModel(float x, float y, float beginY) {
		this.x = x;
		this.y = y;
		this.beginY = beginY;
		this.moles = new ArrayList<MoleModel>();
		this.reset();
	}
	
	public void setStartTime(long time) {
		this.startTime = time;
	}
	
	public float getBeginY() {
		return this.beginY;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public ArrayList<MoleModel> getMoles() {
		return this.moles;
	}
	
	public boolean isRoomForMole(MoleModel mole) {
		return isRoomForMole(mole.getTime(), mole.getAppearanceTime());
	}
	
	public boolean isRoomForMole(float time, float appearanceTime) {
		float currentMoleGoDownTime = time + appearanceTime;
		
		for (MoleModel mole : moles) {
			float moleGoUpTime = getPopUpTime(mole);
			float moleGoDownTime = moleGoUpTime + mole.getAppearanceTime();
			
			if (!(moleGoDownTime <= time || time < moleGoUpTime)) {
				return false;
			}
			else if (!(moleGoUpTime >= currentMoleGoDownTime || currentMoleGoDownTime > moleGoDownTime)) {
				return false;
			}
			else if (!(time >= moleGoDownTime || moleGoDownTime > currentMoleGoDownTime)) {
				return false;
			}
		}
		
		// TODO remove on final release.
		//System.out.println("Location: " + x + "," + y + " nextMoleTime: " + nextMoleTime + " previousMoleTime: " + prevMoleTime +
		//		" current moletime = " + time + " appearanceTime: " + appearanceTime +
		//		" islegit: " + (nextMoleTime - prevMoleTime >= appearanceTime));
		return true;
	}

	public MoleModel getFirstMole() {
		if (moles.size() > 0) {
			return moles.get(0);
		} else {
			return null;
		}
	}
	
	public boolean isPopUpTime(MoleModel mole, float time) {
		float moleGoUpTime = getPopUpTime(mole);
		float moleGoDownTime = moleGoUpTime + mole.getAppearanceTime();	
		return (time >= moleGoUpTime) && (moleGoDownTime > time);
	}
	
	public float getPopUpTime(MoleModel mole) {
		return mole.getTime() + this.timeOffset;
	}
	
	public boolean addMole(MoleModel mole) {
		if (isRoomForMole(mole)) {
			float time = mole.getTime();
			int index = -1;
			
			for (int i = 0; i < moles.size(); i++) {
				MoleModel curMole = moles.get(i);
				float curMoleGoUpTime = getPopUpTime(curMole);
				float curMoleGoDownTime = curMoleGoUpTime + curMole.getAppearanceTime(); 
				
				if (curMoleGoDownTime < time) {
					index = i;
				}
				else if (curMoleGoUpTime > time) {
					break;
				}
			}
			
			moles.add(index + 1, mole);
			
			return true;
		}
		
		return false;
	}
	
	public void reset() {
		this.moles.clear();
		this.activeMole = null;
		this.activeMoleIndex = -1;
		this.timeOffset = 0;
		this.startTime = System.currentTimeMillis();
	}
	
	public void onMoleDeath(MoleModel mole) {
		if (this.activeMole.equals(mole)) {
			this.activeMole = null;
		}
	}
	
	public boolean setNextActiveMole() {
		this.activeMole = getNextActiveMole();
		
		if (activeMole != null) {
			this.activeMoleIndex++;
			return true;
		} else {
			return false;
		}
	}
	
	public MoleModel getNextActiveMole() {
		if (this.activeMoleIndex < this.moles.size() - 1) {
			return this.moles.get(this.activeMoleIndex + 1);
		} else {
			return null;
		}
	}
	
	public MoleModel getActiveMole() {
		return this.activeMole;
	}
	
	public void freeze(float time) {
		if (activeMole != null) {
			activeMole.freeze();
		}
		
		this.timeOffset += time;
	}
	
	public void unfreeze() {
		if (activeMole != null) {
			activeMole.unfreeze();
		}	
	}
}
