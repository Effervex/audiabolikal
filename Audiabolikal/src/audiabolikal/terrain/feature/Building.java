package audiabolikal.terrain.feature;

import java.util.Collection;

import audiabolikal.Globals;

import com.jme3.math.Vector3f;

public class Building extends TerrainFeature {
	private final int MIN_DIMENSION = 3;
	private final int SIZE_MEAN = 5;
	private final int SIZE_SD = 1;

	public Building() {
		int sizeX = (int) Math.round(Math.max(MIN_DIMENSION, SIZE_MEAN
				+ Globals.random_.nextGaussian() * SIZE_SD));
		int sizeY = (int) Math.round(Math.max(MIN_DIMENSION, SIZE_MEAN
				+ Globals.random_.nextGaussian() * SIZE_SD));
		
	}

	@Override
	public boolean applyFeature(int[][] terrain) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vector3f getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Vector3f> getStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[][] getSurface() {
		// TODO Auto-generated method stub
		return null;
	}
}
