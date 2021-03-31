import static java.lang.System.err;
import static java.lang.System.out;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.PrintStream;

public class Characters {
	public static void main(String[] args) {
		if (args.length == 0) {
			displayUsage(System.err);
			System.exit(1);
		}

		char ch = 32;
		String fontName = "Arial";
		int fontStyle = Font.BOLD;
		int fontSize = 60;

		// parse arguments
		for (int i = 0, consumed = 0; i < args.length; i++) {
			if (consumed == 1) {
				consumed = 0;
				continue;
			}

			String s = args[i];

			// if argument is an option
			if (s.startsWith("--")) {
				try {
					switch (s.substring(2)) {	// remove "--"
						case "help":
							displayUsage(System.out);
							System.exit(0);
							break;
						case "font":
							fontName = args[i + 1];
							consumed = 1;
							break;
						case "size":
							fontSize = Integer.parseInt(args[i + 1]);
							consumed = 1;
							break;
						case "italic":
							fontStyle |= Font.ITALIC;
							break;
						default:
							error("Unrecognized option \"%s\"", s);
							break;
					}
				} catch (IndexOutOfBoundsException ex) {			// if no argument after parameterized option
					error("Expected argument after \"%s\"", s);
				} catch (NumberFormatException ex) {				// if argument should be int but is not
					error("Invalid argument type after \"%s\"", s);
				}
			} else {
				// if argument is not an option and has not been consumed as an
				// argument, and <char> has not been set yet
				if (ch == 32) {
					ch = s.charAt(0);
				}
			}
		}

		// if <char> has somehow not been set
		if (ch == 32) {
			error("<char> argument missing or misplaced");
		}

		Font f = new Font(fontName, fontStyle, fontSize);
		
		// reflect character glyph about y-axis and get outline
		Shape outline = getCharacterOutline(f, ch);
		Rectangle bounds = outline.getBounds();

		// top left and bottom right points of bounding box
		var min = new Point(bounds.x, bounds.y);
		var max = new Point(bounds.x + bounds.width, bounds.y + bounds.height);

		// apply padding
		final int PAD = 5;
		min.translate(-PAD, -PAD);
		max.translate(PAD, PAD);

		// display character(s)
		for (int y = min.y; y < max.y; y += 2) {
			for (int x = min.x; x < max.x; x++) {
				out.print(outline.contains(x, y) ? ch : ' ');
			}
			out.println();
		}
		out.println();
	}

	/**
	 * Reflect character about the y-axis and get outline
	 * 
	 * @param f the {@code Font} to use
	 * @param c the character
	 * @return a {@code Shape}, which is the outline of the character
	 */
	static Shape getCharacterOutline(Font f, char c) {
		var reflectY = AffineTransform.getTranslateInstance(0, 45);
		var frc = new FontRenderContext(null, true, true);
		var gv = f.createGlyphVector(frc, c + "");
		return reflectY.createTransformedShape(gv.getGlyphOutline(0));
	}

	/**
	 * Display usage message to the specified output stream
	 * 
	 * @param p the {@code PrintStream} to output to 
	 */
	static void displayUsage(PrintStream p) {
		p.println("Usage: java Characters <char> [options]\n");
		p.println("Options:");
		p.println("    [--font <font_name>]");
		p.println("    [--size <font_size>]");
		p.println("    [--italic]");
	}

	/**
	 * Display error message and exit the program
	 * 
	 * @param msg the error message (can be a format string)
	 * @param args arguments for the error message, if formatted
	 */
	static void error(String msg, Object... args) {
		err.printf("Error: " + msg, args);
		System.exit(1);
	}
}
