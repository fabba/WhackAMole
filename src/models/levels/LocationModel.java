package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

public class LocationModel {
	private float x, y, beginY;
	private ArrayList<MoleModel> moles;
	private MoleModel currentMole;
	
	public LocationModel(float x, float y) {
		this(x, y, y);
	}
	
	public LocationModel(float x, float y, float beginY) {
		this.x = x;
		this.y = y;
		this.beginY = beginY;
		this.moles = new ArrayList<MoleModel>();
		this.currentMole = null;
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
		float prevMoleTime = 0;
		float nextMoleTime = Float.MAX_VALUE;
		
		for (MoleModel mole : moles) {
			
			if (mole.getTime() + mole.getAppearanceTime() < time) {
				prevMoleTime = mole.getTime() + mole.getAppearanceTime();
			}
			else if (mole.getTime() >= time) {
				nextMoleTime = mole.getTime();
				break;
			}
		}
		
		// TODO remove on final release.
		//System.out.println("Location: " + x + "," + y + " nextMoleTime: " + nextMoleTime + " previousMoleTime: " + prevMoleTime +
		//		" appearanceTime: " + appearanceTime + " islegit: " + (nextMoleTime - prevMoleTime >= appearanceTime));
		return nextMoleTime - prevMoleTime >= appearanceTime;
	}

	public boolean addMole(MoleModel mole) {
		if (isRoomForMole(mole)) {
			float time = mole.getTime();
			int index = 0;
			
			for (int i = 0; i < moles.size(); i++) {
				MoleModel curMole = moles.get(i);
				
				if (curMole.getTime() + curMole.getAppearanceTime() < time) {
					index = i;
				}
				else if (curMole.getTime() > time) {
					break;
				}
			}
			
			moles.add(index, mole);
			return true;
		}
		
		return false;
	}
	
	public boolean setCurrentMole(MoleModel mole) {
		if (moles.contains(mole) || mole == null) {
			this.currentMole = mole;
			return true;
		} else {
			return false;
		}
	}
	
	public MoleModel getCurrentMole() {
		return this.currentMole;
	}
	
	public void reset() {
		this.moles.clear();
		this.currentMole = null;
	}
}
