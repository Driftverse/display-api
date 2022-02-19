package hyleo.animations.text;

import java.awt.Color;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

@Data
@Builder
@NoArgsConstructor(staticName = "gray")
@AllArgsConstructor
@Accessors(fluent = true)
public class Palette {

	@Default
	final List<TextColor> colors = List.of(NamedTextColor.GRAY);

	@Default
	final int depth = 1;

	public int size() {
		return colors().size() * depth();
	}

	public TextColor color(int color) {

		TextColor color1 = colors().get(firstColor(depth, color));

		TextColor color2 = colors().get(secondColor(colors.size(), depth, color));

		System.out.println(new Color(color1.red(), color1.green(), color1.blue()) + " -- "
				+ new Color(color2.red(), color2.green(), color2.blue()));

		int red = color(color % depth, depth, color1, color2, (c) -> c.red());
		int green = color(color % depth, depth, color1, color2, (c) -> c.green());
		int blue = color(color % depth, depth, color1, color2, (c) -> c.blue());

		return TextColor.color(red, green, blue);

	}

	public static int firstColor(int depth, int color) {
		return color / depth;
	}

	public static int secondColor(int colors, int depth, int color) {
		return ((color + depth) / depth % colors);
	}

	public static int distance(int depth, TextColor color1, TextColor color2, Function<TextColor, Integer> function) {

		int x1 = function.apply(color1);
		int x2 = function.apply(color2);

		return (Integer.max(x1, x2) - Integer.min(x1, x2)) / depth;

	}

	public static int direction(TextColor color1, TextColor color2, Function<TextColor, Integer> function) {
		return Integer.compare(function.apply(color2), function.apply(color1));
	}

	public static int color(int color, int depth, TextColor color1, TextColor color2,
			Function<TextColor, Integer> function) {
		return function.apply(color1)
				+ color * distance(depth, color1, color2, function) * direction(color1, color2, function);
	}

	public static void main(String[] args) {

		Palette palette = Palette.builder().depth(40)
				.colors(List.of(TextColor.color(255, 0, 0), TextColor.color(0, 255, 0), TextColor.color(0, 0, 255)))
				.build();

		System.out.println("Should be 0");
		IntStream.range(0, 40 * 3).forEach(i -> {
			TextColor color = palette.color(i);
			System.out.println("Frame: " + i + " " + new Color(color.red(), color.green(), color.blue()) + "\n");
		});

		System.out.println(
				"Distance: " + distance(255, TextColor.color(0, 0, 0), TextColor.color(0, 255, 0), c -> c.blue()));
	}
}