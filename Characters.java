import static java.lang.System.out;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Characters {
	public static void main(String[] args) {
		if (args.length == 0) {
			out.println("ERR: Expected 1 argument but got 0");
			out.println("    java Characters");
			out.println("                   ^^");
			return;
		}

		args[0] = args[0].substring(0, 1);

		final int COLS = 80;
		final int ROWS = 50;

		Font f = new Font("Arial", Font.BOLD, 60);
		var reflectY = AffineTransform.getTranslateInstance(0, 45);
		var frc = new FontRenderContext(null, true, true);
		var gv = f.createGlyphVector(frc, args[0]);
		var outline = reflectY.createTransformedShape(gv.getGlyphOutline(0));

		for (int y = 0; y < ROWS; y += 2) {
			for (int x = 0; x < COLS; x++) {
				out.print(outline.contains(x, y) ? args[0] : " ");
			}
			out.println();
		}
	}
}
