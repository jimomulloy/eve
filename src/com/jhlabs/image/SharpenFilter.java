/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.image.*;public class SharpenFilter extends ConvolveFilter {	static final long serialVersionUID = -4883137561307845895L;		protected static float[] sharpenMatrix = {		 0.0f, -0.2f,  0.0f,		-0.2f,  1.8f, -0.2f,		 0.0f, -0.2f,  0.0f	};	public SharpenFilter() {		super(sharpenMatrix);	}	public String toString() {		return "Blur/Sharpen";	}}