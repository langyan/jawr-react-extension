package net.jawr.web.resource.bundle.generator.js.jsx;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import net.jawr.web.config.JawrConfig;
import net.jawr.web.exception.BundlingProcessException;
import net.jawr.web.resource.bundle.factory.util.ClassLoaderResourceUtils;
import net.jawr.web.util.js.RhinoEngine;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

public class JSXTest {

	/** The coffee script suffix */
	private static final String JSX_SCRIPT_SUFFIX = "jsx";

	/** The coffee script options property name */
	private static final String JAWR_JS_GENERATOR_JSX_SCRIPT_OPTIONS = "jawr.js.generator.reactjsx.script.options";

	/** The coffee script generator location */
	private static final String JAWR_JS_GENERATOR_JSX_SCRIPT_LOCATION = "jawr.js.generator.reactjsx.script.location";

	/** The default coffee script JS location */
	private static final String DEFAULT_JSX_SCRIPT_JS_LOCATION = "net/jawr/web/resource/bundle/generator/js/jsx/JSXTransformer.js";

	/** The jawr config */
	private JawrConfig config;

	/** The coffee script options */
	private String options;

	/** The Rhino engine BundlesHandlerFactory*/
	private RhinoEngine rhino;

	@Test
	public void add() {
		rhino = new RhinoEngine();
		InputStream inputStream = getResourceInputStream(DEFAULT_JSX_SCRIPT_JS_LOCATION);
		rhino.evaluate("JSXTransformer.js", inputStream);

		Object b = null;

		String coffeeScriptSource = "React.render(<h1>Hello, world!</h1>,document.getElementById('example'));";

		Scriptable compileScope = rhino.newObject();
		compileScope.put("reactjsxScriptSource", compileScope,
				coffeeScriptSource);
		//options = "{sourceMap:true,sourceFilename:source.js}";
		try {
			b = rhino
					.evaluateString(
							compileScope,
							String.format(
									"JSXTransformer.transform(reactjsxScriptSource, '%s').code;",
									options), "JReactJsxScriptCompiler");
		} catch (JavaScriptException e) {
			throw new BundlingProcessException(e);
		}
		if (b != null) {

			System.out.println(b.toString());
		}
	}

	/**
	 * Returns the resource input stream
	 * 
	 * @param path
	 *            the resource path
	 * @return the resource input stream
	 */
	private InputStream getResourceInputStream(String path) {
		InputStream is = null;
		if (is == null) {
			try {
				is = ClassLoaderResourceUtils.getResourceAsStream(path, this);
			} catch (FileNotFoundException e) {
				throw new BundlingProcessException(e);
			}
		}

		return is;
	}
}
