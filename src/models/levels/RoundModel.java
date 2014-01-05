package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

/**
 * Model for a round of whack-a-mole. Contains only data.
 */
public class RoundModel {
	
	private int numRound;
	private ArrayList<MoleModel> moles;
	private LevelModel level;
	
	public RoundModel(int numRound, ArrayList<MoleModel> moles, LevelModel level) {
		this.level = level;
		this.moles = moles;
		this.numRound = numRound;
	}
	
	public LevelModel getLevel() {
		return this.level;
	}
	
	public ArrayList<MoleModel> getMoles() {
		return this.moles;
	}
	
	public int getNumRound() {
		return this.numRound;
	}
}
