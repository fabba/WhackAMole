package models.levels;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import models.moles.MoleModel;

import com.example.whackamole.GameScene;

import databaseadapter.LevelAdapter;

public class LevelModel {

	private int numLevel, numberOfRounds;
	private ArrayList<LocationModel> locations;
	private GameScene gameScene;
	private RoundModel currentRound;
	private long startTime;
	private int molesRemaining;
	
	public LevelModel(int numLevel, int numberOfRounds, ArrayList<LocationModel> locations,
			RoundModel round, GameScene scene) {
		this.numLevel = numLevel;
		this.locations = locations;
		this.gameScene = scene;
		this.currentRound = round;
		this.numberOfRounds = numberOfRounds;
		this.startTime = System.currentTimeMillis();
		this.molesRemaining = this.currentRound.getMoles().size();
		
		// synchronize startTimes.
		for (LocationModel location : locations) {
			location.setStartTime(this.startTime);
		}
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
			this.currentRound = levelAdapter.getRound(currentRound.getNumRound() + 1,
					numLevel, locations, gameScene);
			levelAdapter.close();
			
			this.startTime = System.currentTimeMillis();
			this.molesRemaining = this.currentRound.getMoles().size();
			
			// synchronize startTimes.
			for (LocationModel location : locations) {
				location.setStartTime(this.startTime);
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public void onMoleDeath(MoleModel mole) {
		this.molesRemaining--;
		System.out.println("Remaining moles: " + this.molesRemaining);
	}
	
    public void burnOthers(){
    	for(LocationModel location : locations){
    		MoleModel mole = location.getActiveMole();
    		if(mole != null){
    			mole.touched();
    		}
    	}
    }
    
    public void freeze(long time){
    	//allFore.setCurrentTileIndex(1);
    	for (LocationModel location : locations) {
    		location.freeze(time);
    	}
    }
    
    public void smog(){
    	//allFore.setCurrentTileIndex(0);
    }
    
    public void unsmog(){
    	//allFore.setCurrentTileIndex(2);
    }
    
    
    public void blur(){
    }
    
    private void scheduleMolePopUp(final MoleModel mole, final float time, final float prevTime) {
    	System.out.println("Schedule time: " + (long)(time - prevTime) * 1000 + " prevTime: " + prevTime + 
    			" time: " + time);
    	
    	new Timer().schedule(
			new TimerTask() {
				float popUpTime = time;
				MoleModel popUpMole = mole;
				
				@Override
				public void run() {
					LocationModel location = popUpMole.getLocation();
					
					if (location.isPopUpTime(popUpMole, popUpTime)) {
						location.setNextActiveMole();
						popUpMole.jump();
						
						// schedule next mole if there is one.
						MoleModel nextActiveMole = location.getNextActiveMole();
						if (nextActiveMole != null) {
							float nextPopUpTime = location.getPopUpTime(nextActiveMole);
							
							System.out.println("nextPopUpTime: " + nextPopUpTime + " time: " + time);
							
							scheduleMolePopUp(nextActiveMole, nextPopUpTime, time);
						}
					} else {
						scheduleMolePopUp(popUpMole, location.getPopUpTime(popUpMole), time);
					}
				}
			},
			(long)(time - prevTime) * 1000
		);
    }
    
	public void playRound() {
		for (LocationModel location : locations) {
			MoleModel mole = location.getFirstMole();
			
			if (mole != null) {
				scheduleMolePopUp(mole, location.getPopUpTime(mole), 0);
			}
		}
	}
	
	public long getStartTime() {
		return this.startTime;
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
