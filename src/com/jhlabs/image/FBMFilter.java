/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.image;import java.awt.image.*;import java.util.*;import com.jhlabs.math.*;public class FBMFilter extends RGBImageFilter implements MutatableFilter, Cloneable, java.io.Serializable {	public final static int NOISE = 0;	public final static int RIDGED = 1;	public final static int VLNOISE = 2;	public final static int SCNOISE = 3;	public final static int CELLULAR = 4;	private float scale = 32;	private float stretch = 1.0f;	private float angle = 0.0f;	private float amount = 1.0f;	private float H = 1.0f;	private float octaves = 4.0f;	private float lacunarity = 2.5f;	private float gain = 0.5f;	private float bias = 0.5f;	private int operation;	private float m00 = 1.0f;	private float m01 = 0.0f;	private float m10 = 0.0f;	private float m11 = 1.0f;	private float min;	private float max;	private Colormap colormap = new Gradient();	private boolean ridged;	private FBM fBm;	protected Random random = new Random();	private int basisType = NOISE;	private Function2D basis;	public FBMFilter() {		setBasisType(NOISE);	}	public void setAmount(float amount) {		this.amount = amount;	}	public float getAmount() {		return amount;	}	public void setOperation(int operation) {		this.operation = operation;	}		public int getOperation() {		return operation;	}		public void setScale(float scale) {		this.scale = scale;	}	public float getScale() {		return scale;	}	public void setStretch(float stretch) {		this.stretch = stretch;	}	public float getStretch() {		return stretch;	}	public void setAngle(float angle) {		this.angle = angle;		float cos = (float)Math.cos(this.angle);		float sin = (float)Math.sin(this.angle);		m00 = cos;		m01 = sin;		m10 = -sin;		m11 = cos;	}	public float getAngle() {		return angle;	}	public void setOctaves(float octaves) {		this.octaves = octaves;		fBm = makeFBM(H, octaves, lacunarity);	}	public float getOctaves() {		return octaves;	}	public void setH(float H) {		this.H = H;		fBm = makeFBM(H, octaves, lacunarity);	}	public float getH() {		return H;	}	public void setLacunarity(float lacunarity) {		this.lacunarity = lacunarity;		fBm = makeFBM(H, octaves, lacunarity);	}	public float getLacunarity() {		return lacunarity;	}	public void setGain(float gain) {		this.gain = gain;	}	public float getGain() {		return gain;	}	public void setBias(float bias) {		this.bias = bias;	}	public float getBias() {		return bias;	}	public void setColormap(Colormap colormap) {		this.colormap = colormap;	}		public Colormap getColormap() {		return colormap;	}		public void setDimensions(int width, int height) {		super.setDimensions(width, height);		fBm = makeFBM(H, octaves, lacunarity);	}	public void setBasisType(int basisType) {		this.basisType = basisType;		switch (basisType) {		default:		case NOISE:			basis = new Noise();			break;		case RIDGED:			basis = new RidgedFBM();			break;		case VLNOISE:			basis = new VLNoise();			break;		case SCNOISE:			basis = new SCNoise();			break;		case CELLULAR:			basis = new CellularFunction2D();			break;		}	}	public int getBasisType() {		return basisType;	}	public void setBasis(Function2D basis) {		this.basis = basis;	}	public Function2D getBasis() {		return basis;	}	protected FBM makeFBM(float H, float lacunarity, float octaves) {		FBM fbm = new FBM(H, lacunarity, octaves, basis);		float[] minmax = Noise.findRange(fbm, null);		min = minmax[0];		max = minmax[1];		return fbm;	}		public int filterRGB(int x, int y, int rgb) {		float nx = m00*x + m01*y;		float ny = m10*x + m11*y;		nx /= scale;		ny /= scale * stretch;		float f = fBm.evaluate(nx, ny);		// Normalize to 0..1		f = (f-min)/(max-min);//		f = (f + 0.5) * 0.5;		f = ImageMath.gain(f, gain);		f = ImageMath.bias(f, bias);		f *= amount;		int a = rgb & 0xff000000;		int v;		if (colormap != null)			v = colormap.getColor(f);		else {			v = PixelUtils.clamp((int)(f*255));			int r = v << 16;			int g = v << 8;			int b = v;			v = a|r|g|b;		}		if (operation != PixelUtils.REPLACE)			v = PixelUtils.combinePixels(rgb, v, operation);		return v;	}	public void mutate(int amount, ImageFilter d, boolean keepShape, boolean keepColors) {		FBMFilter dst = (FBMFilter)d;		if (keepShape || amount == 0) {			dst.setScale(getScale());			dst.setAngle(getAngle());			dst.setStretch(getStretch());			dst.setAmount(getAmount());			dst.setLacunarity(getLacunarity());			dst.setOctaves(getOctaves());			dst.setH(getH());			dst.setGain(getGain());			dst.setBias(getBias());			dst.setColormap(getColormap());		} else {			dst.scale = mutate(scale, 0.6f, 4, 3, 64);			dst.setAngle(mutate(angle, 0.6f, ImageMath.PI/2));			dst.stretch = mutate(stretch, 0.6f, 5, 1, 10);			dst.amount = mutate(amount, 0.6f, 0.2f, 0, 1);			dst.lacunarity = mutate(lacunarity, 0.5f, 0.5f, 0, 3);			dst.octaves = mutate(octaves, 0.9f, 0.2f, 0, 12);			dst.H = mutate(H, 0.7f, 0.2f, 0, 1);			dst.gain = mutate(gain, 0.2f, 0.2f, 0, 1);			dst.bias = mutate(bias, 0.2f, 0.2f, 0, 1);		}		if (keepColors || amount == 0)			dst.setColormap(getColormap());		else			dst.setColormap(Gradient.randomGradient());	}		private float mutate(float n, float probability, float amount, float lower, float upper) {		if (random.nextFloat() >= probability)			return n;		return ImageMath.clamp(n + amount * (float)random.nextGaussian(), lower, upper);	}	private float mutate(float n, float probability, float amount) {		if (random.nextFloat() >= probability)			return n;		return n + amount * (float)random.nextGaussian();	}	public Object clone() {		FBMFilter f = (FBMFilter)super.clone();		f.fBm = makeFBM(f.H, f.octaves, f.lacunarity);		return f;	}		public String toString() {		return "Texture/Fractal Brownian Motion...";	}	}