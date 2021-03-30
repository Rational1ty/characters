import static java.lang.System.err;
import static java.lang.System.out;

import java.awt.Font;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Characters {
	public static void main(String[] args) {
		if (args.length == 0) {
			err.println("ERR: Expected 1 argument but got 0");
			err.println("    java Characters");
			err.println("                   ^^");
			return;
		}

		final int PAD = 5;

		args[0] = args[0].substring(0, 1);

		Font f = new Font("Arial", Font.BOLD, 60);
		
		var reflectY = AffineTransform.getTranslateInstance(0, 45);
		var frc = new FontRenderContext(null, true, true);
		var gv = f.createGlyphVector(frc, args[0]);
		var outline = reflectY.createTransformedShape(gv.getGlyphOutline(0));
		var bounds = outline.getBounds();

		var min = new Point(bounds.x, bounds.y);
		var max = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
		min.translate(-PAD, -PAD);
		max.translate(PAD, PAD);

		for (int y = min.y; y < max.y; y += 2) {
			for (int x = min.x; x < max.x; x++) {
				out.print(outline.contains(x, y) ? args[0] : " ");
			}
			out.println();
		}
		out.println();
	}
}
