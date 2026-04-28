package com.github.forax.javawebcompiler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CompilerTest {

  @Test
  public void compileValidCode() {
    var code = """
    public class Main {
      public static void main(String[] args) {
        System.out.println("Hello");
      }
    }
    """;
    var diagnostics = Compiler.compileInMemory("Main", code, new MemoryClassLoader(), false);
    assertTrue(diagnostics.isEmpty());
  }
}
