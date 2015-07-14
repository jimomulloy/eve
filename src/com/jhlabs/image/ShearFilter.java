/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.*;import java.awt.image.*;public class ShearFilter extends TransformFilter {	private float xangle = 0.0f;	private float yangle = 0.0f;	private float shx = 0.0f;	private float shy = 0.0f;	private float xoffset = 0.0f;	private float yoffset = 0.0f;	private boolean resize = true;	public ShearFilter() {	}	public void setResize(boolean resize) {		this.resize = resize;	}	public boolean isResize() {		return resize;	}	public void setXAngle(float xangle) {		this.xangle = xangle;		initialize();	}	public float getXAngle() {		return xangle;	}	public void setYAngle(float yangle) {		this.yangle = yangle;		initialize();	}	public float getYAngle() {		return yangle;	}	private void initialize() {		shx = (float)Math.sin(xangle);		shy = (float)Math.sin(yangle);	}		protected void transformSpace(Rectangle r) {		float tangent = (float)Math.tan(xangle);		xoffset = -r.height * tangent;		if (tangent < 0.0)			tangent = -tangent;		r.width = (int)(r.height * tangent + r.width + 0.999999f);		tangent = (float)Math.tan(yangle);		yoffset = -r.width * tangent;		if (tangent < 0.0)			tangent = -tangent;		r.height = (int)(r.width * tangent + r.height + 0.999999f);	}/*	public void imageComplete(int status) {try {		if (status == IMAGEERROR || status == IMAGEABORTED) {			consumer.imageComplete(status);			return;		}		int width = originalSpace.width;		int height = originalSpace.height;		float tangent = Math.tan(angle);		if (tangent < 0.0)			tangent = -tangent;		int newWidth = (int)(height * tangent + width + 0.999999);		int[] outPixels = new int[height*newWidth];		int inIndex = 0;		int yOffset = 0;		for (int y = 0; y < height; y++) {			float newCol;			if (angle >= 0.0)				newCol = y * tangent;			else				newCol = (height-y) * tangent;			int iNewCol = (int)newCol;			float f = newCol - iNewCol;			f = 1.0 - f;			int outIndex = yOffset+iNewCol;			int lastRGB = inPixels[inIndex];			for (int x = 0; x < width; x++) {				int rgb = inPixels[inIndex];				outPixels[outIndex] = ImageMath.mixColors(f, lastRGB, rgb);				lastRGB = rgb;				inIndex++;				outIndex++;			}			outPixels[outIndex] = ImageMath.mixColors(f, lastRGB, 0);			yOffset += newWidth;		}		consumer.setPixels(0, 0, newWidth, height, defaultRGBModel, outPixels, 0, newWidth);		consumer.imageComplete(status);		inPixels = null;}catch (Exception e) {	e.printStackTrace();}	}*/		protected void transformInverse(int x, int y, float[] out) {		out[0] = x + xoffset + (y * shx);		out[1] = y + yoffset + (x * shy);	}	public String toString() {		return "Distort/Shear...";	}}