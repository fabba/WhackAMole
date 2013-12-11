package models.levels;

import java.util.ArrayList;

import models.moles.MoleModel;

import com.example.whackamole.GameScene;

import databaseadapter.LevelAdapter;

public class LevelModel {

	private int numLevel, numberOfRounds;
	private ArrayList<LocationModel> locations;
	private GameScene gameScene;
	private RoundModel currentRound;
	
	public LevelModel(int numLevel, int numberOfRounds, ArrayList<LocationModel> locations,
			RoundModel round, GameScene scene) {
		this.numLevel = numLevel;
		this.locations = locations;
		this.gameScene = scene;
		this.currentRound = round;
		this.numberOfRounds = numberOfRounds;
	}
	
	public static LevelModel loadLevel(int numLevel, int numRound, GameScene scene) {
		LevelAdapter levelAdapter = new LevelAdapter();
		levelAdapter.open();
		ArrayList<LocationModel> locations = levelAdapter.getLocations(numLevel);
		RoundModel round = levelAdapter.getRound(numRound, numLevel, locations, scene);
		int numberOfRounds = levelAdapter.getNumberOfRounds(numLevel);
		levelAdapter.close();
		
		if (!locations.isEmpty() && round != null) {
			return new LevelModel(numLevel, numberOfRounds, locations, round, scene);
		} else {
			return null;
		}
	}
	
	public boolean nextRound() {
		// numRound starts at 1 thus the - 1
		if (currentRound.getNumRound() - 1 < numberOfRounds) { 
			for (LocationModel location : locations) {
				location.reset();
			}
			
			LevelAdapter levelAdapter = new LevelAdapter();
			levelAdapter.open();
			currentRound = levelAdapter.getRound(currentRound.getNumRound() + 1, numLevel, locations, gameScene);
			levelAdapter.close();
			
			return true;
		} else {
			return false;
		}
	}
	
	public void playRound() {
		// TODO write this function :)
		for (MoleModel mole : currentRound.getMoles()) {
			mole.jump();
		}
	}
	
	public int getNumLevel() {
		return this.numLevel;
	}
	
	public int getNumRound() {
		return this.currentRound.getNumRound();
	}
	
	public ArrayList<LocationModel> getLocations() {
		return this.locations;
	}
	
	public GameScene getGameScene() {
		return this.gameScene;
	}
	
	public RoundModel getCurrentRound() {
		return this.currentRound;
	}
	
	public int getNumberOfRounds() {
		return this.numberOfRounds;
	}
}
