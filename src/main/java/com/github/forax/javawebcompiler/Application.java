package com.github.forax.javawebcompiler;

import module java.base;
import module java.compiler;

import tools.jackson.databind.ObjectMapper;

import javax.tools.ToolProvider;
import java.util.logging.Logger;

public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

  private record Diagnostic(long line, long column, String message) {} 

  static void main(String[] args) {
    var app = JExpress.express();

    // Serve the static frontend files from "public"
    app.use(JExpress.staticFiles(Path.of("public")));

    var objectMapper = new ObjectMapper();

    app.post("/compile", (req, res) -> {
      try {
        var body = req.bodyText();
        /*
        Request validation???
        */
          if (body == null || body.isBlank()) {
              res.status(400).json("error");
              return;
          }

        var tree = objectMapper.readTree(body);
          /*
            Request validation???
            */
          if (tree.get("code") == null) {
              res.status(400).json("error");
              return;
          }

        var sourceCode = tree.get("code").asString();

        var diagnostics = compileInMemory("Main", sourceCode);
        res.send(objectMapper.writeValueAsString(diagnostics));
      } catch (Exception e) {
        res.status(500).json("""
            {"error": "Internal Server Error"}
            """);
      }
    });

    app.listen(8080);
    System.out.println("Web site on http://localhost:8080/index.html");
    /*
    Add a logger
     */
      LOGGER.info("Web site on http://localhost:8080/index.html");

  }

<<<<<<< Updated upstream
  private static List<Diagnostic> compileInMemory(String className, String sourceCode) {
    var compiler = ToolProvider.getSystemJavaCompiler();
=======
  private static List<Map<String, Object>> compileInMemory(String className, String sourceCode) {
    /*
      Verification here
       */
      Objects.requireNonNull(className);
      Objects.requireNonNull(sourceCode);

      var compiler = ToolProvider.getSystemJavaCompiler();
>>>>>>> Stashed changes
    var diagnostics = new DiagnosticCollector<>();

    var file = new SimpleJavaFileObject(
        URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
        JavaFileObject.Kind.SOURCE) {
      @Override
      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
      }
    };

    var compilationUnits = List.of(file);
    var task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

    var success = task.call();
    var result = new ArrayList<Diagnostic>();

    if (!success) {
      for (var diagnostic : diagnostics.getDiagnostics()) {
        result.add(new Diagnostic(
            diagnostic.getLineNumber(),
            diagnostic.getColumnNumber(),
            diagnostic.getMessage(Locale.FRANCE)));
      }
    }
    return result;
  }
}