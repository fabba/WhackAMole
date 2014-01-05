package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

/**
 * Model for a location at which moles can appear.
 */
public class LocationModel {
	private float x, y, beginY;
	private ArrayList<MoleModel> moles;
	private MoleModel activeMole;
	private int activeMoleIndex;
	private float timeOffset;
	
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
	
	public float getBeginY() {
		return this.beginY;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	/**
	 * Get a list of moles that pop up at this location in order of time.
	 * @return list of moles.
	 */
	public ArrayList<MoleModel> getMoles() {
		return this.moles;
	}
	
	/**
	 * Checks whether there is room for this mole (depends on time and appearanceTime).
	 * @param mole
	 * @return true if there is room, false otherwise.
	 */
	public boolean isRoomForMole(MoleModel mole) {
		return isRoomForMole(mole.getTime(), mole.getAppearanceTime());
	}
	
	/**
	 * Checks whether there is room for a mole with time and appearanceTime.
	 * @param time the time at which the mole is to appear.
	 * @param appearanceTime the time for which the mole is to appear.
	 * @return true if there is room, false otherwise.
	 */
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
		
		return true;
	}

	/**
	 * Get the first mole that is to appear in this location.
	 * @return the first mole, null if no moles appear here.
	 */
	public MoleModel getFirstMole() {
		if (moles.size() > 0) {
			return moles.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Checks whether it is time for mole to pop up at time.
	 * This is important as the pop up time might shift during the course
	 * of a game by for instance the freeze effect.
	 * @param mole 
	 * @param time
	 * @return true if it is time to pop up, false otherwise.
	 */
	public boolean isPopUpTime(MoleModel mole, float time) {
		float moleGoUpTime = getPopUpTime(mole);
		float moleGoDownTime = moleGoUpTime + mole.getAppearanceTime();	
		return (time >= moleGoUpTime) && (moleGoDownTime > time);
	}
	
	/**
	 * Get the earliest time at which the mole is supposed to pop up.
	 * @param mole
	 * @return
	 */
	public float getPopUpTime(MoleModel mole) {
		return mole.getTime() + this.timeOffset;
	}
	
	/**
	 * Add a mole to the location.
	 * @param mole
	 * @return true if succeeded, false otherwise (there might be no room!).
	 */
	public boolean addMole(MoleModel mole) {
		if (isRoomForMole(mole)) {
			float time = mole.getTime();
			int index = -1;
			
			for (int i = 0; i < moles.size(); i++) {
				MoleModel curMole = moles.get(i);
				float curMoleGoUpTime = getPopUpTime(curMole);
				float curMoleGoDownTime = curMoleGoUpTime + curMole.getAppearanceTime(); 
				
				if (curMoleGoDownTime <= time) {
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
	
	/**
	 * Reset this location, clearing all the moles.
	 */
	public void reset() {
		this.moles.clear();
		this.activeMole = null;
		this.activeMoleIndex = -1;
		this.timeOffset = 0;
	}
	
	/**
	 * Called on mole death, removes the active mole if this was the mole
	 * that died.
	 * @param mole the mole that died.
	 */
	public void onMoleDeath(MoleModel mole) {
		if (this.activeMole.equals(mole)) {
			this.activeMole = null;
		}
	}
	
	/**
	 * Set the next active mole.
	 * @return true if succesfully set (not null), false otherwise
	 */
	public boolean setNextActiveMole() {
		this.activeMole = getNextActiveMole();
		
		if (activeMole != null) {
			this.activeMoleIndex++;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the next active mole if there is one.
	 * @return the next active mole, if non null.
	 */
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
	
	/**
	 * Called on burn, unavoidably touches the active mole.
	 */
	public void burn() {
		if (activeMole != null) {
			activeMole.unavoidableTouched();
		}
	}
	
	/**
	 * Called on freeze, and freezes the location and active mole.
	 * @param time the time for which to freeze in seconds.
	 */
	public void freeze(float time) {
		if (activeMole != null) {
			activeMole.freeze();
		}
		this.timeOffset += time;
	}
	
	/**
	 * Called on unfreeze, unfreezes the active mole.
	 */
	public void unfreeze() {
		if (activeMole != null) {
			activeMole.unfreeze();
		}	
	}
}
