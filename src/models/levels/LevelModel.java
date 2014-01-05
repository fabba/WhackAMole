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

/**
 * The level model or rather the game model is the model for the entire game.
 * Contains functions to play and load a game of whack-a-mole.
 */
public class LevelModel {

	private int numLevel, numberOfRounds, score, molesRemaining, lives;
	private float freezeTime, freezeDuration;
	private float smogTime, smogDuration;
	private float burnTime, burnDuration;
	private float startTime, timeOffset;
	
	private ArrayList<LocationModel> locations;
	private GameScene gameScene;
	private RoundModel currentRound;
	
	private LevelModel(int numLevel, GameScene scene) {
		this.numLevel = numLevel;
		this.locations = null;
		this.gameScene = scene;
		this.currentRound = null;
		this.numberOfRounds = -1;
		this.timeOffset = 0;
		this.freezeTime = -1;
		this.freezeDuration = 0;
		this.smogTime = -1;
		this.smogDuration = 0;
	}
	
	/** 
	 * Load a level from the database, initializes all that is necessary
	 * and places the moles on the locations for a specific round which
	 * are all also loaded.
	 * @param numLevel number of level to get from the database.
	 * @param numRound number of round to get from the database.
	 * @param scene the scene to load the level on.
	 * @return the level loaded.
	 */
	public static LevelModel loadLevel(int numLevel, int numRound, GameScene scene) {
		LevelModel level = new LevelModel(numLevel, scene);
		
		LevelAdapter levelAdapter = new LevelAdapter();
		levelAdapter.open();
		level.setLocations(levelAdapter.getLocations(numLevel));
		RoundModel round = levelAdapter.getRound(numRound, level);
		int numberOfRounds = levelAdapter.getNumberOfRounds(numLevel);
		levelAdapter.close();
		
		if (!level.getLocations().isEmpty() && round != null) {
			level.numberOfRounds = numberOfRounds;
			level.initializeRound(round);
			
			// TODO remove on final
			for (LocationModel location : level.locations) {
				for (MoleModel mole : location.getMoles()) {
					System.out.println("Location: " + location + " mole: " + mole + " queued at: " + mole.getTime());
				}
			}
		} else {
			return null;
		}
		
		return level;
	}
	
	/**
	 * Initialize a round for this level. 
	 * @param round
	 */
	private void initializeRound(RoundModel round) {
		this.currentRound = round;
		
		this.startTime = gameScene.getSecondsElapsedTotal();
		this.molesRemaining = this.currentRound.getMoles().size();
		this.score = 0;
		this.lives = 6; // TODO load from database?
		
		this.gameScene.onLivesUpdated(this.lives);
	}
	
	/**
	 * Load the next round if that exists.
	 * @return true if succes, false otherwise.
	 */
	public boolean nextRound() {
		if (currentRound.getNumRound() < numberOfRounds) { 
			for (LocationModel location : locations) {
				location.reset();
			}
			
			LevelAdapter levelAdapter = new LevelAdapter();
			levelAdapter.open();
			this.currentRound = levelAdapter.getRound(currentRound.getNumRound() + 1, this);
			levelAdapter.close();
			
			initializeRound(this.currentRound);
			
			return true;
		} else {
			return false;
		}
	}
	
	public void setLocations(ArrayList<LocationModel> locations) {
		this.locations = locations;
	}
    
	/**
	 * Creates a mole of type moleClass at location at time with appearanceTime.
	 * @param moleClass
	 * @param location
	 * @param time
	 * @param appearanceTime
	 * @return the mole created.
	 */
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
	
    /**
     * Create a list of moles based on the classes provided at times with
     * appearanceTimes. Note all three parameters should be of the same size
     * (obviously).
     * @param moleClasses
     * @param times
     * @param appearanceTimes
     * @return list of moles created.
     */
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
	    	
