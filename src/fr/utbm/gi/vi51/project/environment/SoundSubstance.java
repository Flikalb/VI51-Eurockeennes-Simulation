package fr.utbm.gi.vi51.project.environment;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.janusproject.jaak.envinterface.perception.Substance;

public class SoundSubstance extends Substance {

        protected WeakReference<Scene> _scene;

    
        
	public SoundSubstance(Object semantic, Scene scene) {
		super(semantic);
		_scene = new WeakReference<Scene>(scene);
	}
        
        
        public Scene getScene() {
            return _scene.get();
        }

	@Override
	public BigDecimal bigDecimalValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger bigIntegerValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte byteValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Substance decrement(Substance arg0) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Number getAmount() {
		// TODO Auto-generated method stub
		return 400;
	}

	@Override
	protected Substance increment(Substance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDisappeared() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short shortValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
