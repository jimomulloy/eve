/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.*;import java.awt.image.*;/** * A filter which acts as a superclass for filters which need to have the whole image in memory * to do their stuff. */public abstract class WholeImageFilter extends ImageFilter implements java.io.Serializable {	protected Rectangle transformedSpace;	protected Rectangle originalSpace;	protected ColorModel defaultRGBModel;	protected int[] inPixels;	protected byte[] inBytePixels;		/**	 * If true, then image pixels for images with an IndexColorModel ndex will be accumulated 	 * as bytes in inBytePixels. If false, they will be converted to the default RGB color model	 * and accumulated in inPixels.	 */	protected boolean canFilterIndexColorModel = false;	/**	 * Construct a WholeImageFilter	 */	public WholeImageFilter() {		defaultRGBModel = ColorModel.getRGBdefault();	}	protected void transformSpace(Rectangle rect) {	}		public void setDimensions(int width, int height) {		originalSpace = new Rectangle(0, 0, width, height);		transformedSpace = new Rectangle(0, 0, width, height);		transformSpace(transformedSpace);		consumer.setDimensions(transformedSpace.width, transformedSpace.height);	}	public void setColorModel(ColorModel model) {		if (canFilterIndexColorModel && model instanceof IndexColorModel)			consumer.setColorModel(model);		else			consumer.setColorModel(defaultRGBModel);	}		public void setPixels(int x, int y, int w, int h, ColorModel model, byte pixels[], int off, int scansize) {		int index = y * originalSpace.width + x;		int srcindex = off;		int srcinc = scansize - w;		int indexinc = originalSpace.width - w;		if (canFilterIndexColorModel) {			if (inBytePixels == null)				inBytePixels = new byte[originalSpace.width * originalSpace.height];			for (int dy = 0; dy < h; dy++) {				for (int dx = 0; dx < w; dx++)					inBytePixels[index++] = pixels[srcindex++];				srcindex += srcinc;				index += indexinc;			}		} else {			if (inPixels == null)				inPixels = new int[originalSpace.width * originalSpace.height];			for (int dy = 0; dy < h; dy++) {				for (int dx = 0; dx < w; dx++)					inPixels[index++] = model.getRGB(pixels[srcindex++] & 0xff);				srcindex += srcinc;				index += indexinc;			}		}	}	public void setPixels(int x, int y, int w, int h, ColorModel model, int pixels[], int off, int scansize) {		int index = y * originalSpace.width + x;		int srcindex = off;		int srcinc = scansize - w;		int indexinc = originalSpace.width - w;		if (inPixels == null)			inPixels = new int[originalSpace.width * originalSpace.height];		for (int dy = 0; dy < h; dy++) {			for (int dx = 0; dx < w; dx++) 				inPixels[index++] = model.getRGB(pixels[srcindex++]); 			srcindex += srcinc;			index += indexinc;		}	}}