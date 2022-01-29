module MandelbrotBackgroundCreator {
	requires java.desktop;
	requires commons.math3;
	requires aparapi;
	opens karabin.mandelbrot.drawing.gpu.kernel to aparapi;
}