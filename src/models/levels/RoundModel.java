package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

public class RoundModel {
	
	private int numRound;
	private ArrayList<MoleModel> moles;
	
	public RoundModel(int numRound, ArrayList<MoleModel> moles) {
		this.moles = moles;
		this.numRound = numRound;
	}
	
	public ArrayList<MoleModel> getMoles() {
		return this.moles;
	}
	
	public int getNumRound() {
		return this.numRound;
	}
}