	    	for (int index : shuffledIndexes) {
	    		LocationModel location = locations.get(index);
	    			
	    		if (location.isRoomForMole(time, appearanceTime)) {
	    			MoleModel mole = createMole(moleClass, location, time, appearanceTime);
	    			
	    			if (mole != null) {
	    				location.addMole(mole);
	    				moles.add(mole);
	    			}
	    			break;
	    		}
	    	}
    	}
    	
    	return moles;
    }
	
    /**
     * Called on mole death, handles moles remaining and 
     * ends the round if non remaining.
     * @param mole which died.
     */
	public void onMoleDeath(MoleModel mole) {
		this.molesRemaining--;
		
		// TODO remove on final
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
	
	/**
	 * Burn the game for time, time just indicates the visual effect.
	 * @param time time in milliseconds
	 */
	public void burn(long time) {
		this.burn(((float)time) / 1000);
	}
	
	/**
	 * Burn the game for time, time just indicates the visual effect.
	 * @param time time in seconds.
	 */
    public void burn(float time) {
    	this.burnTime = gameScene.getSecondsElapsedTotal() - startTime;
		this.burnDuration = time;
		
		for (LocationModel location : locations) {
    		location.burn();
    	}
    	
    	gameScene.onBurn();
    	
    	unburn(this.burnDuration);
    }
    
    /**
     * 'Unburn' the game after time.
     * @param time time in seconds
     */
    private void unburn(float time) {
    	new Timer().schedule(
			new TimerTask() {
				
				@Override
				public void run() {
					// TODO should not be based on system time
					float time = gameScene.getSecondsElapsedTotal() - startTime;
					System.out.println("Trying to unburning at time: " + time);
					if (time + .1 >= burnTime + burnDuration) {
						burnTime = -1;
						burnDuration = 0;
						gameScene.onUnburn();
						System.out.println("Unburning at time: " + time);
					}
				}
			},
			(long)((double)time * 1000)
		);
    }
    
    /**
     * Freeze the game for time.
     * @param time time in milliseconds.
     */
    public void freeze(long time) {
    	this.freeze(((float)time) / 1000);
    }
    
    /**
     * Freeze the game for time.
     * @param time time in seconds.
     */
    public void freeze(float time) {
		this.freezeTime = gameScene.getSecondsElapsedTotal() - startTime;
		this.freezeDuration = time;
		this.timeOffset += this.freezeDuration;
		
		System.out.println("FREEZING for time: " + time + " at: " + this.freezeTime);
		
		for (LocationModel location : locations) {
    		location.freeze(this.freezeDuration);
    	}
		
		unfreeze(this.freezeDuration);
		
		gameScene.onFreeze();
	}
	
    /**
     * Unfreeze the game after time.
     * @param time time in seconds.
     */
	private void unfreeze(float time) {
		new Timer().schedule(
			new TimerTask() {
				
				@Override
				public void run() {
					float time = gameScene.getSecondsElapsedTotal() - startTime;
					if (time + .1 >= freezeTime + freezeDuration) {
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
			(long)((double)time * 1000)
		);
	}
    
	/**
	 * Smog the game for time, smog is just a visual effect.
	 * @param time time in milliseconds.
	 */
	public void smog(long time) {
    	this.smog(((float)time) / 1000);
    }
	
	/**
	 * Smog the game for time, smog is just a visual effect.
	 * @param time time in seconds.
	 */
    public void smog(float time) {
    	this.smogTime = gameScene.getSecondsElapsedTotal() - startTime;
		this.smogDuration = time;
		
    	unsmog(smogDuration);
    	gameScene.onSmog();
    }
    
    /**
     * 'Unsmog' the game after time.
     * @param time time in seconds.
     */
    private void unsmog(float time) {
    	new Timer().schedule(
			new TimerTask() {
				
				@Override
				public void run() {
					float time = gameScene.getSecondsElapsedTotal() - startTime;
					
					// TODO remove on final
					System.out.println("Trying to unsmog at " + time + " smogtime: " + smogTime + " smogduration: " + smogDuration);
					
					if (time + .1 >= smogTime + smogDuration) {
						smogTime = -1;
						smogDuration = 0;
						
						gameScene.onUnsmog();
						
						System.out.println("Unsmogging at time: " + time);
					}
				}
			},
			(long)((double)time * 1000)
		);
    	
		System.out.println("SMOGGING for time: " + time + " at: " + this.smogTime);
    }
    
    /**
     * Schedule a mole pop up at time for appearanceTime. Searches for an empty location
     * where this mole can pop up, if no location is found the mole is dropped, beware!
     * @param mole the mole to schedule.
     * @param time the time at which the mole is to appear.
     * @param prevTime the time for which the mole has to appear.
     */
    private void scheduleMolePopUp(final MoleModel mole, final float time, final float prevTime) {
    	System.out.println("Schedule time: " + (time - prevTime) + " prevTime: " + prevTime + 
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
							System.out.println("nextPopUpTime: " + nextPopUpTime + " time: " + time +
									" from mole: " + mole + " to mole: " + nextActiveMole);
							
							scheduleMolePopUp(nextActiveMole, nextPopUpTime, time);
						}
					} else {
						scheduleMolePopUp(popUpMole, location.getPopUpTime(popUpMole), time);
					}
				}
			},
			(long)(((double)(time - prevTime)) * 1000)
		);
    }
    
    /**
     * Play the current round.
     */
	public void playRound() {
		gameScene.onScoreUpdated();
		
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
	
	public float getStartTime() {
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
	
	public float getTimeOffset() {
		return this.timeOffset;
	}
}