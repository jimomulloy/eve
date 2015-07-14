/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.image.RGBImageFilter;public class ChannelMixFilter extends RGBImageFilter implements java.io.Serializable {		static final long serialVersionUID = 4578927872126740383L;		public int blueGreen, redBlue, greenRed;	public int intoR, intoG, intoB;		public ChannelMixFilter() {		canFilterIndexColorModel = true;	}	public void setBlueGreen(int blueGreen) {		this.blueGreen = blueGreen;	}	public int getBlueGreen() {		return blueGreen;	}	public void setRedBlue(int redBlue) {		this.redBlue = redBlue;	}	public int getRedBlue() {		return redBlue;	}	public void setGreenRed(int greenRed) {		this.greenRed = greenRed;	}	public int getGreenRed() {		return greenRed;	}	public void setIntoR(int intoR) {		this.intoR = intoR;	}	public int getIntoR() {		return intoR;	}	public void setIntoG(int intoG) {		this.intoG = intoG;	}	public int getIntoG() {		return intoG;	}	public void setIntoB(int intoB) {		this.intoB = intoB;	}	public int getIntoB() {		return intoB;	}	public int filterRGB(int x, int y, int rgb) {		int a = rgb & 0xff000000;		int r = (rgb >> 16) & 0xff;		int g = (rgb >> 8) & 0xff;		int b = rgb & 0xff;		int nr = PixelUtils.clamp((intoR * (blueGreen*g+(255-blueGreen)*b)/255 + (255-intoR)*r)/255);		int ng = PixelUtils.clamp((intoG * (redBlue*b+(255-redBlue)*r)/255 + (255-intoG)*g)/255);		int nb = PixelUtils.clamp((intoB * (greenRed*r+(255-greenRed)*g)/255 + (255-intoB)*b)/255);		return a | (nr << 16) | (ng << 8) | nb;	}	public String toString() {		return "Colors/Mix Channels...";	}}