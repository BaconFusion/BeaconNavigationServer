package me.glor.me.glor.SmartphoneSensorNavigation;

import me.glor.Callee;
import me.glor.Table;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Created by glor on 9/18/16.
 */
public class SmartphoneSensorCallee implements Callee<SmartphoneSensors> {
	DataOutputStream dos;

	public SmartphoneSensorCallee(OutputStream outputStream) {
		dos = new DataOutputStream(outputStream);
	}

	private SmartphoneSensorCallee() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void calcPosition(Collection<SmartphoneSensors> collection) {

	}

	//remove
	@Override
	public void setTable(Table table) {

	}
}
