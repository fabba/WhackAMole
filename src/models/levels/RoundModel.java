package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

public class RoundModel {
	
	private int level;
	private int numRound;
	private ArrayList<MoleModel> moles;
	
	public RoundModel(int numRound, int level, ArrayList<MoleModel> moles) {
		this.level = level;
		this.moles = moles;
		this.numRound = numRound;
	}
	
	public ArrayList<MoleModel> getMoles() {
		return this.moles;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public int getNumRound() {
		return this.numRound;
	}
}
