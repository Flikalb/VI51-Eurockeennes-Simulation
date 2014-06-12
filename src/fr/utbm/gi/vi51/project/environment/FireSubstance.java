package fr.utbm.gi.vi51.project.environment;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.janusproject.jaak.envinterface.perception.Substance;

public class FireSubstance extends Substance {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2766910160439520238L;

	public FireSubstance(Object semantic) {
		super(semantic);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Substance increment(Substance s) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected Substance decrement(Substance s) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean isDisappeared() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Number getAmount() {
		// TODO Auto-generated method stub
		return 400;
	}

	@Override
	public byte byteValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short shortValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigInteger bigIntegerValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal bigDecimalValue() {
		// TODO Auto-generated method stub
		return null;
	}

}