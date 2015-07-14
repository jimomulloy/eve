/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.image.*;public class BlurFilter extends ConvolveFilter {	static final long serialVersionUID = -4753886159026796838L; 	 	protected static float[] blurMatrix = {		0.1f, 0.2f, 0.1f,		0.2f, 0.2f, 0.2f,		0.1f, 0.2f, 0.1f	};	private int blur = 2;		public BlurFilter() {		super((float[])blurMatrix.clone());	}	public void setBlur(int blur) {		this.blur = blur;		float[] m = (float[])blurMatrix.clone();		m[4] = (float)blur/10;		Kernel kernel = new Kernel(3, 3, m);		kernel.normalize();		setKernel(kernel);	}		public int getBlur() {		return blur;	}		public String toString() {		return "Blur/Simple Blur...";	}}