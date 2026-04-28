package com.github.forax.javawebcompiler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {

  @Test
  public void previewFeatureWithoutFlagShouldFail() {
    var code = """
    public class Main {
      public static void main(String[] args) {
        Object o = 42;
        switch (o) {
          case Integer i -> System.out.println(i);
          default -> {}
        }
      }
    }
    """;
    var diagnostics = Compiler.compileInMemory("Main", code, new MemoryClassLoader(), false);
    assertFalse(diagnostics.isEmpty());
  }

  @Test
  public void previewFeatureWithFlagShouldSucceed() {
    var code = """
    public class Main {
      public static void main(String[] args) {
        Object o = 42;
        switch (o) {
          case Integer i -> System.out.println(i);
          default -> {}
        }
      }
    }
    """;
    var diagnostics = Compiler.compileInMemory("Main", code, new MemoryClassLoader(), true);
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void previewToggleShouldChangeCompilationResult() {
    var code = """
    public class Main {
      public static void main(String[] args) {
        Object o = 42;
        switch (o) {
          case Integer i -> System.out.println(i);
          default -> {}
        }
      }
    }
    """;
    var loader = new MemoryClassLoader();
    var withoutPreview = Compiler.compileInMemory("Main", code, loader, false);
    var withPreview = Compiler.compileInMemory("Main", code, loader, true);
    assertFalse(withoutPreview.isEmpty());
    assertTrue(withPreview.isEmpty());
  }
}
