package models.levels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import models.moles.MoleModel;

public class LocationModel {
	private float x, y, beginY;
	private ArrayList<MoleModel> moles;
	private MoleModel activeMole;
	private int activeMoleIndex;
	private int timeOffset;
	private float freezeTime;
	private float freezeDuration;
	private long startTime;
	
	public LocationModel(float x, float y) {
		this(x, y, y);
	}
	
	public LocationModel(float x, float y, float beginY) {
		this.x = x;
		this.y = y;
		this.beginY = beginY;
		this.moles = new ArrayList<MoleModel>();
		this.activeMole = null;
		this.activeMoleIndex = -1;
		this.timeOffset = 0;
		this.freezeTime = -1;
		this.freezeDuration = 0;
		this.startTime = System.currentTimeMillis();
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

	public MoleModel getFirstMole() {
		if (moles.size() > 0) {
			return moles.get(0);
		} else {
			return null;
		}
	}
	
	public boolean isPopUpTime(MoleModel mole, float time) {
		float moleGoUpTime = mole.getTime() + this.timeOffset;
		float moleGoDownTime = moleGoUpTime + mole.getAppearanceTime() * 3; // TODO remove * 3	
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
				
				if (curMole.getTime() + curMole.getAppearanceTime() < time) {
					index = i;
				}
				else if (curMole.getTime() > time) {
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
	
	// TODO remove parameter
	public MoleModel getActiveMole(float time) {
		return this.activeMole;
		
		/*
		// is location currently frozen?
		if (this.freezeTime > 0) {
			
			// if frozen time has passed, 'unfreeze', continue like it is not frozen.
			// else, return active mole, and stay frozen.
			if (time > this.freezeTime + this.freezeDuration) {
				this.freezeTime = -1;
				this.freezeDuration = 0;
				
				if (this.activeMole != null) {
					this.activeMole.unfreeze();
				}
				
				System.out.println("Unfreezing at time: " + time);
			} else {
				System.out.println("Still FROZEN at time: " + time);
				return activeMole;
			}
		}
		
		// has current mole passed his time, yes? remove.
		if (this.activeMole != null) {
			float moleGoDownTime = this.activeMole.getTime() + this.timeOffset +
					this.activeMole.getAppearanceTime() * 3; // TODO remove * 3
			if (moleGoDownTime <= time) {
				activeMole = null;
			}
		}
		
		// is there no mole currently present, and is there a next mole,
		// see if it is time to load the next mole.
		if (this.activeMole == null && this.activeMoleIndex < this.moles.size() - 1) {
			MoleModel mole = this.moles.get(this.activeMoleIndex + 1);
			float moleGoUpTime = mole.getTime() + this.timeOffset;
			float moleGoDownTime = moleGoUpTime + mole.getAppearanceTime() * 3; // TODO remove * 3
			
			if ((time >= moleGoUpTime) && (moleGoDownTime > time)) {
				activeMole = mole;
				this.activeMoleIndex++;
			}
		}
		
		return activeMole;
		*/
	}
	
	public void freeze(final long time) {
		this.freezeTime = (System.currentTimeMillis() - startTime) / 1000;
		this.freezeDuration = time / 1000;
		this.timeOffset += this.freezeDuration;
		
		System.out.println("FREEZING for time: " + time + " at: " + this.freezeTime);
		
		if (activeMole != null) {
			activeMole.freeze();
			unfreeze(this.freezeTime + this.freezeDuration);
		}
	}
	
	public void unfreeze(final float time) {
		new Timer().schedule(
			new TimerTask() {
				
				@Override
				public void run() {
					if (time >= freezeTime + freezeDuration) {
						freezeTime = -1;
						freezeDuration = 0;
						
						if (activeMole != null) {
							activeMole.unfreeze();
						}
						
						System.out.println("Unfreezing at time: " + time);
					}
				}
			},
			(long)(time) * 1000
		);
	}
}
