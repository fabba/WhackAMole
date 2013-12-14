package models.levels;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import com.example.whackamole.GameScene;

import models.moles.MoleModel;

import databaseadapter.LevelAdapter;

public class LevelModel {

	private int numLevel, numberOfRounds, score, molesRemaining, lives;
	private float freezeTime, freezeDuration, timeOffset;
	
	private ArrayList<LocationModel> locations;
	private GameScene gameScene;
	private RoundModel currentRound;
	private long startTime;
	
	private LevelModel(int numLevel, GameScene scene) {
		this.numLevel = numLevel;
		this.locations = null;
		this.gameScene = scene;
		this.currentRound = null;
		this.numberOfRounds = -1;
		this.timeOffset = 0;
		this.freezeTime = -1;
		this.freezeDuration = 0;
	}
	
	public static LevelModel loadLevel(int numLevel, int numRound, GameScene scene) {
		LevelModel level = new LevelModel(numLevel, scene);
		
		LevelAdapter levelAdapter = new LevelAdapter();
		levelAdapter.open();
		level.setLocations(levelAdapter.getLocations(numLevel));
		RoundModel round = levelAdapter.getRound(numRound, level);
		int numberOfRounds = levelAdapter.getNumberOfRounds(numLevel);
		levelAdapter.close();
		
		if (!level.getLocations().isEmpty() && round != null) {
			level.initializeRound(round, numberOfRounds);
		} else {
			// TODO raise exception
		}
		
		return level;
	}
	
	private void initializeRound(RoundModel round, int numberOfRounds) {
		this.currentRound = round;
		this.numberOfRounds = numberOfRounds;
		
		this.startTime = System.currentTimeMillis();
		this.molesRemaining = this.currentRound.getMoles().size();
		this.score = 0;
		this.lives = 6; // TODO load from database?
		
		this.gameScene.onLivesUpdated(this.lives);
		
		// synchronize startTimes.
		for (LocationModel location : locations) {
			location.setStartTime(this.startTime);
		}
	}
	
	public boolean nextRound() {
		System.out.println("Current round num: " + currentRound.getNumRound() + " Total num of rounds " + numberOfRounds);
		if (currentRound.getNumRound() < numberOfRounds) { 
			for (LocationModel location : locations) {
				location.reset();
			}
			
			LevelAdapter levelAdapter = new LevelAdapter();
			levelAdapter.open();
			this.currentRound = levelAdapter.getRound(currentRound.getNumRound() + 1, this);
			levelAdapter.close();
			
			initializeRound(this.currentRound, this.numberOfRounds);
			
			return true;
		} else {
			return false;
		}
	}

	public void setLocations(ArrayList<LocationModel> locations) {
		this.locations = locations;
	}
    
    public MoleModel createMole(Class<?> moleClass, LocationModel location,
    		float time, float appearanceTime){
    	
    	try {
    		Constructor<?> constructor = moleClass.getConstructor(
					LocationModel.class, float.class, float.class, LevelModel.class);
    		return (MoleModel)constructor.newInstance(location, time, appearanceTime, this);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
	
    public ArrayList<MoleModel> createMoles(ArrayList<Class<?>> moleClasses,
    		ArrayList<Float> times, ArrayList<Float> appearanceTimes) {
    	
    	ArrayList<MoleModel> moles = new ArrayList<MoleModel>();
    	int size = moleClasses.size();
    	
    	for (int i = 0; i < size; i++) {
    		float time = times.get(i);
    		float appearanceTime = appearanceTimes.get(i);
    		Class<?> moleClass = moleClasses.get(i);
    		
	    	ArrayList<Integer> shuffledIndexes = new ArrayList<Integer>();
	    	for (int j = 0; j < locations.size(); j++) {
	    		shuffledIndexes.add(j);
	    	}
	    	Collections.shuffle(shuffledIndexes);
	    	
	    	boolean placementSucceeded = false;
	    	for (int index : shuffledIndexes) {
	    		LocationModel location = locations.get(index);
	    		
	    		System.out.println("Placement succeeded " + location.isRoomForMole(time, appearanceTime));
	    		
	    		if (location.isRoomForMole(time, appearanceTime)) {
	    			MoleModel mole = createMole(moleClass, location, time, appearanceTime);
	    			
	    			if (mole != null) {
	    				location.addMole(mole);
	    				moles.add(mole);
	    			}
	    			
	    			placementSucceeded = true;
	    			break;
	    		}
	    	}
	    	
	    	if (!placementSucceeded) {
	    		// TODO throw exception
	    	}
    	}
    	
    	return moles;
    }
	
	public void onMoleDeath(MoleModel mole) {
		this.molesRemaining--;
		System.out.println("Remaining moles: " + this.molesRemaining);
		
		mole.getLocation().onMoleDeath(mole);
		
		if (this.molesRemaining == 0) {
			gameScene.onRoundEnd(this);
		}
	}
	
	public void addLives(int lives) {
    	this.lives += lives;
    	
    	if (this.lives <= 0) {
    		gameScene.onGameLost();
    	}
    	
    	gameScene.onLivesUpdated(this.lives);
    }
    
    public void loseLives(int lives) {	
    	addLives(-lives);
    }
	
	public void addToScore(int score) {
		this.score += score;
        gameScene.onScoreUpdated();
    }
	
    public void burn() {
    	for (LocationModel location : locations) {
    		MoleModel mole = location.getActiveMole();
    		if (mole != null) {
    			mole.unavoidableTouched();
    		}
    	}
    	
    	gameScene.onBurn();
    }
    
    public void freeze(final long time) {
    	this.freeze(((float)time) / 1000);
    }
    
    public void freeze(final float time) {
		this.freezeTime = (System.currentTimeMillis() - startTime) / 1000;
		this.freezeDuration = time;
		this.timeOffset += this.freezeDuration;
		
		System.out.println("FREEZING for time: " + time + " at: " + this.freezeTime);
		
		for (LocationModel location : locations) {
    		location.freeze(this.freezeDuration);
    	}
		
		unfreeze(this.freezeTime + this.freezeDuration);
		
		gameScene.onFreeze();
	}
	
	public void unfreeze(final float time) {
		new Timer().schedule(
			new TimerTask() {
				
				@Override
				public void run() {
					if (time >= freezeTime + freezeDuration) {
						freezeTime = -1;
						freezeDuration = 0;
						
						for (LocationModel location : locations) {
				    		location.unfreeze();
				    	}
						
						gameScene.onUnfreeze();
						
						System.out.println("Unfreezing at time: " + time);
					}
				}
			},
			(long)(time) * 1000
		);
	}
    
    public void smog() {
    	gameScene.onSmog();
    }
    
    public void unsmog() {
    	// TODO implement
    }
    
    public void blur() {
    	// TODO implement
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
							
							// TODO remove on final
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
	
	public int getScore() {
		return this.score;
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
	
	public int getLives() {
		return this.lives;
	}
}
