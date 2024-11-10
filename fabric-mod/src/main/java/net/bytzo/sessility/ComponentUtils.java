package net.bytzo.sessility;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;

public class ComponentUtils {

	/**
	 * Recursively copy the provided chat component and its siblings, replacing any
	 * existing set of translatable arguments with the specified arguments.
	 *
	 * @param  component the component to copy
	 * @param  args      set of replacement arguments
	 * @return           the copied chat component, with the replaced arguments
	 */
	public static Component componentWithArgs(Component component, Object... args) {

		// If a component's contents are translatable, set its new contents to another
		// translatable content with its arguments replaced, otherwise set the
		// component's new contents to the existing contents.
		ComponentContents existingContents = component.getContents();
		ComponentContents newContents;
		if (existingContents instanceof TranslatableContents contents) {
			newContents = new TranslatableContents(contents.getKey(), contents.getFallback(), args);
		} else {
			newContents = existingContents;
		}

		// Create a new component with the newly determined contents, while keeping the
		// style of the existing component.
		var newComponent = MutableComponent.create(newContents)
				.withStyle(component.getStyle());

		// Recursively apply the arguments to the siblings of the component
		for (var sibling : newComponent.getSiblings()) {
			newComponent.append(componentWithArgs(sibling));
		}

		return newComponent;
	}

	public static boolean isEmptyContents(Component component) {
		return component.getContents() == PlainTextContents.EMPTY;
	}

}
