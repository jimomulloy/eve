/* * Copyright (C) Jerry Huxtable 1998 */package com.jhlabs.math;public class BlackFunction implements BinaryFunction {	public boolean isBlack(int rgb) {		return rgb == 0xff000000;	}}